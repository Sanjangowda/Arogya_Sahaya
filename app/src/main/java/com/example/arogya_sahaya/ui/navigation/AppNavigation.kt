package com.example.arogya_sahaya.ui.navigation

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.arogya_sahaya.ui.screens.asha.AshaConnectScreen
import com.example.arogya_sahaya.ui.screens.dashboard.DashboardScreen
import com.example.arogya_sahaya.ui.screens.medicine.AddMedicineScreen
import com.example.arogya_sahaya.ui.screens.medicine.PillReminderScreen
import com.example.arogya_sahaya.ui.screens.profile.ProfileScreen
import com.example.arogya_sahaya.ui.screens.vitals.VitalLogScreen
import com.example.arogya_sahaya.auth.FirebaseAuthManager
import com.example.arogya_sahaya.ui.auth.LoginScreen
import com.example.arogya_sahaya.ui.auth.SignupScreen
import com.example.arogya_sahaya.ui.screens.sos.SosScreen
import androidx.compose.runtime.LaunchedEffect
import com.example.arogya_sahaya.data.local.DatabaseProvider
import com.example.arogya_sahaya.data.local.ProfileRepository
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.coroutines.launch

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val context = LocalContext.current

    var showEmergencyDialog by remember { mutableStateOf(false) }
    var emergencyPhone by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return@LaunchedEffect

        val profile = withContext(Dispatchers.IO) {
            ProfileRepository(
                DatabaseProvider.getDatabase().profileDao()
            ).getProfile(uid)
        }

        emergencyPhone = profile?.emergencyContact ?: ""
    }


    Scaffold(

        bottomBar = {
            val currentRoute =
                navController.currentBackStackEntryAsState().value
                    ?.destination?.route

            if (
                currentRoute != "login" &&
                currentRoute != "signup"
            ) {
                BottomBar(navController)
            }
        }


    ) { padding ->
        
        val startDestination =
            if (FirebaseAuthManager.isUserLoggedIn()) {
               Screen.Dashboard.route
            } else {
                "login"
            }

        NavHost(
            navController = navController,
            startDestination = startDestination,
            modifier = Modifier.padding(padding)
        ){

            composable("login") {
                LoginScreen(navController)
            }

            composable("signup") {
                SignupScreen(navController)
            }

            composable(Screen.Dashboard.route) {
                DashboardScreen(navController)
            }

            composable(Screen.Medicine.route) {
                PillReminderScreen(navController)
            }

            composable(Screen.Vitals.route) {
                VitalLogScreen(navController)
            }

            composable("add_medicine") {
                AddMedicineScreen(navController)
            }

            composable("add_vital") {
                VitalLogScreen(navController)
            }

            composable("sos") {
                SosScreen(navController)
            }

            composable(Screen.Profile.route) {
                ProfileScreen(navController)
            }
        }
    }

    val currentRoute =
        navController.currentBackStackEntryAsState().value
            ?.destination?.route

    if (
        currentRoute != "login" &&
        currentRoute != "signup"
    ) {

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.BottomStart
        ) {
            SosFloatingButton(
                modifier = Modifier.padding(
                    start = 20.dp,
                    bottom = 120.dp
                ),
                onClick = {
                    showEmergencyDialog = true
                }
            )
        }
    }



    // 🔴 SOS Dialog
    if (showEmergencyDialog) {
        AlertDialog(
            onDismissRequest = { showEmergencyDialog = false },
            title = { Text("Emergency SOS") },
            text = { Text("Send emergency alert now?") },
            confirmButton = {
                TextButton(onClick = {
                    showEmergencyDialog = false

                    val uid = FirebaseAuth.getInstance().currentUser?.uid

                    if (uid != null) {
                        kotlinx.coroutines.CoroutineScope(kotlinx.coroutines.Dispatchers.IO).launch {
                            val latestProfile = ProfileRepository(
                                DatabaseProvider.getDatabase().profileDao()
                            ).getProfile(uid)

                            withContext(kotlinx.coroutines.Dispatchers.Main) {
                                sendEmergencyAlert(
                                    context,
                                    latestProfile?.emergencyContact
                                )
                            }
                        }
                    } else {
                        sendEmergencyAlert(context, emergencyPhone)
                    }
                }) {
                    Text("Send")
                }
            },
            dismissButton = {
                TextButton(onClick = { showEmergencyDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
fun SosFloatingButton(onClick: () -> Unit, modifier: Modifier = Modifier)
{
    FloatingActionButton(
        modifier = modifier,
        onClick = onClick,
        containerColor = Color(0xFFEF4444),
        contentColor = Color.White
    ) {
        Text("SOS")
    }
}

@Composable
fun BottomBar(navController: NavController) {
    NavigationBar {
        NavigationBarItem(
            selected = false,
            onClick = { navController.navigate("dashboard") },
            icon = { Text("🏠") },
            label = { Text("Home") }
        )
        NavigationBarItem(
            selected = false,
            onClick = { navController.navigate("medicine") },
            icon = { Text("💊") },
            label = { Text("Medicine") }
        )
        NavigationBarItem(
            selected = false,
            onClick = { navController.navigate("vitals") },
            icon = { Text("❤️") },
            label = { Text("Vitals") }
        )
        NavigationBarItem(
            selected = false,
            onClick = { navController.navigate("profile") },
            icon = { Text("👤") },
            label = { Text("Profile") }
        )
    }
}

// SOS ACTION
private fun sendEmergencyAlert(
    context: Context,
    phoneNumber: String?
) {
    val number = when {
        phoneNumber.isNullOrBlank() -> "112"
        phoneNumber.startsWith("+") -> phoneNumber
        phoneNumber.length == 10 -> "+91$phoneNumber"
        else -> phoneNumber
    }

    val message = "Emergency! I need help immediately."

    try {
        val smsManager = android.telephony.SmsManager.getDefault()
        smsManager.sendTextMessage(number, null, message, null, null)
    } catch (e: Exception) {
        e.printStackTrace()

        // fallback → open SMS app
        val intent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("smsto:$number")
            putExtra("sms_body", message)
        }
        context.startActivity(intent)
    }
}