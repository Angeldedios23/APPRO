package com.example.appro2

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class VenderCompostaActivity : AppCompatActivity() {

    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vender_composta)

        val btnGuardar = findViewById<Button>(R.id.btnGuardarVenta)
        btnGuardar.setOnClickListener {
            val descripcion = findViewById<EditText>(R.id.txtDescripcion).text.toString()
            val cantidad = findViewById<EditText>(R.id.txtCantidad).text.toString()
            val precio = findViewById<EditText>(R.id.txtPrecio).text.toString()

            val userId = auth.currentUser?.uid ?: return@setOnClickListener
            val venta = hashMapOf(
                "descripcion" to descripcion,
                "cantidadKg" to cantidad.toDoubleOrNull(),
                "precio" to precio.toDoubleOrNull(),
                "userId" to userId,
                "timestamp" to System.currentTimeMillis()
            )

            db.collection("ventas_composta").add(venta)
                .addOnSuccessListener {
                    Toast.makeText(this, "Venta publicada", Toast.LENGTH_SHORT).show()
                    finish()
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Error al publicar", Toast.LENGTH_SHORT).show()
                }
        }
        findViewById<ImageButton>(R.id.backButton).setOnClickListener {
            finish() // Cierra la actividad y regresa
        }
    }
}