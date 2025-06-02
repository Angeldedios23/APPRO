package com.example.appro2

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val searchView = findViewById<SearchView>(R.id.search_view)
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        val btnColaborar = findViewById<Button>(R.id.btnColaborar)

        // Búsqueda
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                Toast.makeText(this@MainActivity, "Buscando: $query", Toast.LENGTH_SHORT).show()
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }
        })

        // Botón "Colaborar" lleva a RolActivity
        btnColaborar.setOnClickListener {
            val intent = Intent(this, RolActivity::class.java)
            startActivity(intent)
        }
        // Boton Checkout para la anotacion de residuos
        val btnCheckout = findViewById<Button>(R.id.btnResiduos)
        btnCheckout.setOnClickListener {
            val intent = Intent(this, CheckoutActivity::class.java)
            startActivity(intent)
        }

        // Navegación inferior
        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    // Ya estás en MainActivity, no hacer nada o refrescar si quieres
                    Toast.makeText(this, "Estás en Inicio", Toast.LENGTH_SHORT).show()
                }
                R.id.nav_rewards -> {
                    // Ir a la pantalla de rifas o historial
                    val intent = Intent(this, HistorialActivity::class.java)
                    startActivity(intent)
                }
                R.id.nav_profile -> {
                    // Ir al perfil del usuario
                    val intent = Intent(this, PerfilActivity::class.java)
                    startActivity(intent)
                }
            }
            true
        }
    }
}