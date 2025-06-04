package com.example.appro2

import android.os.Bundle
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class ComprarCompostaActivity : AppCompatActivity() {

    private lateinit var lista: RecyclerView
    private val db = FirebaseFirestore.getInstance()
    private val compostas = mutableListOf<Map<String, Any>>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comprar_composta)

        lista = findViewById(R.id.recyclerCompostas)
        lista.layoutManager = LinearLayoutManager(this)
        lista.adapter = CompostaAdapter(compostas)



        db.collection("ventas_composta")
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { result ->
                for (doc in result) {
                    compostas.add(doc.data)
                }
                lista.adapter?.notifyDataSetChanged()
            }

        findViewById<ImageButton>(R.id.backButton).setOnClickListener {
            finish() // Regresa a la pantalla anterior
        }
    }
}