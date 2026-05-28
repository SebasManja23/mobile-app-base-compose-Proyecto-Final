package com.fic.mobile_app_base_compose.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.fic.mobile_app_base_compose.MaizApp
import com.fic.mobile_app_base_compose.data.local.MovimientoDao
import com.fic.mobile_app_base_compose.data.local.UsuarioDao
import com.fic.mobile_app_base_compose.data.model.MovimientoMaiz
import com.fic.mobile_app_base_compose.data.model.Usuario
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MaizViewModel(
    private val movimientoDao: MovimientoDao,
    private val usuarioDao: UsuarioDao
) : ViewModel() {

    val todosLosMovimientos: StateFlow<List<MovimientoMaiz>> =
        movimientoDao.obtenerTodos()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyList()
            )

    fun guardarMovimiento(producto: String, tipo: String, cantidad: String, unidad: String, fecha: String) {
        viewModelScope.launch {
            val nuevo = MovimientoMaiz(
                producto = producto, tipo = tipo, cantidad = cantidad, unidad = unidad, fecha = fecha
            )
            movimientoDao.guardar(nuevo)
        }
    }

    suspend fun login(nombreUsuario: String, contrasena: String): Usuario? {
        return usuarioDao.login(nombreUsuario, contrasena)
    }

    suspend fun registrarUsuario(usuario: Usuario): Boolean {
        return try {
            if (usuarioDao.obtenerUsuario(usuario.nombreUsuario) != null) return false
            usuarioDao.registrar(usuario)
            true
        } catch (e: Exception) { false }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as MaizApp)
                MaizViewModel(
                    application.database.movimientoDao(),
                    application.database.usuarioDao()
                )
            }
        }
    }
}