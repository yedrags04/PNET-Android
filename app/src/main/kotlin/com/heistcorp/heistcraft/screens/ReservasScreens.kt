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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.heistcorp.heistcraft.navigation.Destination
import com.heistcorp.heistcraft.navigation.NavRoutes
import com.heistcorp.heistcraft.network.ApiClient
import com.heistcorp.heistcraft.screens.reservas.ReservaDetalleViewModel
import com.heistcorp.heistcraft.screens.reservas.ReservaDetalleViewModelFactory
import com.heistcorp.heistcraft.screens.reservas.ReservaFormViewModel
import com.heistcorp.heistcraft.screens.reservas.ReservaFormViewModelFactory
import com.heistcorp.heistcraft.screens.reservas.ReservasListViewModel
import com.heistcorp.heistcraft.screens.reservas.ReservasListViewModelFactory
import com.heistcorp.heistcraft.screens.reservas.ReservasRepository
import com.heistcorp.heistcraft.ui.theme.HeistPalette

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReservasListScreen(navController: NavHostController) {
    val viewModel: ReservasListViewModel =
        viewModel(
            factory =
                remember {
                    ReservasListViewModelFactory(
                        repository = ReservasRepository(ApiClient.heistApi),
                    )
                },
        )
    val uiState = viewModel.uiState

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
            uiState.cargando ->
                Text(
                    "Cargando…",
                    modifier = Modifier
                        .padding(padding)
                        .padding(16.dp),
                    color = HeistPalette.text
                )

            uiState.error != null ->
                Column(Modifier
                    .padding(padding)
                    .padding(16.dp)) {
                    Text(uiState.error.orEmpty(), color = HeistPalette.errorSoft)
                    TextButton(onClick = viewModel::refresh) { Text("Reintentar") }
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
                    items(uiState.reservas, key = { it.id }) { reserva ->
                        Card(
                            modifier =
                                Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        navController.navigate(
                                            NavRoutes.reservaDetalle(
                                                reserva.id
                                            )
                                        )
                                    },
                            colors = CardDefaults.cardColors(containerColor = HeistPalette.card),
                        ) {
                            Column(Modifier.padding(16.dp)) {
                                Text(
                                    reserva.leaderName ?: "—",
                                    color = HeistPalette.text,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    "Banco: ${reserva.bankId ?: "—"}",
                                    color = HeistPalette.muted,
                                    style = MaterialTheme.typography.bodySmall
                                )
                                Text(
                                    "${reserva.operationDate ?: ""} ${reserva.operationTime ?: ""}",
                                    color = HeistPalette.positiveGreen
                                )
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
    val viewModel: ReservaDetalleViewModel =
        viewModel(
            factory =
                remember {
                    ReservaDetalleViewModelFactory(
                        repository = ReservasRepository(ApiClient.heistApi),
                    )
                },
        )
    val uiState = viewModel.uiState

    var confirmarBorrado by remember { mutableStateOf(false) }

    LaunchedEffect(reservaId) {
        viewModel.loadReserva(reservaId)
    }

    LaunchedEffect(uiState.userMessage) {
        val message = uiState.userMessage ?: return@LaunchedEffect
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
        viewModel.consumeUserMessage()
    }

    LaunchedEffect(uiState.deleted) {
        if (uiState.deleted) {
            navController.popBackStack(Destination.Reservas.route, inclusive = false)
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
        val reserva = uiState.reserva
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
                uiState.cargando -> Text("Cargando…", color = HeistPalette.text)
                reserva == null -> Text(
                    uiState.error ?: "No se encontró la reserva.",
                    color = HeistPalette.errorSoft
                )

                else -> {
                    Text(
                        "Líder: ${reserva.leaderName}",
                        color = HeistPalette.text,
                        fontWeight = FontWeight.Bold
                    )
                    Text("Email: ${reserva.leaderEmail}", color = HeistPalette.muted)
                    Text("Experiencia: ${reserva.experience}", color = HeistPalette.muted)
                    Text("Banco (id): ${reserva.bankId}", color = HeistPalette.muted)
                    Text(
                        "Fecha: ${reserva.operationDate}  Hora: ${reserva.operationTime}",
                        color = HeistPalette.text
                    )
                    Text(
                        "Equipo: ${reserva.teamSize}  Riesgo: ${reserva.riskLevel}",
                        color = HeistPalette.muted
                    )
                    Text("Presupuesto: ${reserva.budget} €", color = HeistPalette.positiveGreen)
                    Text("Equipo necesario:\n${reserva.equipment}", color = HeistPalette.muted)
                    Text("Plan:\n${reserva.plan}", color = HeistPalette.muted)
                    Text(
                        "Especialidades: ${reserva.specialties?.joinToString()}",
                        color = HeistPalette.muted
                    )

                    TextButton(
                        onClick = { navController.navigate(NavRoutes.editarReserva(reserva.id)) },
                    ) {
                        Text("Editar", color = HeistPalette.positiveGreen)
                    }
                    TextButton(onClick = { confirmarBorrado = true }, enabled = !uiState.deleting) {
                        Text("Eliminar", color = HeistPalette.delete)
                    }
                }
            }
        }
    }

    if (confirmarBorrado && uiState.reserva != null) {
        AlertDialog(
            onDismissRequest = { confirmarBorrado = false },
            title = { Text("¿Eliminar reserva?") },
            text = { Text("Esta acción no se puede deshacer.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.deleteReserva()
                        confirmarBorrado = false
                    },
                    enabled = !uiState.deleting,
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
    val viewModel: ReservaFormViewModel =
        viewModel(
            factory =
                remember {
                    ReservaFormViewModelFactory(
                        repository = ReservasRepository(ApiClient.heistApi),
                    )
                },
        )
    val uiState = viewModel.uiState

    LaunchedEffect(reservaId) {
        viewModel.loadForEdit(reservaId)
    }

    LaunchedEffect(uiState.userMessage) {
        val message = uiState.userMessage ?: return@LaunchedEffect
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
        viewModel.consumeUserMessage()
    }

    LaunchedEffect(uiState.saved) {
        if (uiState.saved) {
            navController.popBackStack(Destination.Reservas.route, inclusive = false)
        }
    }

    val reserva = uiState.editingReserva
    val bankId = uiState.selectedBankId

    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .background(HeistPalette.screenBackground),
    ) {
        when {
            uiState.loading -> {
                Text("Cargando…", color = HeistPalette.text, modifier = Modifier.padding(24.dp))
            }

            reserva != null && bankId != null -> {
                FormularioReservaDialog(
                    bankId = bankId,
                    bankLabel = uiState.selectedBankLabel.ifBlank { bankId },
                    editing = reserva,
                    onDismiss = { navController.popBackStack() },
                    onConfirm = viewModel::saveReservation,
                )
            }

            else -> {
                Text(
                    uiState.error ?: "No se pudo cargar la reserva.",
                    color = HeistPalette.text,
                    modifier = Modifier.padding(24.dp),
                )
                TextButton(onClick = { navController.popBackStack() }) {
                    Text("Volver")
                }
            }
        }
    }
}

@Composable
fun NuevaReservaScreen(navController: NavHostController) {
    val context = LocalContext.current
    val viewModel: ReservaFormViewModel =
        viewModel(
            factory =
                remember {
                    ReservaFormViewModelFactory(
                        repository = ReservasRepository(ApiClient.heistApi),
                    )
                },
        )
    val uiState = viewModel.uiState

    LaunchedEffect(Unit) {
        viewModel.loadForCreate()
    }

    LaunchedEffect(uiState.userMessage) {
        val message = uiState.userMessage ?: return@LaunchedEffect
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
        viewModel.consumeUserMessage()
    }

    LaunchedEffect(uiState.saved) {
        if (uiState.saved) {
            navController.popBackStack(Destination.Reservas.route, inclusive = false)
        }
    }

    val bankId = uiState.selectedBankId

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

            when {
                uiState.loading -> {
                    Text(
                        "Cargando…",
                        color = HeistPalette.text,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                }

                uiState.error != null -> {
                    Text(
                        uiState.error.orEmpty(),
                        color = HeistPalette.errorSoft,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                    TextButton(
                        onClick = viewModel::loadForCreate,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    ) {
                        Text("Reintentar")
                    }
                }

                else -> {
                    LazyColumn(
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        items(uiState.bancos, key = { it.id }) { banco ->
                            Button(
                                onClick = {
                                    viewModel.selectBank(
                                        bankId = banco.id,
                                        bankLabel = banco.name
                                    )
                                },
                                modifier = Modifier.fillMaxWidth(),
                            ) {
                                Text(banco.name)
                            }
                        }
                    }
                }
            }

            TextButton(
                onClick = { navController.popBackStack() },
                modifier = Modifier.padding(16.dp)
            ) {
                Text("Cancelar")
            }
        } else {
            FormularioReservaDialog(
                bankId = bankId,
                bankLabel = uiState.selectedBankLabel,
                editing = null,
                onDismiss = { navController.popBackStack() },
                onConfirm = viewModel::saveReservation,
            )
        }
    }
}
