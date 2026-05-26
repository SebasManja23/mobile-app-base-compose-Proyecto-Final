package com.fic.mobile_app_base_compose.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.fic.mobile_app_base_compose.ui.screens.*

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
    NavHost(navController = navController, startDestination = Screen.Login.route) {
        composable(Screen.Login.route) {
            LoginScreen(navController)
        }
        composable(Screen.Register.route) {
            RegisterScreen(navController)
        }
    }
}
