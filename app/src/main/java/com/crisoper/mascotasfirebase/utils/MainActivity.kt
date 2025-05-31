package com.crisoper.mascotasfirebase.utils

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.crisoper.mascotasfirebase.data.repository.AuthRepository
import com.crisoper.mascotasfirebase.ui.auth.LoginActivity
import com.crisoper.mascotasfirebase.ui.mascota.MascotaListActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val authRepository = AuthRepository()
        val intent = if (authRepository.getCurrentUser() != null) {
            Intent(this, MascotaListActivity::class.java)
        } else {
            Intent(this, LoginActivity::class.java)
        }
        startActivity(intent)
        finish()
    }
}