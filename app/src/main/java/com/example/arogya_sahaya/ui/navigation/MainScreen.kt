package com.example.arogya_sahaya.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.arogya_sahaya.ui.screens.dashboard.DashboardScreen
import com.example.arogya_sahaya.ui.screens.medicine.PillReminderScreen
import com.example.arogya_sahaya.ui.screens.profile.ProfileScreen
import com.example.arogya_sahaya.ui.screens.vitals.VitalLogScreen

@Composable
fun MainScreen(rootNavController: NavController) {

    val navController = rememberNavController()

    Scaffold(
        bottomBar = {
            BottomBar(navController)
        }
    ) { padding ->

        NavHost(
            navController = navController,
            startDestination = Screen.Dashboard.route,
            modifier = Modifier.padding(padding)
        ) {

            composable(Screen.Dashboard.route) {
                DashboardScreen(navController)
            }

            composable(Screen.Medicine.route) {
                PillReminderScreen(navController)
            }

            composable(Screen.Vitals.route) {
                VitalLogScreen(navController)
            }

            composable(Screen.Profile.route) {
                ProfileScreen(rootNavController)
            }
        }
    }
}



fun composable(route: Any, function: () -> Unit) {}
