package com.heistcorp.heistcraft.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.heistcorp.heistcraft.screens.BancosScreen
import com.heistcorp.heistcraft.screens.InicioScreen
import com.heistcorp.heistcraft.screens.UtensiliosScreen

/**
 * AppNavHost: Define la estructura de navegación de la aplicación
 *
 * Esto es como un "mapa" que le dice a la app:
 * - Qué pantalla mostrar para cada destino
 * - Cómo navegar de una pantalla a otra
 *
 * @param navController: controlador que maneja la navegación
 * @param startDestination: route del destino con el que comienza la app (por defecto: Inicio)
 * @param modifier: modificadores de diseño opcionales
 */
@Composable
fun AppNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    startDestination: String = Destination.Inicio.route
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        // Pantalla de Inicio
        composable(route = Destination.Inicio.route) {
            InicioScreen()
        }

        // Pantalla de Bancos
        composable(route = Destination.Bancos.route) {
            BancosScreen()
        }

        // Pantalla de Utensilios
        composable(route = Destination.Utensilios.route) {
            UtensiliosScreen()
        }
    }
}
