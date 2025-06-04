package com.example.appro2

import android.app.AlertDialog
import android.os.Bundle
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class HistorialActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private val db = FirebaseFirestore.getInstance()
    private val historialList = mutableListOf<Recoleccion>()
    private lateinit var adapter: HistorialAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_historial)

        recyclerView = findViewById(R.id.recyclerHistorial)
        recyclerView.layoutManager = LinearLayoutManager(this)

        adapter = HistorialAdapter(historialList) { recoleccion, position ->
            AlertDialog.Builder(this)
                .setTitle("Eliminar registro")
                .setMessage("¿Deseas eliminar este registro?")
                .setPositiveButton("Sí") { _, _ ->
                    db.collection("recolecciones")
                        .document(recoleccion.id)
                        .delete()
                        .addOnSuccessListener {
                            Toast.makeText(this, "Eliminado correctamente", Toast.LENGTH_SHORT).show()
                            adapter.removeAt(position)
                        }
                        .addOnFailureListener {
                            Toast.makeText(this, "Error al eliminar", Toast.LENGTH_SHORT).show()
                        }
                }
                .setNegativeButton("No", null)
                .show()
        }

        recyclerView.adapter = adapter

        findViewById<ImageButton>(R.id.backButton).setOnClickListener {
            finish()
        }

        cargarDatos()
    }

    private fun cargarDatos() {
        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return
        db.collection("recolecciones")
            .whereEqualTo("userId", uid)
            .get()
            .addOnSuccessListener { result ->
                historialList.clear()
                for (doc in result) {
                    val item = doc.toObject(Recoleccion::class.java).copy(id = doc.id)
                    historialList.add(item)
                }
                adapter.notifyDataSetChanged()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Error al cargar historial", Toast.LENGTH_SHORT).show()
            }
    }
}
