package com.example.arogya_sahaya.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "medicines")
data class MedicineEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val userId: String,
    val name: String,
    val dosage: String,
    val time: String,
    val status: String = "Pending"
)