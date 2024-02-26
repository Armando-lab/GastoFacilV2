package com.example.gastofacilv2

import android.os.Bundle
import android.provider.BaseColumns
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class HistoryActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var gastoAdapter: GastoAdapter
    private lateinit var gastosList: MutableList<Gasto>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)

        gastosList = mutableListOf()
        gastoAdapter = GastoAdapter(gastosList)
        recyclerView.adapter = gastoAdapter

        cargarGastosGuardados()
    }

    private fun cargarGastosGuardados() {
        // Crear una instancia de GastoDBHelper
        val dbHelper = GastoDBHelper(this)

        // Obtener un Cursor con los datos de los gastos utilizando GastoDBHelper
        val cursor = dbHelper.readableDatabase.query(
            GastoContract.GastoEntry.TABLE_NAME,
            null,
            null,
            null,
            null,
            null,
            null
        )

        // Limpiar la lista antes de cargar nuevos datos
        gastosList.clear()

        // Iterar sobre el Cursor y agregar los gastos a la lista
        cursor?.use { cursor ->
            while (cursor.moveToNext()) {
                val fecha = cursor.getString(cursor.getColumnIndexOrThrow(GastoContract.GastoEntry.COLUMN_FECHA))
                val cantidad = cursor.getDouble(cursor.getColumnIndexOrThrow(GastoContract.GastoEntry.COLUMN_CANTIDAD))
                val tipoGasto = cursor.getString(cursor.getColumnIndexOrThrow(GastoContract.GastoEntry.COLUMN_TIPO_GASTO))
                val lugar = cursor.getString(cursor.getColumnIndexOrThrow(GastoContract.GastoEntry.COLUMN_LUGAR))

                val gasto = Gasto(fecha, cantidad, tipoGasto, lugar)
                gastosList.add(gasto)
            }
        }

        // Notificar al adaptador que los datos han cambiado
        gastoAdapter.notifyDataSetChanged()
    }
}
