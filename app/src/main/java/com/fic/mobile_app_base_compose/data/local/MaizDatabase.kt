package com.fic.mobile_app_base_compose.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.fic.mobile_app_base_compose.R
import com.fic.mobile_app_base_compose.data.model.MovimientoMaiz
import com.fic.mobile_app_base_compose.data.model.ProductoMaiz
import com.fic.mobile_app_base_compose.data.model.UnidadMaiz
import com.fic.mobile_app_base_compose.data.model.Usuario
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [MovimientoMaiz::class, Usuario::class, ProductoMaiz::class, UnidadMaiz::class],
    version = 2,
    exportSchema = false
)
abstract class MaizDatabase : RoomDatabase() {
    abstract fun movimientoDao(): MovimientoDao
    abstract fun usuarioDao(): UsuarioDao
    abstract fun productoDao(): ProductoDao
    abstract fun unidadDao(): UnidadDao

    companion object {
        @Volatile
        private var Instance: MaizDatabase? = null

        fun getDatabase(context: Context): MaizDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, MaizDatabase::class.java, "maiz_database")
                    .fallbackToDestructiveMigration()
                    .addCallback(MaizDatabaseCallback(context))
                    .build()
                    .also { Instance = it }
            }
        }
    }

    private class MaizDatabaseCallback(
        private val context: Context
    ) : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            Instance?.let { database ->
                CoroutineScope(Dispatchers.IO).launch {
                    prepopulateDatabase(database)
                }
            }
        }

        private suspend fun prepopulateDatabase(db: MaizDatabase) {
            val productoDao = db.productoDao()
            val unidadDao = db.unidadDao()

            productoDao.insertarTodos(
                listOf(
                    ProductoMaiz(nombreRes = R.string.val_maiz, clave = "Maíz"),
                    ProductoMaiz(nombreRes = R.string.val_maiz_quebrado, clave = "Maíz quebrado"),
                    ProductoMaiz(nombreRes = R.string.val_mochote, clave = "Mochote"),
                    ProductoMaiz(nombreRes = R.string.val_tamo, clave = "Tamo")
                )
            )

            unidadDao.insertarTodas(
                listOf(
                    UnidadMaiz(nombreRes = R.string.unit_kg, clave = "kg"),
                    UnidadMaiz(nombreRes = R.string.unit_sacks, clave = "Sacos"),
                    UnidadMaiz(nombreRes = R.string.unit_tons, clave = "Toneladas")
                )
            )
        }
    }
}
