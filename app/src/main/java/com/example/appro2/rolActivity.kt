package com.example.appro2

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity

class RolActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rol)

        val btnRepartidor = findViewById<Button>(R.id.btnRepartidor)
        val btnAgricultor = findViewById<Button>(R.id.btnAgricultor)
        val btnGranjero = findViewById<Button>(R.id.btnGranjero)
        val btnSupermercado = findViewById<Button>(R.id.btnSupermercado)
        val backButton = findViewById<ImageButton>(R.id.backButton)

        btnRepartidor.setOnClickListener {
            startActivity(Intent(this, RepartidorActivity::class.java))
        }

        btnAgricultor.setOnClickListener {
            startActivity(Intent(this, AgricultorActivity::class.java))
        }

        btnGranjero.setOnClickListener {
            startActivity(Intent(this, GranjeroActivity::class.java))
        }

        btnSupermercado.setOnClickListener {
            startActivity(Intent(this, SupermercadoActivity::class.java))
        }

        // Acción del botón de retroceso
        backButton.setOnClickListener {
            finish() // Cierra la actividad actual y vuelve a la anterior
        }
    }
}
