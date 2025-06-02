package com.example.appro2

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Intent
import androidx.core.content.ContextCompat
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import java.util.*

class CheckoutActivity : AppCompatActivity() {

    private lateinit var fechaEditText: EditText
    private lateinit var observacionesEditText: EditText
    private lateinit var listaLayout: LinearLayout
    private lateinit var puntosText: TextView
    private lateinit var cantidadText: TextView
    private lateinit var pesoAproxText: TextView
    private lateinit var totalText: TextView

    private var totalKg = 0.0
    private var cantidadObjetos = 0
    private var puntosTotales = 0.0  // variable para puntos totales si la usas luego

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checkout)

        fechaEditText = findViewById(R.id.fechaEditText)
        observacionesEditText = findViewById(R.id.observacionesEditText)
        listaLayout = findViewById(R.id.listaResiduosLayout)
        puntosText = findViewById(R.id.puntosText)
        cantidadText = findViewById(R.id.cantidadText)
        pesoAproxText = findViewById(R.id.pesoAproxText)
        totalText = findViewById(R.id.totalText)

        // Fecha con calendario
        fechaEditText.setOnClickListener {
            val calendar = Calendar.getInstance()
            val datePicker = DatePickerDialog(this,
                { _, year, month, dayOfMonth ->
                    fechaEditText.setText("${month + 1}/$dayOfMonth/$year")
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH))
            datePicker.show()
        }

        // Agregar residuo
        findViewById<ImageButton>(R.id.agregarResiduoBtn).setOnClickListener {
            mostrarFormularioResiduo()  // Aquí abres el formulario para agregar residuo
        }

        // Botón finalizar
        findViewById<Button>(R.id.btnFinalizar).setOnClickListener {
            Toast.makeText(this, "Orden registrada", Toast.LENGTH_SHORT).show()

            // Ejemplo para enviar puntos a otra actividad
            val intent = Intent(this, HistorialActivity::class.java)
            intent.putExtra("puntos_totales", puntosTotales)
            startActivity(intent)
        }

        // Regresar
        findViewById<ImageButton>(R.id.backButton).setOnClickListener {
            finish()
        }
    }

    private fun agregarResiduo(nombre: String, cantidad: Int, pesoUnitarioKg: Double) {
        val item = TextView(this)
        item.text = "$nombre - $cantidad piezas"
        item.setTextColor(ContextCompat.getColor(this, android.R.color.white))
        listaLayout.addView(item)

        cantidadObjetos += cantidad
        val pesoTotal = cantidad * pesoUnitarioKg
        totalKg += pesoTotal
        puntosTotales = totalKg  // Ejemplo de asignar puntos

        actualizarTotales()
    }

    private fun actualizarTotales() {
        cantidadText.text = "Cantidad: $cantidadObjetos piezas"
        pesoAproxText.text = "Peso aprox: %.2f Kg".format(totalKg)
        totalText.text = "Total: %.2f Kg".format(totalKg)
        puntosText.text = "Puntos obtenidos: %.2f pts".format(puntosTotales)
    }

    private fun mostrarFormularioResiduo() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_agregar_residuo, null)
        val nombreEditText = dialogView.findViewById<EditText>(R.id.nombreResiduoEditText)
        val cantidadEditText = dialogView.findViewById<EditText>(R.id.cantidadEditText)
        val unidadSpinner = dialogView.findViewById<Spinner>(R.id.unidadSpinner)

        // Unidades disponibles
        val unidades = arrayOf("Kg", "gr", "Lts", "mL", "piezas")
        unidadSpinner.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, unidades)

        AlertDialog.Builder(this)
            .setTitle("Agregar residuo")
            .setView(dialogView)
            .setPositiveButton("Agregar") { _, _ ->
                val nombre = nombreEditText.text.toString()
                val cantidad = cantidadEditText.text.toString().toIntOrNull() ?: 0
                val unidad = unidadSpinner.selectedItem.toString()

                val pesoUnitario = obtenerPesoAproximado(nombre, unidad)
                val pesoConvertido = convertirUnidadAKg(pesoUnitario, unidad)

                agregarResiduo(nombre, cantidad, pesoConvertido)
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun obtenerPesoAproximado(nombre: String, unidad: String): Double {
        return when (nombre.lowercase()) {
            "pan" -> 95.0
            "cascara de sandia" -> 150.0
            "corazon de pera" -> 130.0
            else -> 100.0
        }
    }

    private fun convertirUnidadAKg(peso: Double, unidad: String): Double {
        return when (unidad) {
            "Kg" -> peso
            "gr" -> peso / 1000
            "mL" -> peso / 1000
            "Lts" -> peso
            "piezas" -> peso / 1000
            else -> peso / 1000
        }
    }
}
