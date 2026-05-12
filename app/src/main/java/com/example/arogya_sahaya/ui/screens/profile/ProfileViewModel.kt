package com.example.arogya_sahaya.ui.screens.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.arogya_sahaya.data.local.DatabaseProvider
import com.example.arogya_sahaya.data.local.ProfileEntity
import com.example.arogya_sahaya.data.local.ProfileRepository
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

class ProfileViewModel : ViewModel() {

    private val repository =
        ProfileRepository(
            DatabaseProvider.getDatabase().profileDao()
        )

    private val uid =
        FirebaseAuth.getInstance().currentUser?.uid ?: ""

    fun saveProfile(profile: ProfileEntity) {
        viewModelScope.launch {
            repository.saveProfile(profile)
        }
    }

    fun loadProfile(onResult: (ProfileEntity?) -> Unit) {
        viewModelScope.launch {
            onResult(repository.getProfile(uid))
        }
    }
}