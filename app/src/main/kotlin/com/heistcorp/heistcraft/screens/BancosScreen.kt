package com.heistcorp.heistcraft.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.heistcorp.heistcraft.network.ApiClient
import com.heistcorp.heistcraft.screens.bancos.BancoRow
import com.heistcorp.heistcraft.screens.bancos.BancosRepository
import com.heistcorp.heistcraft.screens.bancos.BancosViewModel
import com.heistcorp.heistcraft.screens.bancos.BancosViewModelFactory
import com.heistcorp.heistcraft.screens.bancos.matchesFilter
import com.heistcorp.heistcraft.screens.components.ExpandableFilterSection
import com.heistcorp.heistcraft.ui.theme.HeistPalette
import com.heistcorp.heistcraft.util.resolveAssetUrl
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BancosScreen(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val viewModel: BancosViewModel =
        viewModel(
            factory =
                remember {
                    BancosViewModelFactory(
                        repository = BancosRepository(ApiClient.heistApi),
                    )
                },
        )

    val uiState = viewModel.uiState

    var mostrarFiltros by remember { mutableStateOf(false) }
    var filtroLocalizacion by remember { mutableStateOf("") }
    var filtroDificultad by remember { mutableStateOf("") }
    var expandirDificultad by remember { mutableStateOf(false) }
    var filtroRecompensa by remember { mutableFloatStateOf(1000f) }
    var filtroDisponibilidad by remember { mutableStateOf("todos") }
    var expandirDisponibilidad by remember { mutableStateOf(false) }

    LaunchedEffect(uiState.userMessage) {
        val message = uiState.userMessage ?: return@LaunchedEffect
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
        viewModel.consumeUserMessage()
    }

    val filtrados =
        remember(
            uiState.rows,
            filtroLocalizacion,
            filtroDificultad,
            filtroRecompensa,
            filtroDisponibilidad
        ) {
            uiState.rows.filter {
                it.matchesFilter(
                    location = filtroLocalizacion,
                    difficulty = filtroDificultad,
                    maxReward = filtroRecompensa.toInt(),
                    availability = filtroDisponibilidad,
                )
            }
        }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Bancos disponibles") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                ),
            )
        }

    ) { innerPadding ->
        Column(
            modifier =
                modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(innerPadding)
                    .padding(16.dp),
        ) {

            ExpandableFilterSection(
                expanded = mostrarFiltros,
                onToggle = { mostrarFiltros = !mostrarFiltros },
            ) {
                OutlinedTextField(
                    value = filtroLocalizacion,
                    onValueChange = { filtroLocalizacion = it },
                    label = { Text("Localización (Ej: Madrid)") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = filterFieldColors(),
                )

                Box(modifier = Modifier.fillMaxWidth()) {
                    OutlinedTextField(
                        value =
                            when (filtroDificultad) {
                                "facil" -> "Fácil"
                                "media" -> "Media"
                                "alta" -> "Alta"
                                else -> "Todos"
                            },
                        onValueChange = {},
                        label = { Text("Dificultad") },
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .clickable { expandirDificultad = true },
                        readOnly = true,
                        enabled = false,
                        trailingIcon = {
                            Icon(
                                Icons.Filled.ArrowDropDown,
                                contentDescription = null
                            )
                        },
                        colors = filterFieldColors(),
                    )
                    DropdownMenu(
                        expanded = expandirDificultad,
                        onDismissRequest = { expandirDificultad = false },
                        modifier = Modifier.background(HeistPalette.card),
                    ) {
                        listOf(
                            "" to "Todos",
                            "facil" to "Fácil",
                            "media" to "Media",
                            "alta" to "Alta",
                        ).forEach { (value, label) ->
                            DropdownMenuItem(
                                text = { Text(label, color = HeistPalette.text) },
                                onClick = {
                                    filtroDificultad = value
                                    expandirDificultad = false
                                },
                            )
                        }
                    }
                }

                Column {
                    Text("Dinero a ganar: ${filtroRecompensa.toInt()} €", color = HeistPalette.text)
                    Slider(
                        value = filtroRecompensa,
                        onValueChange = { filtroRecompensa = it },
                        valueRange = 0f..1000f,
                    )
                }

                Box(modifier = Modifier.fillMaxWidth()) {
                    OutlinedTextField(
                        value =
                            when (filtroDisponibilidad) {
                                "si" -> "Sí (Disponible)"
                                "no" -> "No (Ocupado)"
                                else -> "Todos"
                            },
                        onValueChange = {},
                        label = { Text("Disponibilidad") },
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .clickable { expandirDisponibilidad = true },
                        readOnly = true,
                        enabled = false,
                        trailingIcon = {
                            Icon(
                                Icons.Filled.ArrowDropDown,
                                contentDescription = null
                            )
                        },
                        colors = filterFieldColors(),
                    )
                    DropdownMenu(
                        expanded = expandirDisponibilidad,
                        onDismissRequest = { expandirDisponibilidad = false },
                        modifier = Modifier.background(HeistPalette.card),
                    ) {
                        listOf(
                            "todos" to "Todos",
                            "si" to "Sí (Disponible)",
                            "no" to "No (Ocupado)",
                        ).forEach { (value, label) ->
                            DropdownMenuItem(
                                text = { Text(label, color = HeistPalette.text) },
                                onClick = {
                                    filtroDisponibilidad = value
                                    expandirDisponibilidad = false
                                },
                            )
                        }
                    }
                }

                Button(
                    onClick = {
                        filtroLocalizacion = ""
                        filtroDificultad = ""
                        filtroRecompensa = 1000f
                        filtroDisponibilidad = "todos"
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = HeistPalette.accent),
                ) {
                    Text("Quitar filtros")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            when {
                uiState.loading ->
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = HeistPalette.positiveGreen)
                    }

                uiState.loadError != null ->
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                    ) {
                        Text(uiState.loadError.orEmpty(), color = HeistPalette.errorSoft)
                        Button(onClick = viewModel::refreshData) {
                            Text("Reintentar")
                        }
                    }

                else ->
                    LazyVerticalGrid(
                        columns = GridCells.Adaptive(minSize = 160.dp),
                        contentPadding = PaddingValues(bottom = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                    ) {
                        items(filtrados, key = { it.banco.id }) { row ->
                            BancoCard(row = row) {
                                viewModel.selectRow(row)
                            }
                        }
                    }
            }
        }
    }

    val selectedRow = uiState.selectedRow
    if (selectedRow != null && !uiState.showForm) {
        DetalleBancoDialog(
            row = selectedRow,
            onDismiss = viewModel::dismissDetails,
            onReservar = viewModel::startCreateReservation,
            onEditar = viewModel::startEditReservation,
            onCancelarReserva = viewModel::cancelSelectedReservation,
        )
    }

    if (uiState.showForm && selectedRow != null) {
        FormularioReservaDialog(
            bankId = selectedRow.banco.id,
            bankLabel = selectedRow.banco.name,
            editing = uiState.editingReserva,
            onDismiss = viewModel::dismissForm,
            onConfirm = viewModel::saveReservation,
        )
    }
}

