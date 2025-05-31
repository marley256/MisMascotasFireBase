package com.crisoper.mascotasfirebase.ui.mascota

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.crisoper.mascotasfirebase.R
import com.crisoper.mascotasfirebase.data.model.Mascota
import com.crisoper.mascotasfirebase.data.repository.AuthRepository
import com.crisoper.mascotasfirebase.ui.auth.LoginActivity
import com.crisoper.mascotasfirebase.viewmodel.MascotaViewModel
import com.crisoper.mascotasfirebase.adapter.MascotaAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MascotaListActivity : AppCompatActivity() {
    private lateinit var viewModel: MascotaViewModel
    private lateinit var adapter: MascotaAdapter
    private lateinit var authRepository: AuthRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mascota_list)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = getString(R.string.app_name)

        authRepository = AuthRepository()
        if (authRepository.getCurrentUser() == null) {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
            return
        }

        viewModel = ViewModelProvider(this).get(MascotaViewModel::class.java)
        val recyclerView = findViewById<RecyclerView>(R.id.mascotaRecyclerView)
        val emptyTextView = findViewById<android.widget.TextView>(R.id.emptyTextView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        viewModel.mascotas.observe(this) { mascotas ->
            adapter = MascotaAdapter(mascotas ?: emptyList(), object : MascotaAdapter.OnMascotaClickListener {
                override fun onEditClick(mascota: Mascota) {
                    val intent = Intent(this@MascotaListActivity, MascotaAddEditActivity::class.java).apply {
                        putExtra("MASCOTA", mascota)
                    }
                    startActivity(intent)
                }

                override fun onDeleteClick(mascota: Mascota) {
                    viewModel.deleteMascota(mascota.id)
                }
            })
            recyclerView.adapter = adapter
            emptyTextView.visibility = if (mascotas.isNullOrEmpty()) View.VISIBLE else View.GONE
        }

        findViewById<FloatingActionButton>(R.id.addMascotaButton).setOnClickListener {
            showAddMascotaDialog()
        }
    }

    private fun showAddMascotaDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_add_mascota, null)
        val nombreEditText = dialogView.findViewById<EditText>(R.id.nombreEditText)
        val historiaEditText = dialogView.findViewById<EditText>(R.id.historiaEditText)
        val edadEditText = dialogView.findViewById<EditText>(R.id.edadEditText)
        val fotoUrlEditText = dialogView.findViewById<EditText>(R.id.fotoUrlEditText)
        val mascotaImageView = dialogView.findViewById<ImageView>(R.id.mascotaImageView)
        val saveButton = dialogView.findViewById<Button>(R.id.saveButton)
        val cancelButton = dialogView.findViewById<Button>(R.id.cancelButton)

        val dialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .setTitle("Nueva Mascota")
            .create()

        saveButton.setOnClickListener {
            val nombre = nombreEditText.text.toString().trim()
            val historia = historiaEditText.text.toString().trim()
            val edadStr = edadEditText.text.toString().trim()
            val fotoUrl = fotoUrlEditText.text.toString().trim()

            if (nombre.isNotEmpty() && historia.isNotEmpty() && edadStr.isNotEmpty()) {
                try {
                    val edad = edadStr.toInt()
                    val newMascota = Mascota("", nombre, historia, edad, fotoUrl)

                    viewModel.addMascota(newMascota) { success, error ->
                        if (success) {
                            Toast.makeText(this, "Mascota guardada", Toast.LENGTH_SHORT).show()
                            dialog.dismiss()
                        } else {
                            Toast.makeText(this, "Error: $error", Toast.LENGTH_SHORT).show()
                        }
                    }
                } catch (e: NumberFormatException) {
                    Toast.makeText(this, "La edad debe ser un número", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Complete todos los campos", Toast.LENGTH_SHORT).show()
            }
        }

        cancelButton.setOnClickListener {
            dialog.dismiss()
        }

        // Optional: Load image preview if URL is provided
        fotoUrlEditText.addTextChangedListener(object : android.text.TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: android.text.Editable?) {
                if (!s.isNullOrEmpty()) {
                    Glide.with(this@MascotaListActivity).load(s.toString()).into(mascotaImageView)
                } else {
                    mascotaImageView.setImageDrawable(null)
                }
            }
        })

        dialog.show()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_logout) {
            authRepository.logout()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}