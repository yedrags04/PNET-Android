package com.heistcorp.heistcraft.screens

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
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
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.heistcorp.heistcraft.data.BancoApi
import com.heistcorp.heistcraft.data.ReservaApi
import com.heistcorp.heistcraft.network.ApiClient
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.heistcorp.heistcraft.util.resolveAssetUrl
import java.util.Locale
import kotlinx.coroutines.launch
import retrofit2.HttpException

private data class BancoRow(
    val banco: BancoApi,
    val reserva: ReservaApi?,
) {
    val reserved: Boolean get() = reserva != null

    fun matchesFilter(
        location: String,
        difficulty: String,
        maxReward: Int,
        availability: String,
    ): Boolean {
        val addr = banco.address.lowercase()
        val diff = banco.difficulty.lowercase()
        val displayedAvail =
            when {
                reserved -> "no"
                banco.available -> "si"
                else -> "no"
            }
        val locOk = location.isBlank() || addr.contains(location.lowercase())
        val diffOk = difficulty.isBlank() || diff == difficulty.lowercase()
        val rewardOk = banco.reward <= maxReward
        val availOk = availability == "todos" || displayedAvail == availability
        return locOk && diffOk && rewardOk && availOk
    }
}

@Composable
fun BancosScreen(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val api = remember { ApiClient.heistApi }
    val scope = rememberCoroutineScope()

    var bancos by remember { mutableStateOf<List<BancoApi>>(emptyList()) }
    var reservas by remember { mutableStateOf<List<ReservaApi>>(emptyList()) }
    var loading by remember { mutableStateOf(true) }
    var loadError by remember { mutableStateOf<String?>(null) }

    var selectedRow by remember { mutableStateOf<BancoRow?>(null) }
    var showForm by remember { mutableStateOf(false) }
    var editingReserva by remember { mutableStateOf<ReservaApi?>(null) }

    var mostrarFiltros by remember { mutableStateOf(false) }
    var filtroLocalizacion by remember { mutableStateOf("") }
    var filtroDificultad by remember { mutableStateOf("") }
    var expandirDificultad by remember { mutableStateOf(false) }
    var filtroRecompensa by remember { mutableFloatStateOf(1000f) }
    var filtroDisponibilidad by remember { mutableStateOf("todos") }
    var expandirDisponibilidad by remember { mutableStateOf(false) }

    fun toast(msg: String) {
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
    }

    fun refreshData() {
        scope.launch {
            loading = true
            loadError = null
            try {
                bancos = api.getBancos()
                reservas = api.getReservas()
            } catch (e: Exception) {
                loadError = e.message ?: "No se pudo conectar con el servidor"
                if (e is HttpException) {
                    loadError = "Error HTTP ${e.code()}: ${e.message}"
                }
            } finally {
                loading = false
            }
        }
    }

    LaunchedEffect(Unit) {
        refreshData()
    }

    val rows =
        remember(bancos, reservas) {
            bancos.map { b ->
                val r = reservas.firstOrNull { it.bankId == b.id }
                BancoRow(banco = b, reserva = r)
            }
        }

    val filtrados =
        remember(rows, filtroLocalizacion, filtroDificultad, filtroRecompensa, filtroDisponibilidad) {
            rows.filter {
                it.matchesFilter(
                    filtroLocalizacion,
                    filtroDificultad,
                    filtroRecompensa.toInt(),
                    filtroDisponibilidad,
                )
            }
        }

    Column(
        modifier =
            modifier
                .fillMaxSize()
                .background(Color(0xFF131722))
                .padding(16.dp),
    ) {
        Text(
            text = "Bancos disponibles",
            style = MaterialTheme.typography.headlineMedium,
            color = Color.White,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp),
        )

        Surface(
            color = Color(0xFF1E2333),
            shape = MaterialTheme.shapes.medium,
            modifier =
                Modifier
                    .fillMaxWidth()
                    .clickable { mostrarFiltros = !mostrarFiltros },
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text("Filtros", color = Color.White)
                Icon(
                    imageVector = if (mostrarFiltros) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown,
                    contentDescription = "Alternar filtros",
                    tint = Color.White,
                )
            }
        }

        AnimatedVisibility(visible = mostrarFiltros) {
            Column(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
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
                        trailingIcon = { Icon(Icons.Filled.ArrowDropDown, contentDescription = null) },
                        colors = filterFieldColors(),
                    )
                    DropdownMenu(
                        expanded = expandirDificultad,
                        onDismissRequest = { expandirDificultad = false },
                        modifier = Modifier.background(Color(0xFF1E2333)),
                    ) {
                        listOf(
                            "" to "Todos",
                            "facil" to "Fácil",
                            "media" to "Media",
                            "alta" to "Alta",
                        ).forEach { (value, label) ->
                            DropdownMenuItem(
                                text = { Text(label, color = Color.White) },
                                onClick = {
                                    filtroDificultad = value
                                    expandirDificultad = false
                                },
                            )
                        }
                    }
                }

                Column {
                    Text("Dinero a ganar: ${filtroRecompensa.toInt()} €", color = Color.White)
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
                        trailingIcon = { Icon(Icons.Filled.ArrowDropDown, contentDescription = null) },
                        colors = filterFieldColors(),
                    )
                    DropdownMenu(
                        expanded = expandirDisponibilidad,
                        onDismissRequest = { expandirDisponibilidad = false },
                        modifier = Modifier.background(Color(0xFF1E2333)),
                    ) {
                        listOf(
                            "todos" to "Todos",
                            "si" to "Sí (Disponible)",
                            "no" to "No (Ocupado)",
                        ).forEach { (value, label) ->
                            DropdownMenuItem(
                                text = { Text(label, color = Color.White) },
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
                    colors = ButtonDefaults.buttonColors(containerColor = Color.DarkGray),
                ) {
                    Text("Quitar filtros")
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        when {
            loading ->
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = Color(0xFF4CAF50))
                }
            loadError != null ->
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    Text(loadError!!, color = Color(0xFFFF7043))
                    Button(onClick = { refreshData() }) {
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
                            selectedRow = row
                        }
                    }
                }
        }
    }

    val sel = selectedRow
    if (sel != null && !showForm) {
        DetalleBancoDialog(
            row = sel,
            onDismiss = { selectedRow = null },
            onReservar = {
                editingReserva = null
                showForm = true
            },
            onEditar = {
                editingReserva = sel.reserva
                showForm = true
            },
            onCancelarReserva = {
                val rid = sel.reserva?.id
                if (rid != null) {
                    scope.launch {
                        try {
                            api.deleteReserva(rid)
                            toast("Reserva cancelada")
                            selectedRow = null
                            refreshData()
                        } catch (e: Exception) {
                            toast("No se pudo cancelar: ${e.message}")
                        }
                    }
                }
            },
        )
    }

    if (showForm && sel != null) {
        FormularioReservaDialog(
            bankId = sel.banco.id,
            bankLabel = sel.banco.name,
            editing = editingReserva,
            onDismiss = {
                showForm = false
                selectedRow = null
                editingReserva = null
            },
            onConfirm = { body, editingId ->
                scope.launch {
                    try {
                        if (editingId != null) {
                            api.updateReserva(editingId, body)
                            toast("Reserva actualizada")
                        } else {
                            api.createReserva(body)
                            toast("Reserva creada")
                        }
                        showForm = false
                        selectedRow = null
                        editingReserva = null
                        refreshData()
                    } catch (e: Exception) {
                        toast("Error al guardar: ${e.message}")
                    }
                }
            },
        )
    }
}

