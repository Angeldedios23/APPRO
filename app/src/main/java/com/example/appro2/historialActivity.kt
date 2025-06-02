package com.example.appro2

import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet

class HistorialActivity : AppCompatActivity() {

    private lateinit var lineChart: LineChart
    private lateinit var txtPuntos: TextView
    private lateinit var btnMes: Button
    private lateinit var btnAnio: Button
    private lateinit var btnInfo: ImageButton
    private lateinit var backButton: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_historial)

        // Vincular vistas
        lineChart = findViewById(R.id.lineChart)
        txtPuntos = findViewById(R.id.txtPuntos)
        btnMes = findViewById(R.id.btnMes)
        btnAnio = findViewById(R.id.btnAnio)
        btnInfo = findViewById(R.id.btnInfo)
        backButton = findViewById(R.id.backButton)

        // Evento para regresar
        backButton.setOnClickListener {
            finish()
        }

        // Mostrar puntos recibidos desde la actividad anterior
        val puntos = intent.getDoubleExtra("puntos_totales", 0.0)
        txtPuntos.text = String.format("%.2f", puntos)

        // Eventos click
        btnInfo.setOnClickListener { mostrarDialogoInformativo() }
        btnMes.setOnClickListener { mostrarGraficaMensual() }
        btnAnio.setOnClickListener { mostrarGraficaAnual() }

        // Mostrar gráfica mensual por defecto
        mostrarGraficaMensual()
    }

    private fun mostrarGraficaMensual() {
        val datosKilos = listOf(5f, 6f, 7.2f, 8.5f, 9.2f, 12f, 15f, 14f, 17f, 20f, 25f)
        val entries = datosKilos.mapIndexed { index, value -> Entry(index.toFloat(), value) }
        actualizarGrafica(entries, "Kilos de desperdicio (Mes)")
    }

    private fun mostrarGraficaAnual() {
        val datosAnuales = listOf(50f, 60f, 70f, 85f, 92f, 120f, 150f, 140f, 170f, 200f, 250f, 300f)
        val entries = datosAnuales.mapIndexed { index, value -> Entry(index.toFloat(), value) }
        actualizarGrafica(entries, "Kilos de desperdicio (Año)")
    }

    private fun actualizarGrafica(entries: List<Entry>, label: String) {
        val dataSet = LineDataSet(entries, label)
        dataSet.color = resources.getColor(android.R.color.holo_blue_light, theme)
        dataSet.lineWidth = 2f
        dataSet.setDrawCircles(false)

        val lineData = LineData(dataSet)
        lineChart.data = lineData

        lineChart.axisRight.isEnabled = false
        lineChart.description.isEnabled = false

        val xAxis = lineChart.xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.granularity = 1f

        lineChart.invalidate()
    }

    private fun mostrarDialogoInformativo() {
        AlertDialog.Builder(this)
            .setTitle("Recompensa de puntos")
            .setMessage(
                "Por cada kilo de desecho orgánico que usted anota, gana 1 punto (los gramos se redondean).\n\n" +
                        "Puede usar los puntos para ganar premios o participar en rifas."
            )
            .setPositiveButton("Aceptar", null)
            .show()
    }
}
