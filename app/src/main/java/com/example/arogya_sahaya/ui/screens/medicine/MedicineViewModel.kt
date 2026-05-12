package com.example.arogya_sahaya.ui.screens.medicine

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.arogya_sahaya.data.local.DatabaseProvider
import com.example.arogya_sahaya.data.local.MedicineEntity
import com.example.arogya_sahaya.data.local.MedicineRepository
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

class MedicineViewModel : ViewModel() {

    private val repository =
        MedicineRepository(
            DatabaseProvider.getDatabase().medicineDao()
        )

    private val uid =
        FirebaseAuth.getInstance().currentUser?.uid ?: ""

    fun addMedicine(name: String, dosage: String, time: String) {
        viewModelScope.launch {
            repository.insertMedicine(
                MedicineEntity(
                    userId = uid,
                    name = name,
                    dosage = dosage,
                    time = time
                )
            )
        }
    }

    fun getMedicines(onResult: (List<MedicineEntity>) -> Unit) {
        viewModelScope.launch {
            onResult(repository.getMedicines(uid))
        }
    }

    fun markTaken(id: Int) {
        viewModelScope.launch {
            repository.updateMedicineStatus(id, "Taken")
        }
    }

    fun markPending(id: Int) {
        viewModelScope.launch {
            repository.updateMedicineStatus(id, "Pending")
        }
    }

    fun deleteMedicine(id: Int) {
        viewModelScope.launch {
            repository.deleteMedicine(id)
        }
    }
}