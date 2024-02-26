package com.example.gastofacilv2

import android.provider.BaseColumns

object GastoContract {
    object GastoEntry : BaseColumns {
        const val TABLE_NAME = "gastos"
        const val COLUMN_FECHA = "fecha"
        const val COLUMN_CANTIDAD = "cantidad"
        const val COLUMN_TIPO_GASTO = "tipo_gasto"
        const val COLUMN_LUGAR = "lugar"
    }
}
