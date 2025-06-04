package com.example.appro2

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class HistorialAdapter(
    private val items: MutableList<Recoleccion>,
    private val onItemClick: (Recoleccion, Int) -> Unit
) : RecyclerView.Adapter<HistorialAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val fecha: TextView = view.findViewById(R.id.textFecha)
        val cantidad: TextView = view.findViewById(R.id.textCantidad)
        val peso: TextView = view.findViewById(R.id.textPeso)
        val puntos: TextView = view.findViewById(R.id.textPuntos)

        init {
            view.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onItemClick(items[position], position)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_historial, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.fecha.text = "📅 Fecha: ${item.fecha}"
        holder.cantidad.text = "📦 Cantidad: ${item.cantidadTotal}"
        holder.peso.text = "⚖️ Peso: %.2f Kg".format(item.pesoTotalKg)
        holder.puntos.text = "⭐ Puntos: %.2f".format(item.puntos)
    }

    override fun getItemCount(): Int = items.size

    fun removeAt(position: Int) {
        items.removeAt(position)
        notifyItemRemoved(position)
    }
}
