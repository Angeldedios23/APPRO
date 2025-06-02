package com.example.appro2

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class RegisterActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        val nameEdit = findViewById<EditText>(R.id.nameEdit)
        val emailEdit = findViewById<EditText>(R.id.emailEdit)
        val passwordEdit = findViewById<EditText>(R.id.passwordEdit)
        val registerButton = findViewById<Button>(R.id.registerButton)
        val goToLoginText = findViewById<TextView>(R.id.goToLoginText)

        registerButton.setOnClickListener {
            val name = nameEdit.text.toString().trim()
            val email = emailEdit.text.toString().trim()
            val password = passwordEdit.text.toString().trim()

            if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
                showAlert("Campos incompletos", "Completa todos los campos")
                return@setOnClickListener
            }

            if (password.length < 6) {
                showAlert("Contraseña inválida", "La contraseña debe tener al menos 6 caracteres")
                return@setOnClickListener
            }

            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val userId = auth.currentUser?.uid
                        val user = hashMapOf(
                            "name" to name,
                            "email" to email
                        )

                        if (userId != null) {
                            firestore.collection("users").document(userId)
                                .set(user)
                                .addOnSuccessListener {
                                    showAlert("Registro exitoso", "Usuario registrado correctamente") {
                                        val intent = Intent(this, LoginActivity::class.java)
                                        intent.putExtra("registered_email", email)
                                        startActivity(intent)
                                        finish()
                                    }
                                }
                                .addOnFailureListener {
                                    showAlert("Error", "Error al guardar los datos en Firestore")
                                }
                        }
                    } else {
                        showAlert("Error", "No se pudo registrar: ${task.exception?.message}")
                    }
                }
        }

        goToLoginText.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }

    private fun showAlert(title: String, message: String, onOk: (() -> Unit)? = null) {
        AlertDialog.Builder(this)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
                onOk?.invoke()
            }
            .show()
    }
}