@Composable
private fun filterFieldColors() =
    OutlinedTextFieldDefaults.colors(
        focusedTextColor = Color.White,
        unfocusedTextColor = Color.White,
        disabledTextColor = Color.White,
        disabledBorderColor = Color.Gray,
        disabledTrailingIconColor = Color.White,
        focusedLabelColor = Color.LightGray,
        unfocusedLabelColor = Color.LightGray,
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
        colors = CardDefaults.elevatedCardColors(containerColor = Color(0xFF1E2333)),
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Box(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .height(100.dp)
                        .background(Color.DarkGray),
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
            Text(text = b.name, color = Color.White, fontWeight = FontWeight.Bold)
            if (row.reserved) {
                Text(text = "Reservado", color = Color(0xFFFF7043), style = MaterialTheme.typography.labelMedium)
            }
            Text(
                text =
                    "Dificultad: ${b.difficulty.replaceFirstChar { ch -> if (ch.isLowerCase()) ch.titlecase(Locale.getDefault()) else ch.toString() }}",
                color = Color.LightGray,
                style = MaterialTheme.typography.bodySmall,
            )
            Text(text = "${b.reward} €", color = Color(0xFF4CAF50), fontWeight = FontWeight.Bold)
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
        containerColor = Color(0xFF1E2333),
        titleContentColor = Color.White,
        textContentColor = Color.LightGray,
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
                Text(text = "Recompensa: ${b.reward} EUR", color = Color(0xFF4CAF50))
                Text(text = "Disponibilidad: $disponibleTexto")
            }
        },
        confirmButton = {
            if (row.reserved) {
                Row {
                    TextButton(onClick = onDismiss) {
                        Text("Cerrar", color = Color.LightGray)
                    }
                    TextButton(onClick = onEditar) {
                        Text("Editar reserva", color = Color(0xFF4CAF50))
                    }
                    Button(onClick = onCancelarReserva, colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD32F2F))) {
                        Text("Cancelar reserva")
                    }
                }
            } else {
                Row {
                    TextButton(onClick = onDismiss) {
                        Text("CERRAR", color = Color.LightGray)
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
                    Text(text = "CANCELAR", color = Color.Red)
                }
            }
        },
    )
}
