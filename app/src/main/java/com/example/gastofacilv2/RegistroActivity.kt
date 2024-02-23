package com.example.gastofacilv2
import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.*

class RegistroActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro)

        val editTextFecha = findViewById<EditText>(R.id.editTextFecha)
        val editTextCantidad = findViewById<EditText>(R.id.editTextCantidad)
        val spinnerTipoGasto = findViewById<Spinner>(R.id.spinnerTipoGasto)
        val editTextLugar = findViewById<EditText>(R.id.editTextLugar)
        val btnGuardar = findViewById<Button>(R.id.btnGuardar)

        // Configuración del Spinner con opciones de tipo de gasto
        val opcionesTipoGasto = arrayOf("Comida", "Transporte", "Entretenimiento", "Compras", "Otros")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, opcionesTipoGasto)
        spinnerTipoGasto.adapter = adapter

        // Configuración del EditText de fecha para abrir un DatePickerDialog
        editTextFecha.setOnClickListener {
            val calendario = Calendar.getInstance()
            val año = calendario.get(Calendar.YEAR)
            val mes = calendario.get(Calendar.MONTH)
            val dia = calendario.get(Calendar.DAY_OF_MONTH)

            val dpd = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                val cal = Calendar.getInstance().apply {
                    set(Calendar.YEAR, year)
                    set(Calendar.MONTH, monthOfYear)
                    set(Calendar.DAY_OF_MONTH, dayOfMonth)
                }
                val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                editTextFecha.setText(dateFormat.format(cal.time))
            }, año, mes, dia)
            dpd.show()
        }

        // Manejar clic en el botón de guardar
        btnGuardar.setOnClickListener {
            // Aquí puedes obtener los valores ingresados por el usuario
            val fecha = editTextFecha.text.toString()
            val cantidad = editTextCantidad.text.toString().toDoubleOrNull() ?: 0.0
            val tipoGasto = spinnerTipoGasto.selectedItem.toString()
            val lugar = editTextLugar.text.toString()

            PreferenceManager.saveGasto(this, fecha, cantidad, tipoGasto, lugar)

            // Luego puedes redirigir a otra actividad o realizar otra acción según sea necesario
        }
    }
}
