package com.heistcorp.heistcraft.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.heistcorp.heistcraft.data.AnimalApi
import com.heistcorp.heistcraft.data.AnimalCreateBody
import com.heistcorp.heistcraft.network.ApiClient
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnimalesScreen(onBack: () -> Unit) {
    val context = LocalContext.current
    val api = remember { ApiClient.heistApi }
    val scope = rememberCoroutineScope()
    var lista by remember { mutableStateOf<List<AnimalApi>>(emptyList()) }
    var mostrarAlta by remember { mutableStateOf(false) }
    var editar by remember { mutableStateOf<AnimalApi?>(null) }
    var borrar by remember { mutableStateOf<AnimalApi?>(null) }

    fun recargar() {
        scope.launch {
            try {
                lista = api.getAnimales()
            } catch (e: Exception) {
                Toast.makeText(context, e.message ?: "Error", Toast.LENGTH_LONG).show()
            }
        }
    }

    LaunchedEffect(Unit) {
        recargar()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Animales (API MongoDB)") },
                colors =
                    TopAppBarDefaults.topAppBarColors(
                        containerColor = Color(0xFF1E2333),
                        titleContentColor = Color.White,
                        navigationIconContentColor = Color.White,
                    ),
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                },
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { mostrarAlta = true },
                containerColor = Color(0xFF4CAF50),
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Añadir animal", tint = Color.White)
            }
        },
    ) { padding ->
        LazyColumn(
            modifier =
                Modifier
                    .fillMaxSize()
                    .background(Color(0xFF131722))
                    .padding(padding),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            items(lista, key = { it.id }) { a ->
                Card(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 4.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF1E2333)),
                ) {
                    Row(
                        Modifier.padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Column(Modifier.weight(1f)) {
                            Text(a.nombre, color = Color.White, fontWeight = FontWeight.Bold)
                            Text(a.especie, color = Color.LightGray, style = MaterialTheme.typography.bodySmall)
                            Text(
                                "Edad: ${a.edad}  Habitat: ${a.habitat.ifBlank { "—" }}  En peligro: ${if (a.enPeligro) "Sí" else "No"}",
                                color = Color(0xFF4CAF50),
                                style = MaterialTheme.typography.bodySmall,
                            )
                        }
                        TextButton(onClick = { editar = a }) { Text("Editar") }
                        TextButton(onClick = { borrar = a }) { Text("Borrar", color = Color(0xFFFF5252)) }
                    }
                }
            }
        }
    }

    if (mostrarAlta) {
        AnimalFormDialog(
            titulo = "Nuevo animal",
            inicial = null,
            onDismiss = { mostrarAlta = false },
            onGuardar = { body ->
                scope.launch {
                    try {
                        api.createAnimal(body)
                        Toast.makeText(context, "Animal creado", Toast.LENGTH_SHORT).show()
                        mostrarAlta = false
                        recargar()
                    } catch (e: Exception) {
                        Toast.makeText(context, e.message ?: "Error", Toast.LENGTH_LONG).show()
                    }
                }
            },
        )
    }

    if (editar != null) {
        val e = editar!!
        AnimalFormDialog(
            titulo = "Editar animal #${e.id}",
            inicial = e,
            onDismiss = { editar = null },
            onGuardar = { body ->
                scope.launch {
                    try {
                        api.updateAnimal(e.id, body)
                        Toast.makeText(context, "Animal actualizado", Toast.LENGTH_SHORT).show()
                        editar = null
                        recargar()
                    } catch (ex: Exception) {
                        Toast.makeText(context, ex.message ?: "Error", Toast.LENGTH_LONG).show()
                    }
                }
            },
        )
    }

    if (borrar != null) {
        val b = borrar!!
        AlertDialog(
            onDismissRequest = { borrar = null },
            title = { Text("Eliminar ${b.nombre}?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        scope.launch {
                            try {
                                api.deleteAnimal(b.id)
                                Toast.makeText(context, "Eliminado", Toast.LENGTH_SHORT).show()
                                borrar = null
                                recargar()
                            } catch (ex: Exception) {
                                Toast.makeText(context, ex.message ?: "Error", Toast.LENGTH_LONG).show()
                            }
                        }
                    },
                ) {
                    Text("Eliminar", color = Color.Red)
                }
            },
            dismissButton = {
                TextButton(onClick = { borrar = null }) {
                    Text("Cancelar")
                }
            },
        )
    }
}

@Composable
private fun AnimalFormDialog(
    titulo: String,
    inicial: AnimalApi?,
    onDismiss: () -> Unit,
    onGuardar: (AnimalCreateBody) -> Unit,
) {
    var nombre by remember { mutableStateOf(inicial?.nombre ?: "") }
    var especie by remember { mutableStateOf(inicial?.especie ?: "") }
    var edad by remember { mutableStateOf(inicial?.edad?.toString() ?: "") }
    var habitat by remember { mutableStateOf(inicial?.habitat ?: "") }
    var enPeligro by remember { mutableStateOf(inicial?.enPeligro ?: false) }

    LaunchedEffect(inicial?.id) {
        nombre = inicial?.nombre ?: ""
        especie = inicial?.especie ?: ""
        edad = inicial?.edad?.toString() ?: ""
        habitat = inicial?.habitat ?: ""
        enPeligro = inicial?.enPeligro ?: false
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(titulo) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(value = nombre, onValueChange = { nombre = it }, label = { Text("Nombre") })
                OutlinedTextField(value = especie, onValueChange = { especie = it }, label = { Text("Especie") })
                OutlinedTextField(value = edad, onValueChange = { edad = it }, label = { Text("Edad") })
                OutlinedTextField(value = habitat, onValueChange = { habitat = it }, label = { Text("Habitat") })
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(checked = enPeligro, onCheckedChange = { enPeligro = it })
                    Text("En peligro de extinción", color = Color.Black)
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    val e = edad.toIntOrNull()
                    if (nombre.isNotBlank() && especie.isNotBlank() && e != null) {
                        onGuardar(
                            AnimalCreateBody(
                                nombre = nombre.trim(),
                                especie = especie.trim(),
                                edad = e,
                                habitat = habitat.trim(),
                                enPeligro = enPeligro,
                            ),
                        )
                    }
                },
            ) {
                Text("Guardar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        },
    )
}
