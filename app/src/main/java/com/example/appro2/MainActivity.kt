package com.example.appro2

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.chip.Chip
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.Calendar

class MainActivity : AppCompatActivity() {

    private var lastInteractionTime: Long = 0
    private val logoutTimeout: Long = 10 * 60 * 1000 // 10 minutos

    private lateinit var chipRecompensa: Chip
    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)
        setContentView(R.layout.activity_main)

        val chipHistorial = findViewById<Chip>(R.id.chipHistorial)
        chipHistorial.setOnClickListener {
            val intent = Intent(this, HistorialActivity::class.java)
            startActivity(intent)
        }

        chipRecompensa = findViewById(R.id.chipRecompensa)
        chipRecompensa.setOnClickListener {
            reclamarRecompensaDiaria()
        }

        verificarSiYaReclamoHoy()

        // Fragmento del mapa
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

            override fun onQueryTextChange(newText: String?): Boolean = true
        })

        btnColaborar.setOnClickListener {
            startActivity(Intent(this, RolActivity::class.java))
        }

        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    Toast.makeText(this, "Estás en Inicio", Toast.LENGTH_SHORT).show()
                }
                R.id.nav_raffle -> {
                    startActivity(Intent(this, RifaActivity::class.java))
                }
                R.id.nav_profile -> {
                    startActivity(Intent(this, PerfilActivity::class.java))
                }
            }
            true
        }

    }

    private fun reclamarRecompensaDiaria() {
        val user = auth.currentUser ?: return
        val userRef = db.collection("usuarios").document(user.uid)

        userRef.get().addOnSuccessListener { doc ->
            val lastClaimed = doc.getLong("ultimaRecompensa") ?: 0L
            val hoy = obtenerFechaSinHora(System.currentTimeMillis())
            val ultimoDia = obtenerFechaSinHora(lastClaimed)

            if (hoy > ultimoDia) {
                val puntosGanados = calcularPuntosPorDia(hoy)
                val puntosActuales = doc.getDouble("puntos") ?: 0.0
                val nuevosPuntos = puntosActuales + puntosGanados

                userRef.update(
                    mapOf(
                        "puntos" to nuevosPuntos,
                        "ultimaRecompensa" to System.currentTimeMillis()
                    )
                ).addOnSuccessListener {
                    Toast.makeText(this, "¡Recompensa de $puntosGanados puntos obtenida!", Toast.LENGTH_SHORT).show()
                    chipRecompensa.isEnabled = false
                }.addOnFailureListener {
                    Toast.makeText(this, "Error al actualizar puntos", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Ya reclamaste tu recompensa hoy", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun verificarSiYaReclamoHoy() {
        val user = auth.currentUser ?: return
        val userRef = db.collection("usuarios").document(user.uid)

        userRef.get().addOnSuccessListener { doc ->
            val lastClaimed = doc.getLong("ultimaRecompensa") ?: 0L
            val hoy = obtenerFechaSinHora(System.currentTimeMillis())
            val ultimoDia = obtenerFechaSinHora(lastClaimed)

            chipRecompensa.isEnabled = hoy > ultimoDia
        }
    }

    private fun obtenerFechaSinHora(millis: Long): Long {
        val cal = Calendar.getInstance()
        cal.timeInMillis = millis
        cal.set(Calendar.HOUR_OF_DAY, 0)
        cal.set(Calendar.MINUTE, 0)
        cal.set(Calendar.SECOND, 0)
        cal.set(Calendar.MILLISECOND, 0)
        return cal.timeInMillis
    }

    private fun calcularPuntosPorDia(timestamp: Long): Double {
        // Lógica simple, puedes variar si quieres por día de la semana
        return 5.0
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
