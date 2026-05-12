package com.example.arogya_sahaya.data.local

class VitalRepository(
    private val dao: VitalDao
) {

    suspend fun insertVital(vital: VitalEntity) {
        dao.insertVital(vital)
    }

    suspend fun getVitals(uid: String): List<VitalEntity> {
        return dao.getVitals(uid)
    }
}