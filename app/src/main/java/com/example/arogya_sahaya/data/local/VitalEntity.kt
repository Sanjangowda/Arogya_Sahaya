package com.example.arogya_sahaya.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "vitals")
data class VitalEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val userId: String,
    val bloodPressure: String,
    val bloodSugar: String,
    val heartRate: String,
    val date: String
)