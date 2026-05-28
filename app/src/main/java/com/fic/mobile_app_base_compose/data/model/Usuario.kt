package com.fic.mobile_app_base_compose.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "usuarios")
data class Usuario(
    @PrimaryKey val nombreUsuario: String,
    val nombreCompleto: String,
    val correo: String,
    val contrasena: String
)