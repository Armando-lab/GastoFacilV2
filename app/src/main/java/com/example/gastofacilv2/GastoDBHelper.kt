package com.example.gastofacilv2

import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class GastoDBHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        val SQL_CREATE_ENTRIES =
            "CREATE TABLE ${GastoContract.GastoEntry.TABLE_NAME} (" +
                    "${BaseColumns._ID} INTEGER PRIMARY KEY," +
                    "${GastoContract.GastoEntry.COLUMN_FECHA} TEXT," +
                    "${GastoContract.GastoEntry.COLUMN_CANTIDAD} REAL," +
                    "${GastoContract.GastoEntry.COLUMN_TIPO_GASTO} TEXT," +
                    "${GastoContract.GastoEntry.COLUMN_LUGAR} TEXT)"

        db.execSQL(SQL_CREATE_ENTRIES)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // Aquí puedes definir cómo manejar las actualizaciones de la base de datos
    }

    fun getAllGastos(): List<Gasto> {
        return getFilteredGastos(null, null)
    }

    fun getGastosSemana(): List<Gasto> {
        val gastosList = mutableListOf<Gasto>()
        val db = readableDatabase

        val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        val calendar = Calendar.getInstance()

        // Obtener la fecha de inicio de la semana actual
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY)
        val startOfWeek = dateFormat.format(calendar.time)

        // Obtener la fecha de fin de la semana actual
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY)
        val endOfWeek = dateFormat.format(calendar.time)

        val cursor: Cursor = db.query(
            GastoContract.GastoEntry.TABLE_NAME,
            null,
            "${GastoContract.GastoEntry.COLUMN_FECHA} BETWEEN ? AND ?",
            arrayOf(startOfWeek, endOfWeek),
            null,
            null,
            null
        )

        with(cursor) {
            while (moveToNext()) {
                val fecha = getString(getColumnIndexOrThrow(GastoContract.GastoEntry.COLUMN_FECHA))
                val cantidad = getDouble(getColumnIndexOrThrow(GastoContract.GastoEntry.COLUMN_CANTIDAD))
                val tipoGasto = getString(getColumnIndexOrThrow(GastoContract.GastoEntry.COLUMN_TIPO_GASTO))
                val lugar = getString(getColumnIndexOrThrow(GastoContract.GastoEntry.COLUMN_LUGAR))

                val gasto = Gasto(fecha, cantidad, tipoGasto, lugar)
                gastosList.add(gasto)
            }
        }
        cursor.close()
        return gastosList
    }

    fun getGastosMes(): List<Gasto> {
        val gastosList = mutableListOf<Gasto>()
        val db = readableDatabase

        val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        val calendar = Calendar.getInstance()

        // Obtener la fecha de inicio del mes actual
        calendar.set(Calendar.DAY_OF_MONTH, 1)
        val startOfMonth = dateFormat.format(calendar.time)

        // Obtener la fecha de fin del mes actual
        calendar.add(Calendar.MONTH, 1)
        calendar.add(Calendar.DATE, -1)
        val endOfMonth = dateFormat.format(calendar.time)

        val cursor: Cursor = db.query(
            GastoContract.GastoEntry.TABLE_NAME,
            null,
            "${GastoContract.GastoEntry.COLUMN_FECHA} BETWEEN ? AND ?",
            arrayOf(startOfMonth, endOfMonth),
            null,
            null,
            null
        )

        with(cursor) {
            while (moveToNext()) {
                val fecha = getString(getColumnIndexOrThrow(GastoContract.GastoEntry.COLUMN_FECHA))
                val cantidad = getDouble(getColumnIndexOrThrow(GastoContract.GastoEntry.COLUMN_CANTIDAD))
                val tipoGasto = getString(getColumnIndexOrThrow(GastoContract.GastoEntry.COLUMN_TIPO_GASTO))
                val lugar = getString(getColumnIndexOrThrow(GastoContract.GastoEntry.COLUMN_LUGAR))

                val gasto = Gasto(fecha, cantidad, tipoGasto, lugar)
                gastosList.add(gasto)
            }
        }
        cursor.close()
        return gastosList
    }

    fun getGastosAnio(): List<Gasto> {
        val gastosList = mutableListOf<Gasto>()
        val db = readableDatabase

        val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        val calendar = Calendar.getInstance()

        // Obtener la fecha de inicio del año actual
        calendar.set(Calendar.DAY_OF_YEAR, 1)
        val startOfYear = dateFormat.format(calendar.time)

        // Obtener la fecha de fin del año actual
        calendar.add(Calendar.YEAR, 1)
        calendar.add(Calendar.DATE, -1)
        val endOfYear = dateFormat.format(calendar.time)

        val cursor: Cursor = db.query(
            GastoContract.GastoEntry.TABLE_NAME,
            null,
            "${GastoContract.GastoEntry.COLUMN_FECHA} BETWEEN ? AND ?",
            arrayOf(startOfYear, endOfYear),
            null,
            null,
            null
        )

        with(cursor) {
            while (moveToNext()) {
                val fecha = getString(getColumnIndexOrThrow(GastoContract.GastoEntry.COLUMN_FECHA))
                val cantidad = getDouble(getColumnIndexOrThrow(GastoContract.GastoEntry.COLUMN_CANTIDAD))
                val tipoGasto = getString(getColumnIndexOrThrow(GastoContract.GastoEntry.COLUMN_TIPO_GASTO))
                val lugar = getString(getColumnIndexOrThrow(GastoContract.GastoEntry.COLUMN_LUGAR))

                val gasto = Gasto(fecha, cantidad, tipoGasto, lugar)
                gastosList.add(gasto)
            }
        }
        cursor.close()
        return gastosList
    }

    fun getGastosPorMesYAnio(mes: Int, anio: Int): List<Gasto> {
        val gastosList = mutableListOf<Gasto>()
        val db = readableDatabase

        // Calcular la fecha de inicio y fin del mes y año especificados
        val startOfMonth = String.format("%02d-%02d-%04d", 1, mes, anio)
        val endOfMonth = String.format("%02d-%02d-%04d", getUltimoDiaDelMes(mes, anio), mes, anio)

        // Consultar la base de datos para obtener los gastos dentro del rango de fechas especificado
        val cursor: Cursor = db.query(
            GastoContract.GastoEntry.TABLE_NAME,
            null,
            "substr(${GastoContract.GastoEntry.COLUMN_FECHA}, 4, 2) = ? AND substr(${GastoContract.GastoEntry.COLUMN_FECHA}, 7, 4) = ?",
            arrayOf(String.format("%02d", mes), anio.toString()),
            null,
            null,
            null
        )

        // Procesar el resultado de la consulta y agregar los gastos a la lista
        with(cursor) {
            while (moveToNext()) {
                val fecha = getString(getColumnIndexOrThrow(GastoContract.GastoEntry.COLUMN_FECHA))
                val cantidad = getDouble(getColumnIndexOrThrow(GastoContract.GastoEntry.COLUMN_CANTIDAD))
                val tipoGasto = getString(getColumnIndexOrThrow(GastoContract.GastoEntry.COLUMN_TIPO_GASTO))
                val lugar = getString(getColumnIndexOrThrow(GastoContract.GastoEntry.COLUMN_LUGAR))

                val gasto = Gasto(fecha, cantidad, tipoGasto, lugar)
                gastosList.add(gasto)
            }
        }
        cursor.close()
        return gastosList
    }

    // Función para obtener el último día del mes
    private fun getUltimoDiaDelMes(mes: Int, anio: Int): Int {
        val calendar = Calendar.getInstance()
        calendar.set(anio, mes - 1, 1) // El mes es 0-based en Calendar
        return calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
    }

    private fun getFilteredGastos(selection: String?, selectionArgs: Array<String>?): List<Gasto> {
        val gastosList = mutableListOf<Gasto>()
        val db = readableDatabase

        val projection = arrayOf(
            BaseColumns._ID,
            GastoContract.GastoEntry.COLUMN_FECHA,
            GastoContract.GastoEntry.COLUMN_CANTIDAD,
            GastoContract.GastoEntry.COLUMN_TIPO_GASTO,
            GastoContract.GastoEntry.COLUMN_LUGAR
        )

        val sortOrder = "${BaseColumns._ID} DESC"

        val cursor: Cursor = db.query(
            GastoContract.GastoEntry.TABLE_NAME,
            projection,
            selection,
            selectionArgs,
            null,
            null,
            sortOrder
        )

        with(cursor) {
            while (moveToNext()) {
                val fecha = getString(getColumnIndexOrThrow(GastoContract.GastoEntry.COLUMN_FECHA))
                val cantidad = getDouble(getColumnIndexOrThrow(GastoContract.GastoEntry.COLUMN_CANTIDAD))
                val tipoGasto = getString(getColumnIndexOrThrow(GastoContract.GastoEntry.COLUMN_TIPO_GASTO))
                val lugar = getString(getColumnIndexOrThrow(GastoContract.GastoEntry.COLUMN_LUGAR))

                val gasto = Gasto(fecha, cantidad, tipoGasto, lugar)
                gastosList.add(gasto)
            }
        }
        cursor.close()
        return gastosList
    }

    companion object {
        const val DATABASE_VERSION = 1
        const val DATABASE_NAME = "Gastos.db"
    }
}
