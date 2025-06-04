package com.example.appro2

import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class SupermercadoActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: TiendaAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_supermercado)

        recyclerView = findViewById(R.id.recyclerTiendas)
        recyclerView.layoutManager = LinearLayoutManager(this)

        /* Regresar */
        val backButton = findViewById<ImageButton>(R.id.backButton)
        backButton.setOnClickListener { finish() }

        val listaTiendas = listOf(
            Tienda("Supercito Chedraui Tecnológico", "Calz. del Tecnológico 202, Moctezuma, 91096 Col. Santa Bárbara, Ver.", "Lunes a Viernes 7:00 – 18:00"),
            Tienda("WALDOS", "REVOLUCION, 130 132, MZ Y 134, Zona Centro, 91000 Xalapa-Enríquez, Ver.", "Todos los días 10:00 – 20:00"),
            Tienda("Chedraui - Crystal", "Calle Lázaro Cárdenas Col. El Encinal Centro Comercial Plaza Cristal, 91150 Xalapa-Enríquez, Ver.", "Lunes a Sábado 8:00 – 17:00"),
            Tienda("La Granja de Xalapa", "Av. Pípila 13, Zona Centro, Centro, 91000 Xalapa-Enríquez, Ver.", "Todos los días 9:00 – 21:00"),
            Tienda("La Granja de Xalapa", "Av. Pípila 13, Zona Centro, Centro, 91000 Xalapa-Enríquez, Ver.", "Todos los días 9:00 – 21:00"),
            Tienda("Tienda Hecho en Veracruz", "Xalapeños Ilustres 107, Zona Centro, Centro, 91000 Xalapa-Enríquez, Ver.", "Todos los días 10:00 – 22:00"),
            Tienda("Casa Verde", "Pico de Orizaba 17, Sipeh Animas, 91067 Xalapa-Enríquez, Ver.", "Lunes a Viernes 10:00 – 20:00"),
        )

        adapter = TiendaAdapter(listaTiendas)
        recyclerView.adapter = adapter
    }
}

