package com.heistcorp.heistcraft.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.heistcorp.heistcraft.data.UtensilioApi
import com.heistcorp.heistcraft.network.ApiClient
import com.heistcorp.heistcraft.screens.components.ExpandableFilterSection
import com.heistcorp.heistcraft.screens.utensilios.UtensiliosRepository
import com.heistcorp.heistcraft.screens.utensilios.UtensiliosViewModel
import com.heistcorp.heistcraft.screens.utensilios.UtensiliosViewModelFactory
import com.heistcorp.heistcraft.ui.theme.HeistPalette
import com.heistcorp.heistcraft.util.resolveAssetUrl

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UtensiliosScreen(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val viewModel: UtensiliosViewModel =
        viewModel(
            factory =
                remember {
                    UtensiliosViewModelFactory(
                        repository = UtensiliosRepository(ApiClient.heistApi),
                    )
                },
        )

    val uiState = viewModel.uiState

    var mostrarFiltros by remember { mutableStateOf(false) }
    var textoBusqueda by remember { mutableStateOf("") }
    var filtroPrecio by remember { mutableFloatStateOf(500f) }

    var mostrarCarrito by remember { mutableStateOf(false) }
    val carritoItems = remember { mutableStateListOf<UtensilioApi>() }

    val maxCatalogo = uiState.catalogo.maxOfOrNull { it.precio } ?: 300
    val rangoMax = maxOf(300f, maxCatalogo.toFloat(), filtroPrecio)

    val filtrados =
        remember(uiState.catalogo, textoBusqueda, filtroPrecio) {
            val q = textoBusqueda.trim().lowercase()
            uiState.catalogo.filter { utensilio ->
                val nombre = utensilio.nombre.lowercase()
                val descripcion = utensilio.descripcion.orEmpty().lowercase()
                val matchText = q.isEmpty() || nombre.contains(q) || descripcion.contains(q)
                val matchPrecio = utensilio.precio <= filtroPrecio.toInt()
                matchText && matchPrecio
            }
        }

    LaunchedEffect(maxCatalogo) {
        if (filtroPrecio < 1f || filtroPrecio > maxCatalogo.toFloat()) {
            filtroPrecio = maxCatalogo.toFloat().coerceAtLeast(1f)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Catálogo de utensilios") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                ),
                actions = {
                    IconButton(onClick = { mostrarCarrito = true }) {
                        Icon(
                            imageVector = Icons.Filled.ShoppingCart,
                            contentDescription = "Abrir carrito",
                            tint = HeistPalette.text,
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier =
                modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(innerPadding)
                    .padding(16.dp)
        ) {
            ExpandableFilterSection(
                expanded = mostrarFiltros,
                onToggle = { mostrarFiltros = !mostrarFiltros },
            ) {
                OutlinedTextField(
                    value = textoBusqueda,
                    onValueChange = { textoBusqueda = it },
                    label = { Text("Buscar por nombre o descripción") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    colors =
                        OutlinedTextFieldDefaults.colors(
                            focusedTextColor = HeistPalette.text,
                            unfocusedTextColor = HeistPalette.text,
                        ),
                )

                Column {
                    Text("Precio máximo: ${filtroPrecio.toInt()} €", color = HeistPalette.text)
                    Slider(
                        value = filtroPrecio,
                        onValueChange = { filtroPrecio = it },
                        valueRange = 0f..rangoMax,
                    )
                }

                Button(
                    onClick = {
                        textoBusqueda = ""
                        filtroPrecio = rangoMax
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = HeistPalette.accent),
                ) {
                    Text("Quitar filtros")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            when {
                uiState.cargando ->
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = HeistPalette.positiveGreen)
                    }

                uiState.error != null && uiState.catalogo.isEmpty() ->
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text(uiState.error.orEmpty(), color = HeistPalette.errorSoft)
                        Button(onClick = viewModel::refresh) {
                            Text("Reintentar")
                        }
                    }

                else ->
                    LazyVerticalGrid(
                        columns = GridCells.Adaptive(minSize = 150.dp),
                        contentPadding = PaddingValues(bottom = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                    ) {
                        items(filtrados, key = { it.id }) { utensilio ->
                            UtensilioCard(utensilio = utensilio) {
                                carritoItems.add(utensilio)
                                Toast.makeText(
                                    context,
                                    "Añadido: ${utensilio.nombre}",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }
            }
        }
    }


    if (mostrarCarrito) {
        CarritoDialog(
            articulos = carritoItems.toList(),
            onDismiss = { mostrarCarrito = false },
            onRemoveItem = { item -> carritoItems.remove(item) },
            onCheckout = {
                carritoItems.clear()
                mostrarCarrito = false
                Toast.makeText(context, "Pedido confirmado (solo en app)", Toast.LENGTH_SHORT)
                    .show()
            },
        )
    }
}

@Composable
private fun UtensilioCard(
    utensilio: UtensilioApi,
    onAddClick: () -> Unit,
) {
    val url = resolveAssetUrl(utensilio.imagen)
    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.elevatedCardColors(containerColor = HeistPalette.card),
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Box(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .height(90.dp)
                        .background(HeistPalette.neutralMid),
            ) {
                if (url.isNotBlank()) {
                    AsyncImage(
                        model = url,
                        contentDescription = utensilio.nombre,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop,
                    )
                }
            }
            Spacer(modifier = Modifier.height(12.dp))

            Text(text = utensilio.nombre, color = HeistPalette.text, fontWeight = FontWeight.Bold)
            Text(
                text = utensilio.descripcion.orEmpty(),
                color = HeistPalette.muted,
                style = MaterialTheme.typography.bodySmall,
                maxLines = 3,
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = "${utensilio.precio} €",
                    color = HeistPalette.positiveGreen,
                    fontWeight = FontWeight.Bold
                )

                Button(
                    onClick = onAddClick,
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                    modifier = Modifier.height(36.dp),
                ) {
                    Text("Añadir", style = MaterialTheme.typography.labelMedium)
                }
            }
        }
    }
}

@Composable
private fun CarritoDialog(
    articulos: List<UtensilioApi>,
    onDismiss: () -> Unit,
    onRemoveItem: (UtensilioApi) -> Unit,
    onCheckout: () -> Unit,
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false),
    ) {
        Surface(
            modifier =
                Modifier
                    .fillMaxWidth(0.9f)
                    .fillMaxHeight(0.7f),
            shape = MaterialTheme.shapes.large,
            color = HeistPalette.card,
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = "Mi Carrito",
                        style = MaterialTheme.typography.headlineSmall,
                        color = HeistPalette.text,
                        fontWeight = FontWeight.Bold,
                    )
                    IconButton(onClick = onDismiss) {
                        Icon(
                            Icons.Filled.Close,
                            contentDescription = "Cerrar",
                            tint = HeistPalette.muted
                        )
                    }
                }

                HorizontalDivider(
                    color = HeistPalette.neutralMid,
                    modifier = Modifier.padding(vertical = 8.dp)
                )

                if (articulos.isEmpty()) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("No hay utensilios seleccionados todavía.", color = HeistPalette.muted)
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        items(articulos.size) { index ->
                            val item = articulos[index]
                            Row(
                                modifier =
                                    Modifier
                                        .fillMaxWidth()
                                        .background(
                                            HeistPalette.divider,
                                            shape = MaterialTheme.shapes.small
                                        )
                                        .padding(12.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                Column {
                                    Text(item.nombre, color = HeistPalette.text)
                                    Text("${item.precio} €", color = HeistPalette.positiveGreen)
                                }
                                IconButton(onClick = { onRemoveItem(item) }) {
                                    Icon(
                                        Icons.Filled.Close,
                                        contentDescription = "Quitar",
                                        tint = HeistPalette.delete
                                    )
                                }
                            }
                        }
                    }

                    val total = articulos.sumOf { it.precio }
                    Column(modifier = Modifier.fillMaxWidth()) {
                        HorizontalDivider(
                            color = HeistPalette.neutralMid,
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                        ) {
                            Text("Total:", color = HeistPalette.text, fontWeight = FontWeight.Bold)
                            Text(
                                "$total €",
                                color = HeistPalette.positiveGreen,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = onCheckout,
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(containerColor = HeistPalette.positiveGreen),
                        ) {
                            Text("Confirmar Pedido")
                        }
                    }
                }
            }
        }
    }
}
