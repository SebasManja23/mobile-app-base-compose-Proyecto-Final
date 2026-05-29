package com.fic.mobile_app_base_compose.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.fic.mobile_app_base_compose.data.model.MovimientoMaiz
import com.fic.mobile_app_base_compose.data.model.Usuario

@Database(entities = [MovimientoMaiz::class, Usuario::class], version = 1, exportSchema = false)
abstract class MaizDatabase : RoomDatabase() {
    abstract fun movimientoDao(): MovimientoDao
    abstract fun usuarioDao(): UsuarioDao

    companion object {
        @Volatile
        private var Instance: MaizDatabase? = null
        fun getDatabase(context: Context): MaizDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, MaizDatabase::class.java, "maiz_database")
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { Instance = it }
            }
        }
    }
}