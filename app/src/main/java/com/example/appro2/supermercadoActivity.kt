package com.example.appro2

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class SupermercadoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_supermercado)

        val descripcion = findViewById<EditText>(R.id.txtDescripcion)
        val horario = findViewById<EditText>(R.id.txtHorario)
        val btnPublicar = findViewById<Button>(R.id.btnPublicar)
        val backButton = findViewById<ImageButton>(R.id.backButton)

        btnPublicar.setOnClickListener {
            val mensaje = "Residuos publicados:\n" +
                    "${descripcion.text} - Disponible: ${horario.text}"
            Toast.makeText(this, mensaje, Toast.LENGTH_LONG).show()
        }

        backButton.setOnClickListener {
            finish() // Cierra esta actividad y vuelve a la anterior
        }
    }
}
