package com.fic.mobile_app_base_compose.data.local

import androidx.room.*
import com.fic.mobile_app_base_compose.data.model.ProductoMaiz
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductoDao {
    @Query("SELECT * FROM productos")
    fun obtenerTodos(): Flow<List<ProductoMaiz>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertar(producto: ProductoMaiz)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarTodos(productos: List<ProductoMaiz>)
}
