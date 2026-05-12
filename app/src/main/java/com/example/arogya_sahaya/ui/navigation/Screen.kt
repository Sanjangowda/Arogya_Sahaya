package com.example.arogya_sahaya.ui.navigation

sealed class Screen(val route: String) {
    object Dashboard : Screen("dashboard")
    object Medicine : Screen("medicine")
    object AddMedicine : Screen("add_medicine")
    object Vitals : Screen("vitals")
    object Asha : Screen("asha")
    object Sos : Screen("sos")
    object Profile : Screen("profile")

}