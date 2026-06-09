package com.fic.mobile_app_base_compose.data.local

import androidx.room.*
import com.fic.mobile_app_base_compose.data.model.UnidadMaiz
import kotlinx.coroutines.flow.Flow

@Dao
interface UnidadDao {
    @Query("SELECT * FROM unidades")
    fun obtenerTodas(): Flow<List<UnidadMaiz>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertar(unidad: UnidadMaiz)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarTodas(unidades: List<UnidadMaiz>)
}
