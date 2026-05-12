package com.example.arogya_sahaya.data.local

class ProfileRepository(
    private val dao: ProfileDao
) {
    suspend fun saveProfile(profile: ProfileEntity) {
        dao.saveProfile(profile)
    }

    suspend fun getProfile(uid: String): ProfileEntity? {
        return dao.getProfile(uid)
    }
}