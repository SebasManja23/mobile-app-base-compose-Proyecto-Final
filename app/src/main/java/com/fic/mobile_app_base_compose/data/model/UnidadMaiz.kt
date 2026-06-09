package com.fic.mobile_app_base_compose.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "unidades")
data class UnidadMaiz(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val nombreRes: Int,
    val clave: String
)
