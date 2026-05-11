package com.heistcorp.heistcraft.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

// 1. Modelo de Datos
data class Banco(
    val id: Int,
    val nombre: String,
    val direccion: String,
    val dificultad: String,
    val recompensa: Int,
    val disponible: Boolean
)

@Composable
fun BancosScreen(modifier: Modifier = Modifier) {
    // Estados para los modales
    var bancoSeleccionado by remember { mutableStateOf<Banco?>(null) }
    var mostrarFormulario by remember { mutableStateOf(false) }

    // Estado para los filtros
    var mostrarFiltros by remember { mutableStateOf(false) }
    var filtroLocalizacion by remember { mutableStateOf("") }
    var filtroRecompensa by remember { mutableFloatStateOf(1000f) }

    // Datos simulados
    val bancosDisponibles = remember {
        List(10) { index ->
            Banco(
                id = index,
                nombre = "Banco ${index + 1}",
                direccion = "Centro Ciudad ${index + 1}",
                dificultad = if (index % 2 == 0) "Media" else "Alta",
                recompensa = 500 + (index * 100),
                disponible = index % 3 != 0
            )
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF131722))
            .padding(16.dp)
    ) {
        // Título principal
        Text(
            text = "Bancos disponibles",
            style = MaterialTheme.typography.headlineMedium,
            color = Color.White,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Botón Toggle Filtros
        Surface(
            color = Color(0xFF1E2333),
            shape = MaterialTheme.shapes.medium,
            modifier = Modifier
                .fillMaxWidth()
                .clickable { mostrarFiltros = !mostrarFiltros }
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Filtros", color = Color.White)
                Icon(
                    imageVector = if (mostrarFiltros) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                    contentDescription = "Alternar filtros",
                    tint = Color.White
                )
            }
        }

        // Contenido de los Filtros
        AnimatedVisibility(visible = mostrarFiltros) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedTextField(
                    value = filtroLocalizacion,
                    onValueChange = { filtroLocalizacion = it },
                    label = { Text("Localización (Ej: Madrid)") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    )
                )

                Column {
                    Text("Dinero a ganar: ${filtroRecompensa.toInt()} €", color = Color.White)
                    Slider(
                        value = filtroRecompensa,
                        onValueChange = { filtroRecompensa = it },
                        valueRange = 0f..1000f
                    )
                }

                Button(
                    onClick = {
                        filtroLocalizacion = ""
                        filtroRecompensa = 1000f
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.DarkGray)
                ) {
                    Text("Quitar filtros")
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Grid de Tarjetas
        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 160.dp),
            contentPadding = PaddingValues(bottom = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(bancosDisponibles) { banco ->
                BancoCard(banco = banco) {
                    bancoSeleccionado = banco
                }
            }
        }
    }

    // Modal de Detalles del Banco
    if (bancoSeleccionado != null && !mostrarFormulario) {
        DetalleBancoDialog(
            banco = bancoSeleccionado!!,
            onDismiss = { bancoSeleccionado = null },
            onReservar = { mostrarFormulario = true }
        )
    }

    // Modal del Formulario de Reserva
    if (mostrarFormulario && bancoSeleccionado != null) {
        FormularioReservaDialog(
            bancoDestino = bancoSeleccionado!!.nombre,
            onDismiss = {
                mostrarFormulario = false
                bancoSeleccionado = null
            },
            onSubmit = {
                mostrarFormulario = false
                bancoSeleccionado = null
            }
        )
    }
}

@Composable
fun BancoCard(banco: Banco, onClick: () -> Unit) {
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        colors = CardDefaults.elevatedCardColors(containerColor = Color(0xFF1E2333))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .background(Color.DarkGray)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = banco.nombre, color = Color.White, fontWeight = FontWeight.Bold)
            Text(text = "Dificultad: ${banco.dificultad}", color = Color.LightGray, style = MaterialTheme.typography.bodySmall)
            Text(text = "${banco.recompensa} €", color = Color(0xFF4CAF50), fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun DetalleBancoDialog(banco: Banco, onDismiss: () -> Unit, onReservar: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = Color(0xFF1E2333),
        titleContentColor = Color.White,
        textContentColor = Color.LightGray,
        title = { Text(text = banco.nombre) },
        text = {
            Column {
                Text(text = "Dirección: ${banco.direccion}")
                Text(text = "Dificultad: ${banco.dificultad}")
                Text(text = "Recompensa: ${banco.recompensa} EUR", color = Color(0xFF4CAF50))
                Text(text = "Disponibilidad: ${if (banco.disponible) "Sí" else "Ocupado"}")
            }
        },
        confirmButton = {
            Button(onClick = onReservar) {
                Text(text = "RESERVAR")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(text = "CANCELAR", color = Color.Red)
            }
        }
    )
}

