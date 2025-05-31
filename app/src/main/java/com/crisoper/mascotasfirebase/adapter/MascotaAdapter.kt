package com.crisoper.mascotasfirebase.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.crisoper.mascotasfirebase.R
import com.crisoper.mascotasfirebase.data.model.Mascota

class MascotaAdapter(
    private val mascotas: List<Mascota>,
    private val listener: OnMascotaClickListener
) : RecyclerView.Adapter<MascotaAdapter.MascotaViewHolder>() {

    interface OnMascotaClickListener {
        fun onEditClick(mascota: Mascota)
        fun onDeleteClick(mascota: Mascota)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MascotaViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_mascota, parent, false)
        return MascotaViewHolder(view)
    }

    override fun onBindViewHolder(holder: MascotaViewHolder, position: Int) {
        val mascota = mascotas[position]
        holder.nombreTextView.text = mascota.nombre
        holder.historiaTextView.text = mascota.historia
        holder.edadTextView.text = mascota.edad.toString()
        if (!mascota.fotoUrl.isNullOrEmpty()) {
            Glide.with(holder.itemView.context).load(mascota.fotoUrl).into(holder.mascotaImageView)
        }
        holder.editButton.setOnClickListener { listener.onEditClick(mascota) }
        holder.deleteButton.setOnClickListener { listener.onDeleteClick(mascota) }
    }

    override fun getItemCount(): Int = mascotas.size

    class MascotaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val mascotaImageView: ImageView = itemView.findViewById(R.id.mascotaImageView)
        val nombreTextView: TextView = itemView.findViewById(R.id.nombreTextView)
        val historiaTextView: TextView = itemView.findViewById(R.id.historiaTextView)
        val edadTextView: TextView = itemView.findViewById(R.id.edadTextView)
        val editButton: Button = itemView.findViewById(R.id.editButton)
        val deleteButton: Button = itemView.findViewById(R.id.deleteButton)
    }
}