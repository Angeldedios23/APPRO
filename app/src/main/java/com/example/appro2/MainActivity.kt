package com.example.appro2

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.chip.Chip
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.FirebaseApp

class MainActivity : AppCompatActivity() {

    private var lastInteractionTime: Long = 0
    private val logoutTimeout: Long = 10 * 60 * 1000 // 10 minutos

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)
        setContentView(R.layout.activity_main) // ✅ Esto debe ir primero

        val chipHistorial = findViewById<Chip>(R.id.chipHistorial)
        chipHistorial.setOnClickListener {
            val intent = Intent(this, HistorialActivity::class.java)
            startActivity(intent)
        }

        // 🔹 Insertar el MapaFragment dentro de map_container
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        val mapaFragment = MapaFragment()
        fragmentTransaction.replace(R.id.map_container, mapaFragment)
        fragmentTransaction.commit()

        val searchView = findViewById<SearchView>(R.id.search_view)
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        val btnColaborar = findViewById<Button>(R.id.btnColaborar)
        val btnRecoleccion = findViewById<Button>(R.id.btnRecoleccion)

        btnRecoleccion.setOnClickListener {
            startActivity(Intent(this, RecoleccionActivity::class.java))
        }

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                Toast.makeText(this@MainActivity, "Buscando: $query", Toast.LENGTH_SHORT).show()
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }
        })

        btnColaborar.setOnClickListener {
            startActivity(Intent(this, RolActivity::class.java))
        }

        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    Toast.makeText(this, "Estás en Inicio", Toast.LENGTH_SHORT).show()
                }
                R.id.nav_rewards -> {
                    startActivity(Intent(this, HistorialActivity::class.java))
                }
                R.id.nav_profile -> {
                    startActivity(Intent(this, PerfilActivity::class.java))
                }
            }
            true
        }
    }

    override fun onStart() {
        super.onStart()
        val user = FirebaseAuth.getInstance().currentUser
        if (user == null) {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }

    override fun onResume() {
        super.onResume()
        val currentTime = System.currentTimeMillis()
        if (lastInteractionTime != 0L && currentTime - lastInteractionTime > logoutTimeout) {
            FirebaseAuth.getInstance().signOut()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }

    override fun onUserInteraction() {
        super.onUserInteraction()
        lastInteractionTime = System.currentTimeMillis()
    }
}
