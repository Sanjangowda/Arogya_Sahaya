package com.example.arogya_sahaya.data.local

class MedicineRepository(
    private val dao: MedicineDao
) {

    suspend fun insertMedicine(medicine: MedicineEntity) {
        dao.insertMedicine(medicine)
    }

    suspend fun getMedicines(uid: String): List<MedicineEntity> {
        return dao.getMedicines(uid)
    }

    suspend fun updateMedicineStatus(id: Int, status: String) {
        dao.updateMedicineStatus(id, status)
    }

    suspend fun deleteMedicine(id: Int) {
        dao.deleteMedicine(id)
    }
}