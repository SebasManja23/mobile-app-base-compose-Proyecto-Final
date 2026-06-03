package com.fic.mobile_app_base_compose.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.fic.mobile_app_base_compose.ui.screens.*
import androidx.lifecycle.viewmodel.compose.viewModel
import com.fic.mobile_app_base_compose.viewmodel.MaizViewModel


sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Register : Screen("register")
    object MainMenu : Screen("main_menu")
    object Entradas : Screen("entradas")
    object Salidas : Screen("salidas")
    object Reportes : Screen("reportes")
    object History : Screen("history")
}
@Composable
fun MaizNavHost(navController: NavHostController) {
    // Inicializamos el ViewModel con su Factory
    val maizViewModel: MaizViewModel = viewModel(factory = MaizViewModel.Factory)

    NavHost(navController = navController, startDestination = Screen.Login.route) {
        composable(Screen.Login.route) {
            LoginScreen(navController, maizViewModel) // <-- Ahora sí pasamos el ViewModel
        }
        composable(Screen.Register.route) {
            RegisterScreen(navController, maizViewModel)
        }
        composable(Screen.Entradas.route) {
            EntradasScreen(navController, maizViewModel)
        }
        composable(Screen.Salidas.route) {
            SalidasScreen(navController, maizViewModel)
        }
        composable(Screen.MainMenu.route) {
            MainMenuScreen(navController)
        }
        composable(Screen.History.route) {
            HistoryScreen(navController, maizViewModel)
        }
        composable(Screen.Reportes.route) {
            ReportesScreen(navController, maizViewModel)
        }

    }
}


