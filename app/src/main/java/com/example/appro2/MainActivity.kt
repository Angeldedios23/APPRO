package com.example.appro2

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 🔹 Insertar el MapaFragment dentro de map_container
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        val mapaFragment = MapaFragment()
        fragmentTransaction.replace(R.id.map_container, mapaFragment)
        fragmentTransaction.commit()

        val searchView = findViewById<SearchView>(R.id.search_view)
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        val btnColaborar = findViewById<Button>(R.id.btnColaborar)
        val btnCheckout = findViewById<Button>(R.id.btnResiduos)

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

        btnCheckout.setOnClickListener {
            startActivity(Intent(this, CheckoutActivity::class.java))
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
}
