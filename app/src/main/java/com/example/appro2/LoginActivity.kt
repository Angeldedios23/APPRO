package com.example.appro2

import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        val emailEditText = findViewById<EditText>(R.id.emailEditText)
        val passwordEditText = findViewById<EditText>(R.id.passwordEditText)
        val loginButton = findViewById<Button>(R.id.loginButton)
        val goToRegisterText = findViewById<TextView>(R.id.goToRegisterText)

        // Mostrar el correo recibido desde el registro si aplica
        val passedEmail = intent.getStringExtra("registered_email")
        if (!passedEmail.isNullOrEmpty()) {
            emailEditText.setText(passedEmail)
        }

        loginButton.setOnClickListener {
            val email = emailEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                showCenteredToast("Completa todos los campos")
                return@setOnClickListener
            }

            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val userId = auth.currentUser?.uid
                        if (userId != null) {
                            firestore.collection("users").document(userId)
                                .get()
                                .addOnSuccessListener { document ->
                                    val name = document.getString("name") ?: "Usuario"

                                    // Guardar el tiempo actual como hora de inicio de sesión
                                    val prefs = getSharedPreferences("session", MODE_PRIVATE)
                                    prefs.edit().putLong("lastLoginTime", System.currentTimeMillis()).apply()

                                    showCenteredToast("¡Bienvenido $name!")

                                    val intent = Intent(this, MainActivity::class.java)
                                    intent.putExtra("user_name", name)
                                    intent.putExtra("user_email", email)
                                    startActivity(intent)
                                    finish()
                                }
                                .addOnFailureListener {
                                    showCenteredToast("No se pudo obtener el perfil")
                                }
                        }
                    } else {
                        showCenteredToast("Correo o contraseña incorrectos")
                    }
                }
        }

        goToRegisterText.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
            finish()
        }
    }

    private fun showCenteredToast(message: String) {
        val toast = Toast.makeText(this, message, Toast.LENGTH_LONG)
        toast.setGravity(Gravity.CENTER, 0, 0)
        toast.show()
    }
}
