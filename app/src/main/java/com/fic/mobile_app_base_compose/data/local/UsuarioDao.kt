package com.fic.mobile_app_base_compose.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.fic.mobile_app_base_compose.data.model.Usuario

@Dao
interface UsuarioDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun registrar(usuario: Usuario)

    @Query("SELECT * FROM usuarios WHERE nombreUsuario = :nombreUsuario AND contrasena = :contrasena")
    suspend fun login(nombreUsuario: String, contrasena: String): Usuario?

    @Query("SELECT * FROM usuarios WHERE nombreUsuario = :nombreUsuario")
    suspend fun obtenerUsuario(nombreUsuario: String): Usuario?
}