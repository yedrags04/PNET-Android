package com.heistcorp.heistcraft.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.heistcorp.heistcraft.ui.theme.HeistPalette
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.heistcorp.heistcraft.data.BancoApi
import com.heistcorp.heistcraft.data.ReservaApi
import com.heistcorp.heistcraft.navigation.Destination
import com.heistcorp.heistcraft.navigation.NavRoutes
import com.heistcorp.heistcraft.network.ApiClient
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReservasListScreen(navController: NavHostController) {
    val api = remember { ApiClient.heistApi }
    val scope = rememberCoroutineScope()
    var reservas by remember { mutableStateOf<List<ReservaApi>>(emptyList()) }
    var cargando by remember { mutableStateOf(true) }
    var error by remember { mutableStateOf<String?>(null) }

    fun recargar() {
        scope.launch {
            cargando = true
            try {
                reservas = api.getReservas()
                error = null
            } catch (e: Exception) {
                error = e.message
            } finally {
                cargando = false
            }
        }
    }

    LaunchedEffect(Unit) {
        recargar()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Reservas vigentes") },
                colors =
                    TopAppBarDefaults.topAppBarColors(
                        containerColor = HeistPalette.card,
                        titleContentColor = HeistPalette.text,
                        navigationIconContentColor = HeistPalette.text,
                    ),
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                },
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { navController.navigate(NavRoutes.NUEVA_RESERVA) },
                icon = { Icon(Icons.Filled.Add, contentDescription = null) },
                text = { Text("Nueva reserva") },
                containerColor = HeistPalette.positiveGreen,
            )
        },
    ) { padding ->
        when {
            cargando ->
                Text("Cargando…", modifier = Modifier.padding(padding).padding(16.dp), color = HeistPalette.text)
            error != null ->
                Column(Modifier.padding(padding).padding(16.dp)) {
                    Text(error!!, color = HeistPalette.errorSoft)
                    TextButton(onClick = { recargar() }) { Text("Reintentar") }
                }
            else ->
                LazyColumn(
                    modifier =
                        Modifier
                            .fillMaxSize()
                            .background(HeistPalette.screenBackground)
                            .padding(padding),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    items(reservas, key = { it.id }) { r ->
                        Card(
                            modifier =
                                Modifier
                                    .fillMaxWidth()
                                    .clickable { navController.navigate(NavRoutes.reservaDetalle(r.id)) },
                            colors = CardDefaults.cardColors(containerColor = HeistPalette.card),
                        ) {
                            Column(Modifier.padding(16.dp)) {
                                Text(r.leaderName ?: "—", color = HeistPalette.text, fontWeight = FontWeight.Bold)
                                Text("Banco: ${r.bankId ?: "—"}", color = HeistPalette.muted, style = MaterialTheme.typography.bodySmall)
                                Text("${r.operationDate ?: ""} ${r.operationTime ?: ""}", color = HeistPalette.positiveGreen)
                            }
                        }
                    }
                }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReservaDetalleScreen(
    reservaId: String,
    navController: NavHostController,
) {
    val context = LocalContext.current
    val api = remember { ApiClient.heistApi }
    val scope = rememberCoroutineScope()
    var reserva by remember { mutableStateOf<ReservaApi?>(null) }
    var cargando by remember { mutableStateOf(true) }
    var confirmarBorrado by remember { mutableStateOf(false) }

    LaunchedEffect(reservaId) {
        cargando = true
        try {
            reserva = api.getReserva(reservaId)
        } catch (_: Exception) {
            reserva = null
        } finally {
            cargando = false
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalle de reserva") },
                colors =
                    TopAppBarDefaults.topAppBarColors(
                        containerColor = HeistPalette.card,
                        titleContentColor = HeistPalette.text,
                        navigationIconContentColor = HeistPalette.text,
                    ),
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                },
            )
        },
    ) { padding ->
        val r = reserva
        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .background(HeistPalette.screenBackground)
                    .padding(padding)
                    .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            when {
                cargando -> Text("Cargando…", color = HeistPalette.text)
                r == null -> Text("No se encontró la reserva.", color = HeistPalette.errorSoft)
                else -> {
                    Text("Líder: ${r.leaderName}", color = HeistPalette.text, fontWeight = FontWeight.Bold)
                    Text("Email: ${r.leaderEmail}", color = HeistPalette.muted)
                    Text("Experiencia: ${r.experience}", color = HeistPalette.muted)
                    Text("Banco (id): ${r.bankId}", color = HeistPalette.muted)
                    Text("Fecha: ${r.operationDate}  Hora: ${r.operationTime}", color = HeistPalette.text)
                    Text("Equipo: ${r.teamSize}  Riesgo: ${r.riskLevel}", color = HeistPalette.muted)
                    Text("Presupuesto: ${r.budget} €", color = HeistPalette.positiveGreen)
                    Text("Equipo necesario:\n${r.equipment}", color = HeistPalette.muted)
                    Text("Plan:\n${r.plan}", color = HeistPalette.muted)
                    Text("Especialidades: ${r.specialties?.joinToString()}", color = HeistPalette.muted)

                    TextButton(
                        onClick = { navController.navigate(NavRoutes.editarReserva(r.id)) },
                    ) {
                        Text("Editar", color = HeistPalette.positiveGreen)
                    }
                    TextButton(onClick = { confirmarBorrado = true }) {
                        Text("Eliminar", color = HeistPalette.delete)
                    }
                }
            }
        }
    }

    if (confirmarBorrado && reserva != null) {
        AlertDialog(
            onDismissRequest = { confirmarBorrado = false },
            title = { Text("¿Eliminar reserva?") },
            text = { Text("Esta acción no se puede deshacer.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        scope.launch {
                            try {
                                api.deleteReserva(reserva!!.id)
                                Toast.makeText(context, "Reserva eliminada", Toast.LENGTH_SHORT).show()
                                navController.popBackStack(Destination.Reservas.route, inclusive = false)
                            } catch (e: Exception) {
                                Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_LONG).show()
                            }
                        }
                        confirmarBorrado = false
                    },
                ) {
                    Text("Eliminar", color = HeistPalette.delete)
                }
            },
            dismissButton = {
                TextButton(onClick = { confirmarBorrado = false }) {
                    Text("Cancelar")
                }
            },
        )
    }
}

