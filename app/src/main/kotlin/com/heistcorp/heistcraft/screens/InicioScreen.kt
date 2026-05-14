package com.heistcorp.heistcraft.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.heistcorp.heistcraft.R
import com.heistcorp.heistcraft.auth.AppAuth
import com.heistcorp.heistcraft.data.BancoApi
import com.heistcorp.heistcraft.network.ApiClient
import com.heistcorp.heistcraft.ui.theme.HeistPalette
import com.heistcorp.heistcraft.util.resolveAssetUrl
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InicioScreen(
    onNavigateToLogin: () -> Unit,
    onNavigateToPerfil: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val sesionActiva = AppAuth.currentSession != null

    var textoBusqueda by remember { mutableStateOf("") }

    val datePickerState = rememberDatePickerState()
    var showDatePicker by remember { mutableStateOf(false) }
    var fechaTexto by remember { mutableStateOf("") }
    val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

    var tipoBotin by remember { mutableStateOf("") }
    var expandirBotin by remember { mutableStateOf(false) }

    val api = remember { ApiClient.heistApi }
    var bancosCarrusel by remember { mutableStateOf<List<BancoApi>>(emptyList()) }
    var cargando by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        cargando = true
        try {
            bancosCarrusel = api.getBancos()
        } catch (_: Exception) {
            bancosCarrusel = emptyList()
        } finally {
            cargando = false
        }
    }

    Column(
        modifier =
            modifier
                .fillMaxSize()
                .background(HeistPalette.screenBackground)
                .verticalScroll(rememberScrollState())
                .padding(vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(32.dp),
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                if (sesionActiva) {
                    IconButton(onClick = onNavigateToPerfil) {
                        Icon(
                            imageVector = Icons.Filled.AccountCircle,
                            contentDescription = stringResource(R.string.cd_ir_perfil),
                            modifier = Modifier.size(40.dp),
                            tint = MaterialTheme.colorScheme.primary,
                        )
                    }
                } else {
                    Button(
                        onClick = onNavigateToLogin,
                        colors =
                            ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primary,
                                contentColor = MaterialTheme.colorScheme.onPrimary,
                            ),
                    ) {
                        Text(
                            stringResource(R.string.iniciar_sesion),
                            style = MaterialTheme.typography.labelLarge,
                        )
                    }
                }
            }
            Text(
                text = "Expertos en robos desde 1942",
                style = MaterialTheme.typography.headlineLarge,
                color = HeistPalette.text,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(),
            )

            ElevatedCard(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.elevatedCardColors(containerColor = HeistPalette.card),
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    OutlinedTextField(
                        value = textoBusqueda,
                        onValueChange = { textoBusqueda = it },
                        placeholder = { Text("Buscar objetivo...") },
                        leadingIcon = { Icon(Icons.Filled.Search, contentDescription = "Buscar") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        colors =
                            OutlinedTextFieldDefaults.colors(
                                focusedTextColor = HeistPalette.text,
                                unfocusedTextColor = HeistPalette.text,
                            ),
                    )

                    Box(modifier = Modifier
                        .fillMaxWidth()
                        .clickable { showDatePicker = true }) {
                        OutlinedTextField(
                            value = fechaTexto,
                            onValueChange = { },
                            placeholder = { Text("Fecha del golpe") },
                            modifier = Modifier.fillMaxWidth(),
                            readOnly = true,
                            enabled = false,
                            colors =
                                OutlinedTextFieldDefaults.colors(
                                    disabledTextColor = HeistPalette.text,
                                    disabledBorderColor = HeistPalette.muted,
                                    disabledPlaceholderColor = HeistPalette.muted,
                                ),
                        )
                    }

                    Box(modifier = Modifier.fillMaxWidth()) {
                        OutlinedTextField(
                            value = if (tipoBotin.isEmpty()) "Tipo de botín" else tipoBotin,
                            onValueChange = {},
                            modifier =
                                Modifier
                                    .fillMaxWidth()
                                    .clickable { expandirBotin = true },
                            readOnly = true,
                            enabled = false,
                            trailingIcon = {
                                Icon(
                                    Icons.Filled.ArrowDropDown,
                                    contentDescription = null
                                )
                            },
                            colors =
                                OutlinedTextFieldDefaults.colors(
                                    disabledTextColor = HeistPalette.text,
                                    disabledBorderColor = HeistPalette.muted,
                                    disabledTrailingIconColor = HeistPalette.text,
                                ),
                        )
                        DropdownMenu(
                            expanded = expandirBotin,
                            onDismissRequest = { expandirBotin = false },
                            modifier = Modifier.background(HeistPalette.card),
                        ) {
                            val opcionesBotin =
                                listOf("Efectivo", "Lingotes de Oro", "Obras de Arte")
                            opcionesBotin.forEach { seleccion ->
                                DropdownMenuItem(
                                    text = { Text(seleccion, color = HeistPalette.text) },
                                    onClick = {
                                        tipoBotin = seleccion
                                        expandirBotin = false
                                    },
                                )
                            }
                        }
                    }

                    Button(
                        onClick = { /* Igual que la web: búsqueda local en catálogo (navegación opcional) */ },
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .height(50.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = HeistPalette.accent),
                    ) {
                        Text("Buscar", fontSize = MaterialTheme.typography.titleMedium.fontSize)
                    }
                }
            }
        }

        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            Text(
                text = "Top bancos",
                style = MaterialTheme.typography.titleLarge,
                color = HeistPalette.text,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 16.dp),
            )

            when {
                cargando ->
                    Box(
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .height(160.dp),
                        contentAlignment = Alignment.Center,
                    ) {
                        CircularProgressIndicator(color = HeistPalette.positiveGreen)
                    }

                bancosCarrusel.isEmpty() ->
                    Text(
                        "No se pudieron cargar los bancos. Comprueba que el servidor HeistCraft esté en marcha.",
                        color = HeistPalette.muted,
                        modifier = Modifier.padding(horizontal = 16.dp),
                    )

                else ->
                    LazyRow(
                        contentPadding = PaddingValues(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                    ) {
                        items(bancosCarrusel, key = { it.id }) { banco ->
                            val url = resolveAssetUrl(banco.image)
                            ElevatedCard(
                                modifier =
                                    Modifier
                                        .width(260.dp)
                                        .height(160.dp),
                                colors = CardDefaults.elevatedCardColors(containerColor = HeistPalette.card),
                            ) {
                                Box(modifier = Modifier.fillMaxSize()) {
                                    if (url.isNotBlank()) {
                                        AsyncImage(
                                            model =
                                                ImageRequest.Builder(LocalContext.current)
                                                    .data(url)
                                                    .crossfade(true)
                                                    .build(),
                                            contentDescription = banco.name,
                                            modifier = Modifier.fillMaxSize(),
                                            contentScale = ContentScale.Crop,
                                        )
                                    }
                                    Text(
                                        text = banco.name,
                                        color = HeistPalette.text,
                                        fontWeight = FontWeight.Bold,
                                        style = MaterialTheme.typography.titleMedium,
                                        textAlign = TextAlign.Center,
                                        modifier =
                                            Modifier
                                                .fillMaxSize()
                                                .background(HeistPalette.scrim)
                                                .padding(16.dp)
                                                .align(Alignment.Center),
                                    )
                                }
                            }
                        }
                    }
            }
        }
    }

    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        datePickerState.selectedDateMillis?.let { millis ->
                            fechaTexto =
                                formatter.format(
                                    Calendar.getInstance().apply {
                                        timeInMillis = millis
                                    }.time,
                                )
                        }
                        showDatePicker = false
                    },
                ) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text("Cancelar")
                }
            },
        ) {
            DatePicker(state = datePickerState)
        }
    }
}
