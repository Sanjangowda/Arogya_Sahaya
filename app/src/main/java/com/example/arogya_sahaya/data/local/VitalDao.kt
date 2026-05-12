package com.example.arogya_sahaya.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface VitalDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVital(vital: VitalEntity)

    @Query("SELECT * FROM vitals WHERE userId = :uid ORDER BY id DESC")
    suspend fun getVitals(uid: String): List<VitalEntity>
}