@Composable
fun EditarReservaScreen(
    reservaId: String,
    navController: NavHostController,
) {
    val context = LocalContext.current
    val api = remember { ApiClient.heistApi }
    val scope = rememberCoroutineScope()
    var reserva by remember { mutableStateOf<ReservaApi?>(null) }
    var bancoNombre by remember { mutableStateOf("") }

    LaunchedEffect(reservaId) {
        try {
            val r = api.getReserva(reservaId)
            reserva = r
            val bid = r.bankId ?: return@LaunchedEffect
            val bancos = api.getBancos()
            bancoNombre = bancos.find { it.id == bid }?.name ?: bid
        } catch (_: Exception) {
            reserva = null
        }
    }

    val r = reserva
    val bankId = r?.bankId

    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .background(HeistPalette.screenBackground),
    ) {
        if (r != null && bankId != null) {
            FormularioReservaDialog(
                bankId = bankId,
                bankLabel = bancoNombre.ifBlank { bankId },
                editing = r,
                onDismiss = { navController.popBackStack() },
                onConfirm = { body, editingId ->
                    scope.launch {
                        try {
                            if (editingId != null) {
                                api.updateReserva(editingId, body)
                                Toast.makeText(context, "Reserva actualizada", Toast.LENGTH_SHORT).show()
                            }
                            navController.popBackStack(Destination.Reservas.route, inclusive = false)
                        } catch (e: Exception) {
                            Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_LONG).show()
                        }
                    }
                },
            )
        } else {
            Text("No se pudo cargar la reserva.", color = HeistPalette.text, modifier = Modifier.padding(24.dp))
            TextButton(onClick = { navController.popBackStack() }) {
                Text("Volver")
            }
        }
    }
}

@Composable
fun NuevaReservaScreen(navController: NavHostController) {
    val context = LocalContext.current
    val api = remember { ApiClient.heistApi }
    val scope = rememberCoroutineScope()
    var bancos by remember { mutableStateOf<List<BancoApi>>(emptyList()) }
    var bankId by remember { mutableStateOf<String?>(null) }
    var bankLabel by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        try {
            bancos = api.getBancos()
        } catch (_: Exception) {
            bancos = emptyList()
        }
    }

    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .background(HeistPalette.screenBackground),
    ) {
        if (bankId == null) {
            Text(
                "Elige banco objetivo",
                color = HeistPalette.text,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(16.dp),
            )
            LazyColumn(
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                items(bancos, key = { it.id }) { b ->
                    Button(
                        onClick = {
                            bankId = b.id
                            bankLabel = b.name
                        },
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Text(b.name)
                    }
                }
            }
            TextButton(onClick = { navController.popBackStack() }, modifier = Modifier.padding(16.dp)) {
                Text("Cancelar")
            }
        } else {
            FormularioReservaDialog(
                bankId = bankId!!,
                bankLabel = bankLabel,
                editing = null,
                onDismiss = { navController.popBackStack() },
                onConfirm = { body, _ ->
                    scope.launch {
                        try {
                            api.createReserva(body)
                            Toast.makeText(context, "Reserva creada correctamente", Toast.LENGTH_LONG).show()
                            navController.popBackStack(Destination.Reservas.route, inclusive = false)
                        } catch (e: Exception) {
                            Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_LONG).show()
                        }
                    }
                },
            )
        }
    }
}
