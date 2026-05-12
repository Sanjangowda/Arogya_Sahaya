package com.example.arogya_sahaya.ui.screens.asha

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.arogya_sahaya.ui.theme.Background
import com.example.arogya_sahaya.ui.theme.TextSecondary
import com.example.arogya_sahaya.ui.utils.appCard


data class HealthEvent(
    val title: String,
    val location: String,
    val date: String,
    val daysLeft: Int
)

@Composable
fun AshaConnectScreen(navController: NavController) {

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Background),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        items(3) {
            Card(
                modifier = Modifier.appCard(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Health Camp", fontWeight = FontWeight.Bold)
                    Text("PHC Center", color = TextSecondary)
                }
            }
        }
    }
}
