package com.example.appro2

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CompostaAdapter(private val compostas: List<Map<String, Any>>) :
    RecyclerView.Adapter<CompostaAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val txtDescripcion: TextView = view.findViewById(R.id.txtDescripcionItem)
        val txtCantidad: TextView = view.findViewById(R.id.txtCantidadItem)
        val txtPrecio: TextView = view.findViewById(R.id.txtPrecioItem)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val vista = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_composta, parent, false)
        return ViewHolder(vista)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val composta = compostas[position]
        holder.txtDescripcion.text = "📝 ${composta["descripcion"] ?: "Sin descripción"}"
        holder.txtCantidad.text = "📦 Cantidad: ${composta["cantidadKg"] ?: "-"} kg"
        holder.txtPrecio.text = "💰 Precio: $${composta["precio"] ?: "-"}"
    }

    override fun getItemCount(): Int = compostas.size
}