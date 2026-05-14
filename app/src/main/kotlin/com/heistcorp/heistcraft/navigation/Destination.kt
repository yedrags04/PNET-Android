package com.heistcorp.heistcraft.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Help
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.Handyman
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.ui.graphics.vector.ImageVector

/** Destinos de navegación y barra inferior. Acceso y perfil desde la pantalla de inicio. */
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

    data object Reservas : Destination(
        route = "reservas",
        label = "Reservas",
        icon = Icons.AutoMirrored.Filled.List,
        contentDescription = "Listado de reservas",
    )

    data object Login : Destination(
        route = "login",
        label = "Iniciar sesión",
        icon = Icons.Filled.Lock,
        contentDescription = "Iniciar sesión",
    )

    data object Signup : Destination(
        route = "signup",
        label = "Registro",
        icon = Icons.Filled.PersonAdd,
        contentDescription = "Crear cuenta",
    )

    data object Perfil : Destination(
        route = "perfil",
        label = "Perfil",
        icon = Icons.Filled.AccountCircle,
        contentDescription = "Perfil de usuario",
    )

    data object Faq : Destination(
        route = "faq",
        label = "FAQ",
        icon = Icons.AutoMirrored.Filled.Help,
        contentDescription = "Preguntas frecuentes",
    )

    companion object {
        val bottomDestinations = listOf(Inicio, Bancos, Utensilios, Faq)
    }
}

object NavRoutes {
    const val RESERVA_DETALLE = "reserva_detalle/{id}"
    const val EDITAR_RESERVA = "editar_reserva/{id}"
    const val NUEVA_RESERVA = "nueva_reserva"

    fun reservaDetalle(id: String) = "reserva_detalle/$id"

    fun editarReserva(id: String) = "editar_reserva/$id"
}
