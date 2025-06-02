package com.example.appro2

import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class AgricultorActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_agricultor)

        // Botón para comprar composta
        findViewById<Button>(R.id.btnComprar).setOnClickListener {
            Toast.makeText(this, "Redirigiendo a catálogo de composta...", Toast.LENGTH_SHORT).show()
        }

        // Botón para vender composta
        findViewById<Button>(R.id.btnVender).setOnClickListener {
            Toast.makeText(this, "Formulario para ofrecer composta", Toast.LENGTH_SHORT).show()
        }

        // Botón de retroceso
        val backButton = findViewById<ImageButton>(R.id.backButton)
        backButton.setOnClickListener {
            finish() // Cierra esta pantalla y regresa a la anterior
        }
    }
}
