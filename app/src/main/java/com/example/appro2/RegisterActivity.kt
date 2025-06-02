package com.example.appro2

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class RegisterActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    private lateinit var fotoPerfil: ImageView
    private lateinit var selectImageButton: Button
    private var selectedImageUri: Uri? = null

    private val selectImageLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            selectedImageUri = result.data?.data
            fotoPerfil.setImageURI(selectedImageUri)
        }
    }

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
        val backButton = findViewById<ImageButton>(R.id.backButton)
        fotoPerfil = findViewById(R.id.fotoPerfil)
        selectImageButton = findViewById(R.id.selectImageButton)

        selectImageButton.setOnClickListener {
            solicitarPermisoGaleria()
        }

        backButton.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        registerButton.setOnClickListener {
            val name = nameEdit.text.toString().trim()
            val email = emailEdit.text.toString().trim()
            val password = passwordEdit.text.toString().trim()

            if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
                showAlert("Campos incompletos", "Completa todos los campos")
                return@setOnClickListener
            }

            if (password.length < 6) {
                showAlert("Contraseña inválida", "Debe tener al menos 6 caracteres")
                return@setOnClickListener
            }

            val imageUrlLocal = selectedImageUri?.toString()

            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val userId = auth.currentUser?.uid ?: return@addOnCompleteListener

                        // Guardar hora del registro como inicio de sesión
                        val prefs = getSharedPreferences("session", MODE_PRIVATE)
                        prefs.edit().putLong("lastLoginTime", System.currentTimeMillis()).apply()

                        saveUserToFirestore(userId, name, email, imageUrlLocal)
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

    private fun solicitarPermisoGaleria() {
        val permiso = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            android.Manifest.permission.READ_MEDIA_IMAGES
        } else {
            android.Manifest.permission.READ_EXTERNAL_STORAGE
        }

        requestPermissions(arrayOf(permiso), 1001)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == 1001 && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            val intent = Intent(Intent.ACTION_PICK).apply {
                type = "image/*"
            }
            selectImageLauncher.launch(intent)
        } else {
            Toast.makeText(this, "Permiso denegado para acceder a imágenes", Toast.LENGTH_SHORT).show()
        }
    }

    private fun saveUserToFirestore(userId: String, name: String, email: String, profileUrl: String?) {
        val user = hashMapOf(
            "name" to name,
            "email" to email
        )

        // Solo guardar profileUrl si hay imagen seleccionada
        if (!profileUrl.isNullOrEmpty()) {
            user["profileUrl"] = profileUrl
        }

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
