package com.crisoper.mascotasfirebase.ui.mascota

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.crisoper.mascotasfirebase.R
import com.crisoper.mascotasfirebase.data.model.Mascota
import com.crisoper.mascotasfirebase.viewmodel.MascotaViewModel

class MascotaAddEditActivity : AppCompatActivity() {
    private lateinit var viewModel: MascotaViewModel
    private lateinit var nombreEditText: EditText
    private lateinit var historiaEditText: EditText
    private lateinit var edadEditText: EditText
    private lateinit var fotoUrlEditText: EditText
    private lateinit var mascotaImageView: ImageView
    private var mascota: Mascota? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mascota_add_edit)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        mascota = intent.getSerializableExtra("MASCOTA") as? Mascota
        supportActionBar?.title = if (mascota == null) "Nueva Mascota" else "Editar Mascota"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        viewModel = ViewModelProvider(this).get(MascotaViewModel::class.java)
        nombreEditText = findViewById(R.id.nombreEditText)
        historiaEditText = findViewById(R.id.historiaEditText)
        edadEditText = findViewById(R.id.edadEditText)
        fotoUrlEditText = findViewById(R.id.fotoUrlEditText)
        mascotaImageView = findViewById(R.id.mascotaImageView)

        mascota?.let {
            nombreEditText.setText(it.nombre)
            historiaEditText.setText(it.historia)
            edadEditText.setText(it.edad.toString())
            fotoUrlEditText.setText(it.fotoUrl)
            if (!it.fotoUrl.isNullOrEmpty()) {
                Glide.with(this).load(it.fotoUrl).into(mascotaImageView)
            }
        }

        findViewById<Button>(R.id.saveButton).setOnClickListener {
            val nombre = nombreEditText.text.toString().trim()
            val historia = historiaEditText.text.toString().trim()
            val edadStr = edadEditText.text.toString().trim()
            val fotoUrl = fotoUrlEditText.text.toString().trim()

            if (nombre.isNotEmpty() && historia.isNotEmpty() && edadStr.isNotEmpty()) {
                try {
                    val edad = edadStr.toInt()
                    val newMascota = mascota?.copy(
                        nombre = nombre,
                        historia = historia,
                        edad = edad,
                        fotoUrl = fotoUrl
                    ) ?: Mascota("", nombre, historia, edad, fotoUrl)

                    if (mascota == null) {
                        viewModel.addMascota(newMascota) { success, error ->
                            if (success) {
                                Toast.makeText(this, "Mascota guardada", Toast.LENGTH_SHORT).show()
                                finish()
                            } else {
                                Toast.makeText(this, "Error: $error", Toast.LENGTH_SHORT).show()
                            }
                        }
                    } else {
                        viewModel.updateMascota(newMascota) { success, error ->
                            if (success) {
                                Toast.makeText(this, "Mascota actualizada", Toast.LENGTH_SHORT).show()
                                finish()
                            } else {
                                Toast.makeText(this, "Error: $error", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                } catch (e: NumberFormatException) {
                    Toast.makeText(this, "La edad debe ser un número", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Complete todos los campos", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}