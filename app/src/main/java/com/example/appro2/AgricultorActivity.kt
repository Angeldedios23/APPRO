package com.example.appro2

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class AgricultorActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_agricultor)

        findViewById<Button>(R.id.btnComprar).setOnClickListener {
            startActivity(Intent(this, ComprarCompostaActivity::class.java))
        }

        findViewById<Button>(R.id.btnVender).setOnClickListener {
            startActivity(Intent(this, VenderCompostaActivity::class.java))
        }

        findViewById<ImageButton>(R.id.backButton).setOnClickListener {
            finish() // Regresa a la actividad anterior
        }
    }
}
