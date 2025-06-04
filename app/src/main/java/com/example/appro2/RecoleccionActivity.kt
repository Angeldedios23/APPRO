package com.example.appro2

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import java.text.SimpleDateFormat
import java.util.*
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.auth.FirebaseAuth


class RecoleccionActivity : AppCompatActivity() {

    private val db = FirebaseFirestore.getInstance()
    private lateinit var fechaEditText: EditText
    private lateinit var listaLayout: LinearLayout
    private lateinit var puntosText: TextView
    private lateinit var cantidadText: TextView
    private lateinit var pesoAproxText: TextView
    private lateinit var totalText: TextView
    private lateinit var btnGuardarRecoleccion: Button

    private var totalKg = 0.0
    private var cantidadObjetos = 0
    private var puntosTotales = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recoleccion)

        val semanaTextView = findViewById<TextView>(R.id.semanaTextView)
        semanaTextView.text = obtenerTextoSemanaActual()

        fechaEditText = findViewById(R.id.fechaEditText)
        listaLayout = findViewById(R.id.listaResiduosLayout)
        puntosText = findViewById(R.id.puntosText)
        cantidadText = findViewById(R.id.cantidadText)
        pesoAproxText = findViewById(R.id.pesoAproxText)
        totalText = findViewById(R.id.totalText)
        btnGuardarRecoleccion = findViewById(R.id.btnGuardarRecoleccion)

        fechaEditText.setOnClickListener {
            val calendar = Calendar.getInstance()
            val datePicker = DatePickerDialog(
                this,
                { _, year, month, dayOfMonth ->
                    fechaEditText.setText("${month + 1}/$dayOfMonth/$year")
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            )
            datePicker.show()
        }

        findViewById<Button>(R.id.agregarResiduoBtn).setOnClickListener {
            mostrarFormularioResiduo()
        }

        btnGuardarRecoleccion.setOnClickListener {
            guardarEnFirestore()
        }
        //* Boton regresar
        findViewById<ImageButton>(R.id.backButton).setOnClickListener {
            finish()
        }
    }

    private fun guardarResiduoEnFirestore(nombre: String, cantidad: Int, pesoKg: Double, fecha: String) {
        val residuo = hashMapOf(
            "nombre" to nombre,
            "cantidad" to cantidad,
            "pesoKg" to pesoKg,
            "fecha" to fecha,
            "timestamp" to System.currentTimeMillis()
        )
        db.collection("residuos")
            .add(residuo)
            .addOnSuccessListener {
                Toast.makeText(this, "Residuo guardado", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Error al guardar", Toast.LENGTH_SHORT).show()
            }
    }

    private fun obtenerTextoSemanaActual(): String {
        val locale = Locale.getDefault()
        val calendar = Calendar.getInstance(locale)
        calendar.firstDayOfWeek = Calendar.MONDAY
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
        val primerDia = calendar.time

        calendar.add(Calendar.DAY_OF_WEEK, 6)
        val ultimoDia = calendar.time

        val formato = SimpleDateFormat("dd MMMM", locale)
        return "${formato.format(primerDia)} - ${formato.format(ultimoDia)}"
    }

    private fun agregarResiduo(nombre: String, cantidad: Int, pesoUnitarioKg: Double) {
        val item = TextView(this)
        item.text = "$nombre - $cantidad piezas"
        item.setTextColor(ContextCompat.getColor(this, android.R.color.white))
        listaLayout.addView(item)

        cantidadObjetos += cantidad
        val pesoTotal = cantidad * pesoUnitarioKg
        totalKg += pesoTotal
        puntosTotales = totalKg

        actualizarTotales()

        val fechaSeleccionada = fechaEditText.text.toString()
        guardarResiduoEnFirestore(nombre, cantidad, pesoUnitarioKg, fechaSeleccionada)
    }

    private fun actualizarTotales() {
        cantidadText.text = "Cantidad: $cantidadObjetos piezas"
        pesoAproxText.text = "Peso aprox: %.2f Kg".format(totalKg)
        totalText.text = "Total: %.2f Kg".format(totalKg)
        puntosText.text = "Puntos obtenidos: %.2f pts".format(puntosTotales)
    }

    private fun mostrarFormularioResiduo() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_agregar_residuo, null)
        val spinnerTipo = dialogView.findViewById<Spinner>(R.id.spinnerTipo)
        val editCantidad = dialogView.findViewById<EditText>(R.id.editCantidad)
        val editPeso = dialogView.findViewById<EditText>(R.id.editPeso)

        val opciones = listOf("Fruta", "Verduras", "Carnes", "Platillos")
        spinnerTipo.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, opciones)

        AlertDialog.Builder(this)
            .setTitle("Agregar residuo")
            .setView(dialogView)
            .setPositiveButton("Agregar") { _, _ ->
                val tipo = spinnerTipo.selectedItem.toString()
                val cantidadStr = editCantidad.text.toString()
                val pesoStr = editPeso.text.toString()

                if (cantidadStr.isBlank() || pesoStr.isBlank()) {
                    Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show()
                    return@setPositiveButton
                }

                val cantidad = cantidadStr.toIntOrNull()
                val pesoUnitario = pesoStr.toDoubleOrNull()

                if (cantidad == null || pesoUnitario == null) {
                    Toast.makeText(this, "Datos inválidos", Toast.LENGTH_SHORT).show()
                    return@setPositiveButton
                }

                agregarResiduo(tipo, cantidad, pesoUnitario)
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun guardarEnFirestore() {
        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser == null) {
            Toast.makeText(this, "Usuario no autenticado", Toast.LENGTH_SHORT).show()
            return
        }

        val residuos = mutableListOf<String>()
        for (i in 0 until listaLayout.childCount) {
            val item = listaLayout.getChildAt(i) as TextView
            residuos.add(item.text.toString())
        }

        val recolecta = hashMapOf(
            "fecha" to fechaEditText.text.toString(),
            "residuos" to residuos,
            "cantidadTotal" to cantidadObjetos,
            "pesoTotalKg" to totalKg,
            "puntos" to puntosTotales,
            "userId" to currentUser.uid, // 👈 MUY IMPORTANTE
            "timestamp" to System.currentTimeMillis()
        )

        db.collection("recolecciones")
            .add(recolecta)
            .addOnSuccessListener {
                Toast.makeText(this, "Recolección guardada en Firestore", Toast.LENGTH_SHORT).show()
                actualizarPuntosUsuario(puntosTotales) // 👈 Aquí llamamos a la función
                finish()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Error al guardar en Firestore", Toast.LENGTH_SHORT).show()
            }
    }


    private fun actualizarPuntosUsuario(puntosGanados: Double) {
        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return
        val usuarioRef = db.collection("usuarios").document(uid)

        usuarioRef.get()
            .addOnSuccessListener { doc ->
                val puntosActuales = doc.getDouble("puntos") ?: 0.0
                val nuevosPuntos = puntosActuales + puntosGanados

                usuarioRef.update("puntos", nuevosPuntos)
                    .addOnSuccessListener {
                        Toast.makeText(this, "Puntos actualizados", Toast.LENGTH_SHORT).show()
                    }
                    .addOnFailureListener {
                        Toast.makeText(this, "Error al actualizar puntos", Toast.LENGTH_SHORT).show()
                    }
            }
            .addOnFailureListener {
                Toast.makeText(this, "Error al leer puntos actuales", Toast.LENGTH_SHORT).show()
            }
    }


}
