package com.example.gastofacilv2

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.LegendEntry
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import kotlinx.coroutines.*
import java.util.Calendar

class ResumenActivity : AppCompatActivity() {

    private lateinit var spinnerMonth: Spinner
    private lateinit var spinnerYear: Spinner
    private lateinit var barChart: BarChart
    private lateinit var dbHelper: GastoDBHelper

    private val tipoGastoColors = mapOf(
        "Comida" to R.color.colorComida,
        "Transporte" to R.color.colorTransporte,
        "Entretenimiento" to R.color.colorEntretenimiento,
        "Compras" to R.color.colorCompras,
        "Otros" to R.color.colorOtros
    )

    // Mantenemos una referencia al trabajo actual
    private var currentJob: Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_resumen)

        spinnerMonth = findViewById(R.id.spinner_month)
        spinnerYear = findViewById(R.id.spinner_year)
        barChart = findViewById(R.id.bar_chart)
        dbHelper = GastoDBHelper(this)

        val monthAdapter = ArrayAdapter.createFromResource(
            this,
            R.array.month_options,
            R.layout.spinner_item
        )
        monthAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item)
        spinnerMonth.adapter = monthAdapter

        val yearAdapter = ArrayAdapter.createFromResource(
            this,
            R.array.year_options,
            R.layout.spinner_item
        )
        yearAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item)
        spinnerYear.adapter = yearAdapter

        // Obtener el mes y el año actual
        val calendar = Calendar.getInstance()
        val currentMonth = calendar.get(Calendar.MONTH)
        val currentYear = calendar.get(Calendar.YEAR)

        // Establecer el mes y el año actual seleccionados en los Spinners
        spinnerMonth.setSelection(currentMonth)
        val yearPosition = yearAdapter.getPosition(currentYear.toString())
        spinnerYear.setSelection(yearPosition)

        // Cargar los datos al iniciar la actividad con el mes y año actual
        loadChartData(currentMonth + 1, currentYear) // Sumamos 1 al mes porque los meses se indexan desde 0

        // Configurar listener para los Spinners
        spinnerMonth.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                loadChartData(getSelectedMonth(), getSelectedYear())
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        spinnerYear.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                loadChartData(getSelectedMonth(), getSelectedYear())
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun getSelectedMonth(): Int {
        return spinnerMonth.selectedItemPosition + 1
    }

    private fun getSelectedYear(): Int {
        return spinnerYear.selectedItem.toString().toInt()
    }

    private fun loadChartData(selectedMonth: Int, selectedYear: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            val gastos = dbHelper.getGastosPorMesYAnio(selectedMonth, selectedYear)

            val allCategories = tipoGastoColors.keys.toSet()

            val gastosPorTipo = HashMap<String, Double>()

            // Inicializar los gastos por tipo con 0 para todas las categorías
            allCategories.forEach { categoria ->
                gastosPorTipo[categoria] = 0.0
            }

            // Llenar los gastos reales para las categorías disponibles
            for (gasto in gastos) {
                val tipo = gasto.tipoGasto
                val cantidad = gasto.cantidad

                if (gastosPorTipo.containsKey(tipo)) {
                    gastosPorTipo[tipo] = gastosPorTipo[tipo]!! + cantidad
                }
            }

            val entries = ArrayList<BarEntry>()
            val colors = ArrayList<Int>()
            val legendEntries = ArrayList<LegendEntry>()

            // Crear entradas de datos para el gráfico y entradas de leyenda para todas las categorías
            allCategories.forEachIndexed { index, categoria ->
                val cantidad = gastosPorTipo[categoria]!!.toFloat()
                entries.add(BarEntry(index.toFloat(), cantidad))

                // Obtener el color correspondiente o usar un color predeterminado si no hay suficientes colores definidos
                val color = tipoGastoColors[categoria] ?: R.color.black
                colors.add(getColor(color))

                // Agregar entrada a la leyenda
                legendEntries.add(LegendEntry(categoria, Legend.LegendForm.SQUARE, 8f, 8f, null, getColor(color)))
            }

            val dataSet = BarDataSet(entries, "Gastos por Tipo")
            dataSet.colors = colors

            val barData = BarData(dataSet)
            barData.barWidth = 0.9f

            withContext(Dispatchers.Main) {
                barChart.data = barData

                // Configurar la ubicación de la leyenda
                val legend = barChart.legend
                legend.form = Legend.LegendForm.SQUARE
                legend.textSize = 12f
                legend.textColor = getColor(R.color.black)
                legend.verticalAlignment = Legend.LegendVerticalAlignment.BOTTOM
                legend.horizontalAlignment = Legend.LegendHorizontalAlignment.LEFT
                legend.orientation = Legend.LegendOrientation.HORIZONTAL
                legend.setDrawInside(false)
                legend.xEntrySpace = 20f
                legend.textSize = 8f

                legend.setCustom(legendEntries)
                legend.yEntrySpace = 8f
                legend.maxSizePercent = 0.8f

                barChart.invalidate()
                barChart.animateY(1500, Easing.EaseInOutCubic)
            }
        }
    }
}
