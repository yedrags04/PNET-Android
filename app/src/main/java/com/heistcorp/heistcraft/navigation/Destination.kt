package com.heistcorp.heistcraft.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.Handyman
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.MeetingRoom
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material.icons.filled.Place
import androidx.compose.ui.graphics.vector.ImageVector

/**
 * Destinos de navegación. La barra inferior cubre el flujo principal; el cajón lateral (rúbrica)
 * incluye Salas, Reservas, Animales, Tutorial y Localización.
 */
sealed class Destination(
    val route: String,
    val label: String,
    val icon: ImageVector,
    val contentDescription: String,
) {
    data object Inicio : Destination(
        route = "inicio",
        label = "Inicio",
        icon = Icons.Filled.Home,
        contentDescription = "Pantalla de inicio",
    )

    data object Bancos : Destination(
        route = "bancos",
        label = "Bancos",
        icon = Icons.Filled.AttachMoney,
        contentDescription = "Pantalla de bancos",
    )

    data object Utensilios : Destination(
        route = "utensilios",
        label = "Utensilios",
        icon = Icons.Filled.Handyman,
        contentDescription = "Pantalla de utensilios",
    )

    data object Salas : Destination(
        route = "salas",
        label = "Salas",
        icon = Icons.Filled.MeetingRoom,
        contentDescription = "Información de salas",
    )

    data object Reservas : Destination(
        route = "reservas",
        label = "Reservas",
        icon = Icons.Filled.List,
        contentDescription = "Listado de reservas",
    )

    data object Animales : Destination(
        route = "animales",
        label = "Animales",
        icon = Icons.Filled.Pets,
        contentDescription = "API animales MongoDB",
    )

    data object Tutorial : Destination(
        route = "tutorial",
        label = "Tutorial",
        icon = Icons.Filled.MenuBook,
        contentDescription = "Guía de uso",
    )

    data object Localizacion : Destination(
        route = "localizacion",
        label = "Localización",
        icon = Icons.Filled.Place,
        contentDescription = "Mapa de instalaciones",
    )

    data object Faq : Destination(
        route = "faq",
        label = "FAQ",
        icon = Icons.Filled.HelpOutline,
        contentDescription = "Preguntas frecuentes",
    )

    companion object {
        val bottomDestinations = listOf(Inicio, Bancos, Utensilios)

        /** Destinos adicionales del menú lateral (Navigation Drawer). */
        val drawerDestinations =
            listOf(Salas, Reservas, Animales, Tutorial, Localizacion, Faq)

        @Deprecated("Usar bottomDestinations", ReplaceWith("Destination.bottomDestinations"))
        val topLevelDestinations = bottomDestinations
    }
}

object NavRoutes {
    const val RESERVA_DETALLE = "reserva_detalle/{id}"
    const val EDITAR_RESERVA = "editar_reserva/{id}"
    const val NUEVA_RESERVA = "nueva_reserva"

    fun reservaDetalle(id: String) = "reserva_detalle/$id"

    fun editarReserva(id: String) = "editar_reserva/$id"
}
