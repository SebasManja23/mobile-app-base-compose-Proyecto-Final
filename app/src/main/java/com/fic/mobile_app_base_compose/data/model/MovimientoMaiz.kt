package com.fic.mobile_app_base_compose.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

object MovimientoTipo {
    const val ENTRADA = "ENTRADA"
    const val SALIDA = "SALIDA"
}

@Entity(tableName = "movimientos")
data class MovimientoMaiz(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val producto: String,
    val tipo: String,
    val cantidad: String,
    val unidad: String,
    val fecha: String,
    val latitud: Double? = null,
    val longitud: Double? = null
)
