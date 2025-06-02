package com.example.appro2

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class PerfilActivity : AppCompatActivity() {

    private lateinit var txtNombre: TextView
    private lateinit var txtCorreo: TextView
    private lateinit var txtPuntos: TextView
    private lateinit var btnCerrarSesion: Button
    private lateinit var btnVerHistorial: Button
    private lateinit var fotoPerfil: ImageView

    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_perfil)

        txtNombre = findViewById(R.id.txtNombre)
        txtCorreo = findViewById(R.id.txtCorreo)
        txtPuntos = findViewById(R.id.txtPuntos)
        btnCerrarSesion = findViewById(R.id.btnCerrarSesion)
        btnVerHistorial = findViewById(R.id.btnVerHistorial)
        fotoPerfil = findViewById(R.id.fotoPerfil)

        // Evento para regresar
        val backButton = findViewById<ImageButton>(R.id.backButton)

        backButton.setOnClickListener {
            finish()
        }


        fotoPerfil.setImageResource(R.drawable.ic_user_default)
        txtPuntos.text = "Puntos acumulados: 0.00"

        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        val currentUser = auth.currentUser
        if (currentUser == null) {
            Toast.makeText(this, "Usuario no autenticado", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
            return
        }

        val userId = currentUser.uid
        val userEmail = currentUser.email ?: "Sin correo"

        // Mostrar correo de inmediato
        txtCorreo.text = userEmail

        // Obtener nombre desde Firestore
        firestore.collection("users").document(userId)
            .get()
            .addOnSuccessListener { document ->
                val nombre = document.getString("name") ?: "Sin nombre"
                txtNombre.text = nombre
            }
            .addOnFailureListener {
                Toast.makeText(this, "Error al obtener datos del usuario", Toast.LENGTH_SHORT).show()
                txtNombre.text = "Sin nombre"
            }

        btnVerHistorial.setOnClickListener {
            startActivity(Intent(this, HistorialActivity::class.java))
        }

        btnCerrarSesion.setOnClickListener {
            auth.signOut()
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }
    }
}