@Composable
private fun filterFieldColors() =
    OutlinedTextFieldDefaults.colors(
        focusedTextColor = HeistPalette.text,
        unfocusedTextColor = HeistPalette.text,
        disabledTextColor = HeistPalette.text,
        disabledBorderColor = HeistPalette.muted,
        disabledTrailingIconColor = HeistPalette.text,
        focusedLabelColor = HeistPalette.muted,
        unfocusedLabelColor = HeistPalette.muted,
    )

@Composable
private fun BancoCard(
    row: BancoRow,
    onClick: () -> Unit,
) {
    val b = row.banco
    val url = resolveAssetUrl(b.image)
    ElevatedCard(
        modifier =
            Modifier
                .fillMaxWidth()
                .clickable(onClick = onClick),
        colors = CardDefaults.elevatedCardColors(containerColor = HeistPalette.card),
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Box(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .height(100.dp)
                        .background(HeistPalette.neutralMid),
            ) {
                if (url.isNotBlank()) {
                    AsyncImage(
                        model =
                            ImageRequest.Builder(LocalContext.current)
                                .data(url)
                                .crossfade(true)
                                .build(),
                        contentDescription = b.name,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop,
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = b.name, color = HeistPalette.text, fontWeight = FontWeight.Bold)
            if (row.reserved) {
                Text(
                    text = "Reservado",
                    color = HeistPalette.errorSoft,
                    style = MaterialTheme.typography.labelMedium
                )
            }
            Text(
                text =
                    "Dificultad: ${
                        b.difficulty.replaceFirstChar { ch ->
                            if (ch.isLowerCase()) ch.titlecase(
                                Locale.getDefault()
                            ) else ch.toString()
                        }
                    }",
                color = HeistPalette.muted,
                style = MaterialTheme.typography.bodySmall,
            )
            Text(
                text = "${b.reward} €",
                color = HeistPalette.positiveGreen,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DetalleBancoDialog(
    row: BancoRow,
    onDismiss: () -> Unit,
    onReservar: () -> Unit,
    onEditar: () -> Unit,
    onCancelarReserva: () -> Unit,
) {
    val b = row.banco
    val url = resolveAssetUrl(b.image)
    val diffLabel =
        b.difficulty.replaceFirstChar { ch ->
            if (ch.isLowerCase()) ch.titlecase(Locale.getDefault()) else ch.toString()
        }
    val disponibleTexto =
        when {
            row.reserved -> "Ocupado (reservado)"
            b.available -> "Disponible"
            else -> "No disponible"
        }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = HeistPalette.card,
        titleContentColor = HeistPalette.text,
        textContentColor = HeistPalette.muted,
        title = { Text(text = b.name) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                if (url.isNotBlank()) {
                    AsyncImage(
                        model = url,
                        contentDescription = b.name,
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .height(160.dp),
                        contentScale = ContentScale.Crop,
                    )
                }
                Text(text = "Dirección: ${b.address}")
                Text(text = "Dificultad: $diffLabel")
                Text(text = "Recompensa: ${b.reward} EUR", color = HeistPalette.positiveGreen)
                Text(text = "Disponibilidad: $disponibleTexto")
            }
        },
        confirmButton = {
            if (row.reserved) {
                Row {
                    TextButton(onClick = onDismiss) {
                        Text("Cerrar", color = HeistPalette.muted)
                    }
                    TextButton(onClick = onEditar) {
                        Text("Editar reserva", color = HeistPalette.positiveGreen)
                    }
                    Button(
                        onClick = onCancelarReserva,
                        colors = ButtonDefaults.buttonColors(containerColor = HeistPalette.buttonDanger)
                    ) {
                        Text("Cancelar reserva")
                    }
                }
            } else {
                Row {
                    TextButton(onClick = onDismiss) {
                        Text("CERRAR", color = HeistPalette.muted)
                    }
                    Button(onClick = onReservar) {
                        Text("RESERVAR")
                    }
                }
            }
        },
        dismissButton = {
            if (!row.reserved) {
                TextButton(onClick = onDismiss) {
                    Text(text = "CANCELAR", color = HeistPalette.delete)
                }
            }
        },
    )
}
