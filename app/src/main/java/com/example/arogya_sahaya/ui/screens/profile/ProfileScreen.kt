package com.example.arogya_sahaya.ui.screens.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.arogya_sahaya.auth.FirebaseAuthManager
import com.example.arogya_sahaya.ui.screens.profile.ProfileViewModel
import com.example.arogya_sahaya.data.local.ProfileEntity

private val Background = Color(0xFFF6F8FA)
private val TextPrimary = Color(0xFF111827)
private val TextSecondary = Color(0xFF6B7280)
private val CardBorder = Color(0xFFE5E7EB)
private val Primary = Color(0xFF2F9EB3)
private val PrimaryLight = Color(0xFFE8F6FA)

data class ProfileData(
    var name: String,
    var age: String,
    var condition: String,
    var phone: String
)

// Profile Screen
@Composable
fun ProfileScreen(navController: NavController) {

    val viewModel: ProfileViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
    var profileState by remember {
        mutableStateOf<com.example.arogya_sahaya.data.local.ProfileEntity?>(null)
    }

    LaunchedEffect(Unit) {
        viewModel.loadProfile {
            profileState = it
        }
    }

    var showDialog by remember { mutableStateOf(false) }

    val profile = profileState?.let {
        ProfileData(
            name = it.name,
            age = it.age,
            condition = it.medicalCondition,
            phone = it.emergencyContact
        )
    } ?: ProfileData(
        name = "Add Name",
        age = "-",
        condition = "Not set",
        phone = "Not set"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = "Profile",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = TextPrimary
        )

        Spacer(modifier = Modifier.height(24.dp))

        Box(
            modifier = Modifier
                .size(92.dp)
                .clip(CircleShape)
                .background(PrimaryLight),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = profile.name.take(2).uppercase(),
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = Primary
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        Text(
            text = profile.name,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = TextPrimary
        )

        Text(
            text = "${profile.age} years",
            fontSize = 14.sp,
            color = TextSecondary
        )

        Spacer(modifier = Modifier.height(22.dp))

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, CardBorder, RoundedCornerShape(20.dp)),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                ProfileRow("Medical Conditions", profile.condition)
                ProfileRow("Emergency Contact", profile.phone)
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = { showDialog = true },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(50.dp)
        ) {
            Text("Edit Profile", fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                FirebaseAuthManager.logout()

                navController.navigate("login") {
                    popUpTo(0)
                    launchSingleTop = true
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFEF4444)
            )
        ) {
            Text("Logout")
        }
    }

    if (showDialog) {
        EditProfileDialog(
            profile = profile,
            onDismiss = { showDialog = false },
            onSave = {
                viewModel.saveProfile(
                    ProfileEntity(
                        userId = com.google.firebase.auth.FirebaseAuth
                            .getInstance()
                            .currentUser?.uid ?: "",
                        name = it.name,
                        age = it.age,
                        medicalCondition = it.condition,
                        emergencyContact = it.phone
                    )
                )

                viewModel.loadProfile { updatedProfile ->
                    profileState = updatedProfile
                }

                showDialog = false
            }
        )
    }

}

@Composable
fun EditProfileDialog(
    profile: ProfileData,
    onDismiss: () -> Unit,
    onSave: (ProfileData) -> Unit
) {

    var name by remember { mutableStateOf(profile.name) }
    var age by remember { mutableStateOf(profile.age) }
    var condition by remember { mutableStateOf(profile.condition) }
    var phone by remember { mutableStateOf(profile.phone) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Edit Profile") },
        text = {
            Column {
                OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Name") })
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(value = age, onValueChange = { age = it }, label = { Text("Age") })
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(value = condition, onValueChange = { condition = it }, label = { Text("Condition") })
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(value = phone, onValueChange = { phone = it }, label = { Text("Phone") })
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onSave(
                        ProfileData(name, age, condition, phone)
                    )
                }
            ) {
                Text("Save")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

@Composable
fun ProfileRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, color = TextSecondary, fontSize = 13.sp)
        Text(value, color = TextPrimary, fontWeight = FontWeight.SemiBold)
    }
}