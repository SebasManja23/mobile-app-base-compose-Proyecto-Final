package com.fic.mobile_app_base_compose.data.model

/**
 * Estructura de datos para el inventario.
 * Define la información básica de una entrada o salida.
 */
data class MovimientoMaiz(
    val id: Int = 0,
    val producto: String, // Maíz, Mochote, Tamo, etc.
    val tipo: String,     // Entrada o Salida
    val cantidad: String,
    val unidad: String,   // kg, Toneladas, Sacos
    val fecha: String
)