package com.example.gastofacilv2

import android.content.Context
import android.content.SharedPreferences

object PreferenceManager {
    private const val PREFS_NAME = "GastosPrefs"

    fun saveGasto(context: Context, fecha: String, cantidad: Double, tipoGasto: String, lugar: String) {
        val prefs: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = prefs.edit()
        val gastoId = System.currentTimeMillis() // ID único para cada gasto
        editor.putString("$gastoId:fecha", fecha)
        editor.putFloat("$gastoId:cantidad", cantidad.toFloat()) // Guardamos la cantidad como Float
        editor.putString("$gastoId:tipoGasto", tipoGasto)
        editor.putString("$gastoId:lugar", lugar)
        editor.apply()
    }
}
