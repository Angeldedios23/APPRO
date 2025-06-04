package com.example.appro2

import android.content.Intent
import android.os.Bundle
import android.util.Log
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

        // Inicializamos Firebase
        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        // Validar tiempo de sesión
        val prefs = getSharedPreferences("session", MODE_PRIVATE)
        val lastLoginTime = prefs.getLong("lastLoginTime", 0L)
        val currentTime = System.currentTimeMillis()
        val currentUser = auth.currentUser

        if (currentUser == null || lastLoginTime == 0L || currentTime - lastLoginTime > 10 * 60 * 1000) {
            cerrarSesion("Sesión expirada. Vuelve a iniciar sesión")
            return
        }

        setContentView(R.layout.activity_perfil)

        // Inicializar vistas
        txtNombre = findViewById(R.id.txtNombre)
        txtCorreo = findViewById(R.id.txtCorreo)
        txtPuntos = findViewById(R.id.txtPuntos)
        btnCerrarSesion = findViewById(R.id.btnCerrarSesion)
        fotoPerfil = findViewById(R.id.fotoPerfil)

        fotoPerfil.setImageResource(R.drawable.profile_placeholder)

        /* Regresar */
        val backButton = findViewById<ImageButton>(R.id.backButton)
        backButton.setOnClickListener { finish() }

        txtPuntos.text = "Puntos acumulados: 0.00"
        txtCorreo.text = currentUser.email ?: "Sin correo"

        // Obtener nombre y puntos por recompensa desde documento de usuario
        firestore.collection("usuarios").document(currentUser.uid)
            .get()
            .addOnSuccessListener { document ->
                val nombre = document.getString("name") ?: "Sin nombre"
                txtNombre.text = nombre

                val puntosTotales = document.getDouble("puntos") ?: 0.0
                txtPuntos.text = "Puntos acumulados: %.2f".format(puntosTotales)
                Log.d("PerfilActivity", "Puntos totales desde usuarios: $puntosTotales")
            }
            .addOnFailureListener {
                txtNombre.text = "Sin nombre"
                txtPuntos.text = "Error al cargar puntos"
            }

            .addOnFailureListener {
                txtNombre.text = "Sin nombre"
                txtPuntos.text = "Error al cargar datos"
            }

        btnCerrarSesion.setOnClickListener {
            cerrarSesion("Sesión cerrada correctamente")
        }
    }

    private fun cerrarSesion(mensaje: String) {
        auth.signOut()
        getSharedPreferences("session", MODE_PRIVATE).edit().clear().apply()
        Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show()
        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
}
