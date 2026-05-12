package com.example.arogya_sahaya.ui.screens.vitals

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.arogya_sahaya.data.local.VitalEntity

private val Background = Color(0xFFF6F8FA)
private val TextPrimary = Color(0xFF111827)
private val TextSecondary = Color(0xFF6B7280)
private val CardBorder = Color(0xFFE5E7EB)
private val Primary = Color(0xFF2F9EB3)
private val PrimaryLight = Color(0xFFE8F6FA)

@Composable
fun VitalLogScreen(navController: NavController) {
    val viewModel: VitalViewModel = viewModel()
    var vitals by remember {
        mutableStateOf<List<VitalEntity>>(emptyList())
    }

    LaunchedEffect(Unit) {
        viewModel.getVitals {
            vitals = it
        }
    }

    var showDialog by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
    ) {

        // 🔹 EXISTING CONTENT (UNCHANGED)
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(
                text = "Vitals",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (vitals.isEmpty()) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, CardBorder, RoundedCornerShape(20.dp)),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(18.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "No readings added yet",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Tap + to add blood pressure, sugar, and heart rate readings.",
                            fontSize = 13.sp,
                            color = TextSecondary
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(vitals) { vital ->
                        VitalCardItem(vital)
                    }

                    item {
                        Spacer(modifier = Modifier.height(100.dp))
                    }
                }
            }
        }

        // 🔹 FIXED FAB (CORRECT POSITION)
        FloatingActionButton(
            onClick = { showDialog = true },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(20.dp)
                .size(56.dp),
            containerColor = Color(0xFFE8D9FF),
            contentColor = Color(0xFF3B2A78)
        ) {
            Text("+", fontSize = 22.sp, fontWeight = FontWeight.Bold)
        }
    }

    // 🔹 DIALOG (UNCHANGED)
    if (showDialog) {
        AddVitalDialog(
            onDismiss = { showDialog = false },
            onSave = { vital ->
                viewModel.addVital(
                    bloodPressure = vital.bloodPressure,
                    bloodSugar = vital.bloodSugar,
                    heartRate = vital.heartRate,
                    date = vital.date
                )
                viewModel.getVitals { updatedVitals ->
                    vitals = updatedVitals
                }
                showDialog = false
            }
        )
    }
}
@Composable
private fun VitalCardItem(vital: VitalEntity) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, CardBorder, RoundedCornerShape(20.dp)),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "BP: ${vital.bloodPressure}/${vital.bloodPressure} mmHg",
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                Text(
                    text = vital.date,
                    fontSize = 12.sp,
                    color = TextSecondary
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Sugar: ${vital.bloodSugar} mg/dL",
                color = TextPrimary,
                fontSize = 14.sp
            )

            Text(
                text = "Heart Rate: ${vital.heartRate} bpm",
                color = TextPrimary,
                fontSize = 14.sp
            )

            Spacer(modifier = Modifier.height(6.dp))

        }
    }
}

@Composable
private fun AddVitalDialog(
    onDismiss: () -> Unit,
    onSave: (VitalEntity) -> Unit
) {
    var systolic by remember { mutableStateOf("") }
    var diastolic by remember { mutableStateOf("") }
    var sugar by remember { mutableStateOf("") }
    var heartRate by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Add Reading",
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column {
                OutlinedTextField(
                    value = systolic,
                    onValueChange = { systolic = it },
                    label = { Text("Systolic BP") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = diastolic,
                    onValueChange = { diastolic = it },
                    label = { Text("Diastolic BP") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = sugar,
                    onValueChange = { sugar = it },
                    label = { Text("Blood Sugar (mg/dL)") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = heartRate,
                    onValueChange = { heartRate = it },
                    label = { Text("Heart Rate (bpm)") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (
                        systolic.isNotBlank() &&
                        diastolic.isNotBlank() &&
                        sugar.isNotBlank() &&
                        heartRate.isNotBlank()
                    ) {
                        onSave(
                            VitalEntity(
                                userId = "",
                                bloodPressure = "${systolic.trim()}/${diastolic.trim()}",
                                bloodSugar = sugar.trim(),
                                heartRate = heartRate.trim(),
                                date = "Today"
                            )
                        )
                    }
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