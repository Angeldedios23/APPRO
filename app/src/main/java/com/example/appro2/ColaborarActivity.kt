package com.example.appro2

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity

class RolActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_colaborar)

        val btnRepartidor = findViewById<Button>(R.id.btnRepartidor)
        val btnAgricultor = findViewById<Button>(R.id.btnAgricultor)
        val btnSupermercado = findViewById<Button>(R.id.btnSupermercado)
        val backButton = findViewById<ImageButton>(R.id.backButton)

        btnRepartidor.setOnClickListener {
            startActivity(Intent(this, RepartidorActivity::class.java))
        }

        btnAgricultor.setOnClickListener {
            startActivity(Intent(this, AgricultorActivity::class.java))
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
