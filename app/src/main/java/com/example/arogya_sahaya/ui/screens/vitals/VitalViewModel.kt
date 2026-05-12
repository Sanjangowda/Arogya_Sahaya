package com.example.arogya_sahaya.ui.screens.vitals

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.arogya_sahaya.data.local.DatabaseProvider
import com.example.arogya_sahaya.data.local.VitalEntity
import com.example.arogya_sahaya.data.local.VitalRepository
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

class VitalViewModel : ViewModel() {

    private val repository =
        VitalRepository(
            DatabaseProvider.getDatabase().vitalDao()
        )

    private val uid =
        FirebaseAuth.getInstance().currentUser?.uid ?: ""

    fun addVital(
        bloodPressure: String,
        bloodSugar: String,
        heartRate: String,
        date: String
    ) {
        viewModelScope.launch {
            repository.insertVital(
                VitalEntity(
                    userId = uid,
                    bloodPressure = bloodPressure,
                    bloodSugar = bloodSugar,
                    heartRate = heartRate,
                    date = date
                )
            )
        }
    }

    fun getVitals(onResult: (List<VitalEntity>) -> Unit) {
        viewModelScope.launch {
            onResult(repository.getVitals(uid))
        }
    }
}