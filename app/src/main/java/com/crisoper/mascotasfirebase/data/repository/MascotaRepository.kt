package com.crisoper.mascotasfirebase.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.crisoper.mascotasfirebase.data.model.Mascota
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import android.util.Log

class MascotaRepository {
    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()
    private val mascotasLiveData = MutableLiveData<List<Mascota>>()

    fun getMascotas(): LiveData<List<Mascota>> {
        val user = auth.currentUser
        if (user == null) {
            Log.d("MascotaRepository", "No authenticated user")
            mascotasLiveData.value = emptyList()
            return mascotasLiveData
        }

        db.collection("users").document(user.uid).collection("mascotas")
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    Log.e("MascotaRepository", "Error fetching mascotas", error)
                    mascotasLiveData.value = emptyList()
                    return@addSnapshotListener
                }
                val mascotas = snapshot?.documents?.mapNotNull { doc ->
                    doc.toObject(Mascota::class.java)?.apply { id = doc.id }
                } ?: emptyList()
                Log.d("MascotaRepository", "Mascotas fetched: ${mascotas.size}")
                mascotasLiveData.value = mascotas
            }
        return mascotasLiveData
    }

    fun addMascota(mascota: Mascota, callback: (Boolean, String?) -> Unit) {
        val user = auth.currentUser
        if (user == null) {
            Log.e("MascotaRepository", "No authenticated user")
            callback(false, "Usuario no autenticado")
            return
        }

        db.collection("users").document(user.uid).collection("mascotas")
            .add(mascota)
            .addOnSuccessListener {
                Log.d("MascotaRepository", "Mascota added: ${mascota.nombre}")
                callback(true, null)
            }
            .addOnFailureListener { e ->
                Log.e("MascotaRepository", "Error adding mascota", e)
                callback(false, e.message)
            }
    }

    fun updateMascota(mascota: Mascota, callback: (Boolean, String?) -> Unit) {
        val user = auth.currentUser
        if (user == null) {
            Log.e("MascotaRepository", "No authenticated user")
            callback(false, "Usuario no autenticado")
            return
        }

        db.collection("users").document(user.uid).collection("mascotas").document(mascota.id)
            .set(mascota)
            .addOnSuccessListener {
                Log.d("MascotaRepository", "Mascota updated: ${mascota.nombre}")
                callback(true, null)
            }
            .addOnFailureListener { e ->
                Log.e("MascotaRepository", "Error updating mascota", e)
                callback(false, e.message)
            }
    }

    fun deleteMascota(mascotaId: String, callback: (Boolean, String?) -> Unit) {
        val user = auth.currentUser
        if (user == null) {
            Log.e("MascotaRepository", "No authenticated user")
            callback(false, "Usuario no autenticado")
            return
        }

        db.collection("users").document(user.uid).collection("mascotas").document(mascotaId)
            .delete()
            .addOnSuccessListener {
                Log.d("MascotaRepository", "Mascota deleted: $mascotaId")
                callback(true, null)
            }
            .addOnFailureListener { e ->
                Log.e("MascotaRepository", "Error deleting mascota", e)
                callback(false, e.message)
            }
    }
}