// --- FORMULARIO INTEGRADO (Solo hay UNO) ---
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormularioReservaDialog(
    bancoDestino: String,
    onDismiss: () -> Unit,
    onSubmit: () -> Unit
) {
    // --- ESTADOS MOVIDOS AQUÍ ARRIBA PARA QUE TODO EL CÓDIGO PUEDA VERLOS ---
    val datePickerState = rememberDatePickerState()
    var showDatePicker by remember { mutableStateOf(false) }

    val timePickerState = rememberTimePickerState()
    var showTimePicker by remember { mutableStateOf(false) }

    // ¡AQUÍ ESTÁ LA SOLUCIÓN! Las declaramos al principio
    var fechaTexto by remember { mutableStateOf("") }
    var horaTexto by remember { mutableStateOf("") }

    val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .fillMaxHeight(0.9f),
            shape = MaterialTheme.shapes.large,
            color = Color(0xFF1E2333)
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Cabecera del formulario
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Formulario de Operación",
                            style = MaterialTheme.typography.headlineSmall,
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Completa los detalles para confirmar tu reserva",
                            color = Color.LightGray,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }

                HorizontalDivider(color = Color.DarkGray)

                // --- FIELDSET 1: Información del Líder ---
                SeccionTitulo("Información del Líder")

                var nombreLider by remember { mutableStateOf("") }
                var emailLider by remember { mutableStateOf("") }
                var experiencia by remember { mutableStateOf("") }

                OutlinedTextField(
                    value = nombreLider,
                    onValueChange = { nombreLider = it },
                    label = { Text("Nombre del Líder *") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                OutlinedTextField(
                    value = emailLider,
                    onValueChange = { emailLider = it },
                    label = { Text("Email de Contacto *") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    singleLine = true
                )
                OutlinedTextField(
                    value = experiencia,
                    onValueChange = { experiencia = it },
                    label = { Text("Años de Experiencia *") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true
                )

                // --- FIELDSET 2: Información de la Operación ---
                SeccionTitulo("Información de la Operación")

                var tamanoEquipo by remember { mutableStateOf("") }

                OutlinedTextField(
                    value = bancoDestino,
                    onValueChange = { },
                    label = { Text("Banco Objetivo *") },
                    modifier = Modifier.fillMaxWidth(),
                    readOnly = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        disabledTextColor = Color.White,
                        disabledBorderColor = Color.Gray
                    ),
                    enabled = false
                )
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    // Campo Fecha Interactivo
                    Box(modifier = Modifier.weight(1f).clickable { showDatePicker = true }) {
                        OutlinedTextField(
                            value = fechaTexto,
                            onValueChange = { },
                            label = { Text("Fecha *") },
                            modifier = Modifier.fillMaxWidth(),
                            readOnly = true,
                            enabled = false,
                            placeholder = { Text("DD/MM/AAAA") },
                            colors = OutlinedTextFieldDefaults.colors(
                                disabledTextColor = Color.White,
                                disabledBorderColor = Color.Gray,
                                disabledLabelColor = Color.LightGray,
                                disabledPlaceholderColor = Color.Gray
                            )
                        )
                    }
                    // Campo Hora Interactivo
                    Box(modifier = Modifier.weight(1f).clickable { showTimePicker = true }) {
                        OutlinedTextField(
                            value = horaTexto,
                            onValueChange = { },
                            label = { Text("Hora *") },
                            modifier = Modifier.fillMaxWidth(),
                            readOnly = true,
                            enabled = false,
                            placeholder = { Text("HH:MM") },
                            colors = OutlinedTextFieldDefaults.colors(
                                disabledTextColor = Color.White,
                                disabledBorderColor = Color.Gray,
                                disabledLabelColor = Color.LightGray,
                                disabledPlaceholderColor = Color.Gray
                            )
                        )
                    }
                }
                OutlinedTextField(
                    value = tamanoEquipo,
                    onValueChange = { tamanoEquipo = it },
                    label = { Text("Miembros del Equipo *") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true
                )

                // --- FIELDSET 3: Detalles Operacionales ---
                SeccionTitulo("Detalles Operacionales")

                var nivelRiesgo by remember { mutableStateOf("Selecciona un nivel") }
                var expandirRiesgo by remember { mutableStateOf(false) }
                var presupuesto by remember { mutableStateOf("") }
                var equipoNecesario by remember { mutableStateOf("") }
                var planOperacion by remember { mutableStateOf("") }

                Box(modifier = Modifier.fillMaxWidth()) {
                    OutlinedTextField(
                        value = nivelRiesgo,
                        onValueChange = {},
                        label = { Text("Nivel de Riesgo Aceptado *") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { expandirRiesgo = true },
                        readOnly = true,
                        enabled = false,
                        trailingIcon = {
                            Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            disabledTextColor = Color.White,
                            disabledBorderColor = Color.Gray,
                            disabledLabelColor = Color.LightGray,
                            disabledTrailingIconColor = Color.White
                        )
                    )
                    DropdownMenu(
                        expanded = expandirRiesgo,
                        onDismissRequest = { expandirRiesgo = false },
                        modifier = Modifier.background(Color(0xFF1E2333))
                    ) {
                        val opciones = listOf("Bajo - Operación Segura", "Medio - Riesgo Moderado", "Alto - Muy Peligroso")
                        opciones.forEach { seleccion ->
                            DropdownMenuItem(
                                text = { Text(seleccion, color = Color.White) },
                                onClick = {
                                    nivelRiesgo = seleccion
                                    expandirRiesgo = false
                                }
                            )
                        }
                    }
                }

                OutlinedTextField(
                    value = presupuesto,
                    onValueChange = { presupuesto = it },
                    label = { Text("Presupuesto Disponible (€) *") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true
                )
                OutlinedTextField(
                    value = equipoNecesario,
                    onValueChange = { equipoNecesario = it },
                    label = { Text("Equipo Necesario *") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 3,
                    maxLines = 4
                )
                OutlinedTextField(
                    value = planOperacion,
                    onValueChange = { planOperacion = it },
                    label = { Text("Plan de Operación *") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 4,
                    maxLines = 6
                )

                // --- FIELDSET 4: Especialidades del Equipo ---
                SeccionTitulo("Especialidades del Equipo")

                var espHacking by remember { mutableStateOf(false) }
                var espConduccion by remember { mutableStateOf(false) }
                var espExplosivos by remember { mutableStateOf(false) }
                var espInfiltracion by remember { mutableStateOf(false) }
                var espCombate by remember { mutableStateOf(false) }
                var espLogistica by remember { mutableStateOf(false) }

                Column {
                    Row(modifier = Modifier.fillMaxWidth()) {
                        CheckboxConTexto("Hacking", espHacking, { espHacking = it }, Modifier.weight(1f))
                        CheckboxConTexto("Conducción", espConduccion, { espConduccion = it }, Modifier.weight(1f))
                    }
                    Row(modifier = Modifier.fillMaxWidth()) {
                        CheckboxConTexto("Explosivos", espExplosivos, { espExplosivos = it }, Modifier.weight(1f))
                        CheckboxConTexto("Infiltración", espInfiltracion, { espInfiltracion = it }, Modifier.weight(1f))
                    }
                    Row(modifier = Modifier.fillMaxWidth()) {
                        CheckboxConTexto("Combate", espCombate, { espCombate = it }, Modifier.weight(1f))
                        CheckboxConTexto("Logística", espLogistica, { espLogistica = it }, Modifier.weight(1f))
                    }
                }

                // --- FIELDSET 5: Confirmación ---
                HorizontalDivider(color = Color.DarkGray)
                var terminosAceptados by remember { mutableStateOf(false) }

                CheckboxConTexto(
                    texto = "Asumo los riesgos de esta operación *",
                    checked = terminosAceptados,
                    onCheckedChange = { terminosAceptados = it }
                )

                // --- BOTONES ---
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("Cancelar", color = Color.LightGray)
                    }
                    Spacer(modifier = Modifier.width(8.dp))

                    val formularioValido = nombreLider.isNotBlank() &&
                            emailLider.isNotBlank() &&
                            fechaTexto.isNotBlank() &&
                            horaTexto.isNotBlank() &&
                            terminosAceptados &&
                            nivelRiesgo != "Selecciona un nivel"

                    Button(
                        onClick = onSubmit,
                        enabled = formularioValido,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF4CAF50),
                            disabledContainerColor = Color.DarkGray
                        )
                    ) {
                        Text("Confirmar y Reservar")
                    }
                }
            }
        }
    }

    // --- LÓGICA DEL CALENDARIO (DatePicker) ---
    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let { millis ->
                        fechaTexto = formatter.format(Calendar.getInstance().apply {
                            timeInMillis = millis
                        }.time)
                    }
                    showDatePicker = false
                }) { Text("OK") }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) { Text("Cancelar") }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    // --- LÓGICA DEL RELOJ (TimePicker) ---
    if (showTimePicker) {
        Dialog(onDismissRequest = { showTimePicker = false }) {
            Surface(
                shape = MaterialTheme.shapes.extraLarge,
                color = Color(0xFF1E2333),
                modifier = Modifier.padding(16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Selecciona la hora", color = Color.White, modifier = Modifier.padding(bottom = 20.dp))
                    TimePicker(state = timePickerState)
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                        TextButton(onClick = { showTimePicker = false }) { Text("Cancelar") }
                        TextButton(onClick = {
                            horaTexto = String.format(Locale.getDefault(), "%02d:%02d", timePickerState.hour, timePickerState.minute)
                            showTimePicker = false
                        }) { Text("Confirmar") }
                    }
                }
            }
        }
    }
}

// --- COMPONENTES DE AYUDA PARA REUTILIZAR CÓDIGO ---

@Composable
fun SeccionTitulo(titulo: String) {
    Text(
        text = titulo,
        color = Color(0xFFFFC107),
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(top = 8.dp, bottom = 4.dp)
    )
}

@Composable
fun CheckboxConTexto(
    texto: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .clip(MaterialTheme.shapes.small)
            .clickable { onCheckedChange(!checked) }
            .padding(end = 8.dp, top = 4.dp, bottom = 4.dp)
    ) {
        Checkbox(
            checked = checked,
            onCheckedChange = onCheckedChange
        )
        Text(
            text = texto,
            color = Color.White,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}