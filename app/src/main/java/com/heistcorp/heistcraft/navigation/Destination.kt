package com.heistcorp.heistcraft.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.Handyman
import androidx.compose.material.icons.filled.Home
import androidx.compose.ui.graphics.vector.ImageVector

/**
 * Define todos los destinos de navegación disponibles en la aplicación
 * Cada destino tiene:
 * - route: identificador único para la navegación
 * - label: nombre que se muestra en la barra de navegación
 * - icon: icono que se muestra en la barra de navegación
 * - contentDescription: descripción de accesibilidad del icono
 */
sealed class Destination(
    val route: String,
    val label: String,
    val icon: ImageVector,
    val contentDescription: String
) {
    // Destino Inicio - Pantalla principal
    data object Inicio : Destination(
        route = "inicio",
        label = "Inicio",
        icon = Icons.Filled.Home,
        contentDescription = "Pantalla de inicio"
    )

    // Destino Bancos - Para gestionar bancos
    data object Bancos : Destination(
        route = "bancos",
        label = "Bancos",
        icon = Icons.Filled.AttachMoney,
        contentDescription = "Pantalla de bancos"
    )

    // Destino Utensilios - Para gestionar utensilios
    data object Utensilios : Destination(
        route = "utensilios",
        label = "Utensilios",
        icon = Icons.Filled.Handyman,
        contentDescription = "Pantalla de utensilios"
    )

    companion object {
        val topLevelDestinations = listOf(Inicio, Bancos, Utensilios)
    }
}
