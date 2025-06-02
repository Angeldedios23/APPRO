package com.example.appro2

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class PerfilActivity : AppCompatActivity() {

    private lateinit var txtNombre: TextView
    private lateinit var txtCorreo: TextView
    private lateinit var txtPuntos: TextView
    private lateinit var btnCerrarSesion: Button
    private lateinit var btnVerHistorial: Button
    private lateinit var fotoPerfil: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_perfil)

        txtNombre = findViewById(R.id.txtNombre)
        txtCorreo = findViewById(R.id.txtCorreo)
        txtPuntos = findViewById(R.id.txtPuntos)
        btnCerrarSesion = findViewById(R.id.btnCerrarSesion)
        btnVerHistorial = findViewById(R.id.btnVerHistorial)
        fotoPerfil = findViewById(R.id.fotoPerfil)

        // Simulación de datos del usuario
        val nombre = "Axel Rivas"
        val correo = "axel@gmail.com"
        val puntos = 26.75
        val fotoURL = "C:\\Users\\angelica nohemi\\Desktop" // ejemplo externo

        txtNombre.text = nombre
        txtCorreo.text = correo
        txtPuntos.text = "Puntos acumulados: %.2f".format(puntos)

        // En caso de usar Glide para cargar imagen
        fotoPerfil.setImageResource(R.drawable.ic_user_default)


        btnVerHistorial.setOnClickListener {
            startActivity(Intent(this, HistorialActivity::class.java))
        }

        btnCerrarSesion.setOnClickListener {
            // Para finalizar la sesion
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }
}