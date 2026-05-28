package com.fic.mobile_app_base_compose.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.fic.mobile_app_base_compose.data.model.MovimientoMaiz
import kotlinx.coroutines.flow.Flow

@Dao
interface MovimientoDao {
    @Insert
    suspend fun guardar(movimiento: MovimientoMaiz)

    @Query("SELECT * FROM movimientos ORDER BY id DESC")
    fun obtenerTodos(): Flow<List<MovimientoMaiz>>
}