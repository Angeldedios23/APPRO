package com.example.appro2

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageButton
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class GranjeroActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_granjero)

        val lista = findViewById<ListView>(R.id.listaResiduosAnimales)
        val btnRecolectar = findViewById<Button>(R.id.btnRecolectar)
        val backButton = findViewById<ImageButton>(R.id.backButton)

        val residuos = listOf("Pan viejo (2kg)", "Frutas maduras (3kg)", "Verduras crudas (1.5kg)")

        lista.adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, residuos)

        btnRecolectar.setOnClickListener {
            Toast.makeText(this, "Recolección marcada como realizada", Toast.LENGTH_SHORT).show()
        }

        // Acción para la flecha de regreso
        backButton.setOnClickListener {
            finish() // Cierra esta pantalla y regresa a la anterior
        }
    }
}
