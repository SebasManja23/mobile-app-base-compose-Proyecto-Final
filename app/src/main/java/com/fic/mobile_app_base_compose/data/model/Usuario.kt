package com.fic.mobile_app_base_compose.data.model

/**
 * Clase que representa a un usuario del sistema.
 * Por ahora es una clase de datos simple para definir el perfil.
 */
data class Usuario(
    val nombreUsuario: String,
    val nombreCompleto: String,
    val correo: String,
    val contrasena: String
)