package com.crisoper.mascotasfirebase.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.crisoper.mascotasfirebase.data.model.Mascota
import com.crisoper.mascotasfirebase.data.repository.MascotaRepository 


class MascotaViewModel : ViewModel() {
    private val repository = MascotaRepository()
    val mascotas: LiveData<List<Mascota>> = repository.getMascotas()

    fun addMascota(mascota: Mascota, callback: (Boolean, String?) -> Unit) {
        repository.addMascota(mascota, callback)
    }

    fun updateMascota(mascota: Mascota, callback: (Boolean, String?) -> Unit) {
        repository.updateMascota(mascota, callback)
    }

    fun deleteMascota(mascotaId: String) {
        repository.deleteMascota(mascotaId) { _, _ -> }
    }
}
