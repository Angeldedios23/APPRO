package com.example.appro2

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TiendaAdapter(private val tiendas: List<Tienda>) :
    RecyclerView.Adapter<TiendaAdapter.TiendaViewHolder>() {

    class TiendaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nombre: TextView = itemView.findViewById(R.id.txtNombreTienda)
        val ubicacion: TextView = itemView.findViewById(R.id.txtUbicacionTienda)
        val horario: TextView = itemView.findViewById(R.id.txtHorarioTienda)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TiendaViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_tienda, parent, false)
        return TiendaViewHolder(view)
    }

    override fun onBindViewHolder(holder: TiendaViewHolder, position: Int) {
        val tienda = tiendas[position]
        holder.nombre.text = tienda.nombre
        holder.ubicacion.text = tienda.ubicacion
        holder.horario.text = tienda.horario
    }

    override fun getItemCount(): Int = tiendas.size
}
