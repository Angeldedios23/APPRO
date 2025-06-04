package com.example.appro2

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class PremioAdapter(private val premios: List<Premio>) :
    RecyclerView.Adapter<PremioAdapter.PremioViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PremioViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_premio, parent, false)
        return PremioViewHolder(view)
    }

    override fun onBindViewHolder(holder: PremioViewHolder, position: Int) {
        val premio = premios[position]
        holder.nombre.text = premio.nombre
        holder.valor.text = "Valor: $ ${premio.valor} pesos mexicanos"
        holder.imagen.setImageResource(premio.imagenResId)
    }

    override fun getItemCount(): Int = premios.size

    class PremioViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imagen: ImageView = view.findViewById(R.id.imgPremio)
        val nombre: TextView = view.findViewById(R.id.txtNombrePremio)
        val valor: TextView = view.findViewById(R.id.txtValorPremio)
    }
}
