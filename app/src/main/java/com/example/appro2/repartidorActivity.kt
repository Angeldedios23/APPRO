package com.example.appro2

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageButton
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class RepartidorActivity : AppCompatActivity() {

    private lateinit var listaDirecciones: ListView
    private lateinit var btnFinalizarRuta: Button
    private lateinit var backButton: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_repartidor)

        listaDirecciones = findViewById(R.id.listaDirecciones)
        btnFinalizarRuta = findViewById(R.id.btnFinalizarRuta)
        backButton = findViewById(R.id.backButton)

        val direcciones = listOf("Calle 123, Zona A", "Av. Central 45", "Mercado San Juan")
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, direcciones)
        listaDirecciones.adapter = adapter

        btnFinalizarRuta.setOnClickListener {
            Toast.makeText(this, "Ruta finalizada. ¡Gracias por tu trabajo!", Toast.LENGTH_SHORT).show()
        }

        backButton.setOnClickListener {
            finish() // Cierra la actividad actual y regresa a la anterior
        }
    }
}
