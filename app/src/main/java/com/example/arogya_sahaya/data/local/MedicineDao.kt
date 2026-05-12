package com.example.arogya_sahaya.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface MedicineDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMedicine(medicine: MedicineEntity)

    @Query("SELECT * FROM medicines WHERE userId = :uid")
    suspend fun getMedicines(uid: String): List<MedicineEntity>

    @Query("UPDATE medicines SET status = :status WHERE id = :id")
    suspend fun updateMedicineStatus(id: Int, status: String)

    @Query("DELETE FROM medicines WHERE id = :id")
    suspend fun deleteMedicine(id: Int)
}