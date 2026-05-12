package com.example.arogya_sahaya.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "profile")
data class ProfileEntity(
    @PrimaryKey
    val userId: String,

    val name: String,
    val age: String,
    val medicalCondition: String,
    val emergencyContact: String
)