package com.heistcorp.heistcraft.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.heistcorp.heistcraft.screens.AnimalesScreen
import com.heistcorp.heistcraft.screens.BancosScreen
import com.heistcorp.heistcraft.screens.EditarReservaScreen
import com.heistcorp.heistcraft.screens.FaqScreen
import com.heistcorp.heistcraft.screens.InicioScreen
import com.heistcorp.heistcraft.screens.LocalizacionScreen
import com.heistcorp.heistcraft.screens.NuevaReservaScreen
import com.heistcorp.heistcraft.screens.ReservaDetalleScreen
import com.heistcorp.heistcraft.screens.ReservasListScreen
import com.heistcorp.heistcraft.screens.SalasScreen
import com.heistcorp.heistcraft.screens.TutorialScreen
import com.heistcorp.heistcraft.screens.UtensiliosScreen

@Composable
fun AppNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    startDestination: String = Destination.Inicio.route,
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier,
    ) {
        composable(route = Destination.Inicio.route) {
            InicioScreen()
        }

        composable(route = Destination.Bancos.route) {
            BancosScreen()
        }

        composable(route = Destination.Utensilios.route) {
            UtensiliosScreen()
        }

        composable(route = Destination.Salas.route) {
            SalasScreen(onBack = { navController.popBackStack() })
        }

        composable(route = Destination.Reservas.route) {
            ReservasListScreen(navController = navController)
        }

        composable(
            route = NavRoutes.RESERVA_DETALLE,
            arguments = listOf(navArgument("id") { type = NavType.StringType }),
        ) { entry ->
            val id = entry.arguments?.getString("id").orEmpty()
            ReservaDetalleScreen(reservaId = id, navController = navController)
        }

        composable(
            route = NavRoutes.EDITAR_RESERVA,
            arguments = listOf(navArgument("id") { type = NavType.StringType }),
        ) { entry ->
            val id = entry.arguments?.getString("id").orEmpty()
            EditarReservaScreen(reservaId = id, navController = navController)
        }

        composable(route = NavRoutes.NUEVA_RESERVA) {
            NuevaReservaScreen(navController = navController)
        }

        composable(route = Destination.Animales.route) {
            AnimalesScreen(onBack = { navController.popBackStack() })
        }

        composable(route = Destination.Tutorial.route) {
            TutorialScreen(onBack = { navController.popBackStack() })
        }

        composable(route = Destination.Localizacion.route) {
            LocalizacionScreen(onBack = { navController.popBackStack() })
        }

        composable(route = Destination.Faq.route) {
            FaqScreen(onBack = { navController.popBackStack() })
        }
    }
}
