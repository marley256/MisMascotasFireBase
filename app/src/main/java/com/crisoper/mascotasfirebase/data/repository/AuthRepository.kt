package com.crisoper.mascotasfirebase.data.repository

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class AuthRepository {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    fun registerUser(email: String, password: String, callback: (Boolean, String?) -> Unit) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("AuthRepository", "User registered: ${auth.currentUser?.uid}")
                    callback(true, null)
                } else {
                    Log.e("AuthRepository", "Registration error", task.exception)
                    callback(false, task.exception?.message)
                }
            }
    }

    fun loginUser(email: String, password: String, callback: (Boolean, String?) -> Unit) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("AuthRepository", "User logged in: ${auth.currentUser?.uid}")
                    callback(true, null)
                } else {
                    Log.e("AuthRepository", "Login error", task.exception)
                    callback(false, task.exception?.message)
                }
            }
    }

    fun logout() {
        auth.signOut()
        Log.d("AuthRepository", "User logged out")
    }

    fun getCurrentUser(): FirebaseUser? = auth.currentUser
}