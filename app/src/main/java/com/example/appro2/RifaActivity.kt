package com.example.appro2

import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class RifaActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: PremioAdapter
    private val listaPremios = listOf(
        Premio("Lavadora ecológica", R.drawable.lavadora, 6600),
        Premio("Laptop gamer", R.drawable.laptop, 15000),
        Premio("Silla gamer", R.drawable.silla, 3000),
        Premio("iPhone", R.drawable.iphone, 19000)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rifa)

        recyclerView = findViewById(R.id.recyclerPremios)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = PremioAdapter(listaPremios)
        recyclerView.adapter = adapter

        /* Regresar */
        val backButton = findViewById<ImageButton>(R.id.backButton)
        backButton.setOnClickListener { finish() }
    }
}

data class Premio(
    val nombre: String,
    val imagenResId: Int,
    val valor: Int
)
