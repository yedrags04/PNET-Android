package com.heistcorp.heistcraft.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.heistcorp.heistcraft.data.ReservaApi
import com.heistcorp.heistcraft.data.ReservaCreateBody
import com.heistcorp.heistcraft.ui.theme.HeistPalette
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormularioReservaDialog(
    bankId: String,
    bankLabel: String,
    editing: ReservaApi?,
    onDismiss: () -> Unit,
    onConfirm: (ReservaCreateBody, editingReservaId: String?) -> Unit,
) {
    key(editing?.id, bankId) {
        FormularioReservaDialogContent(
            bankId = bankId,
            bankLabel = bankLabel,
            editing = editing,
            onDismiss = onDismiss,
            onConfirm = onConfirm,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FormularioReservaDialogContent(
    bankId: String,
    bankLabel: String,
    editing: ReservaApi?,
    onDismiss: () -> Unit,
    onConfirm: (ReservaCreateBody, editingReservaId: String?) -> Unit,
) {
    val zone = ZoneId.systemDefault()

    var nombreLider by remember { mutableStateOf("") }
    var emailLider by remember { mutableStateOf("") }
    var experiencia by remember { mutableStateOf("") }
    var tamanoEquipo by remember { mutableStateOf("") }
    var fechaIso by remember { mutableStateOf("") }
    var horaTexto by remember { mutableStateOf("") }
    var riskLevelApi by remember { mutableStateOf("") }
    var expandirRiesgo by remember { mutableStateOf(false) }
    var presupuesto by remember { mutableStateOf("") }
    var equipoNecesario by remember { mutableStateOf("") }
    var planOperacion by remember { mutableStateOf("") }
    var terminosAceptados by remember { mutableStateOf(false) }

    var espHacking by remember { mutableStateOf(false) }
    var espDriving by remember { mutableStateOf(false) }
    var espExplosives by remember { mutableStateOf(false) }
    var espNegotiation by remember { mutableStateOf(false) }
    var espCombat by remember { mutableStateOf(false) }
    var espLogistics by remember { mutableStateOf(false) }

    val datePickerState = rememberDatePickerState()
    var showDatePicker by remember { mutableStateOf(false) }

    val timeParts = editing?.operationTime?.split(":")
    val initialHour = timeParts?.getOrNull(0)?.toIntOrNull() ?: 12
    val initialMinute = timeParts?.getOrNull(1)?.toIntOrNull() ?: 0
    val timePickerState =
        rememberTimePickerState(
            initialHour = initialHour,
            initialMinute = initialMinute,
            is24Hour = true,
        )
    var showTimePicker by remember { mutableStateOf(false) }

    LaunchedEffect(editing?.id, bankId) {
        if (editing != null) {
            nombreLider = editing.leaderName.orEmpty()
            emailLider = editing.leaderEmail.orEmpty()
            experiencia = editing.experience?.toString().orEmpty()
            tamanoEquipo = editing.teamSize?.toString().orEmpty()
            fechaIso = editing.operationDate.orEmpty()
            horaTexto = editing.operationTime.orEmpty()
            riskLevelApi = editing.riskLevel.orEmpty()
            presupuesto = editing.budget?.toString().orEmpty()
            equipoNecesario = editing.equipment.orEmpty()
            planOperacion = editing.plan.orEmpty()
            terminosAceptados = true
            val sp = editing.specialties.orEmpty()
            espHacking = sp.contains("hacking")
            espDriving = sp.contains("driving")
            espExplosives = sp.contains("explosives")
            espNegotiation = sp.contains("negotiation")
            espCombat = sp.contains("combat")
            espLogistics = sp.contains("logistics")
        } else {
            nombreLider = ""
            emailLider = ""
            experiencia = ""
            tamanoEquipo = ""
            fechaIso = ""
            horaTexto = ""
            riskLevelApi = ""
            presupuesto = ""
            equipoNecesario = ""
            planOperacion = ""
            terminosAceptados = false
            espHacking = false
            espDriving = false
            espExplosives = false
            espNegotiation = false
            espCombat = false
            espLogistics = false
        }
    }

    val fechaDisplay =
        remember(fechaIso) {
            if (fechaIso.isBlank()) {
                ""
            } else {
                runCatching {
                    LocalDate.parse(fechaIso)
                        .format(DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale.getDefault()))
                }.getOrDefault(fechaIso)
            }
        }

    val riskLabel =
        when (riskLevelApi) {
            "bajo" -> "Bajo - Operación Segura"
            "medio" -> "Medio - Riesgo Moderado"
            "alto" -> "Alto - Muy Peligroso"
            else -> ""
        }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false),
    ) {
        Surface(
            modifier =
                Modifier
                    .fillMaxWidth(0.95f)
                    .fillMaxHeight(0.92f),
            shape = MaterialTheme.shapes.large,
            color = HeistPalette.card,
        ) {
            Column(
                modifier =
                    Modifier
                        .padding(16.dp)
                        .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                Text(
                    text = "Formulario de Operación",
                    style = MaterialTheme.typography.headlineSmall,
                    color = HeistPalette.text,
                    fontWeight = FontWeight.Bold,
                )
                Text(
                    text = "Completa los detalles para confirmar tu reserva",
                    color = HeistPalette.muted,
                    style = MaterialTheme.typography.bodySmall,
                )

                HorizontalDivider(color = HeistPalette.divider)

                SeccionTitulo("Información del Líder")

                OutlinedTextField(
                    value = nombreLider,
                    onValueChange = { nombreLider = it },
                    label = { Text("Nombre del Líder *") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    colors = fieldColors(),
                )
                OutlinedTextField(
                    value = emailLider,
                    onValueChange = { emailLider = it },
                    label = { Text("Email de Contacto *") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    singleLine = true,
                    colors = fieldColors(),
                )
                OutlinedTextField(
                    value = experiencia,
                    onValueChange = { experiencia = it },
                    label = { Text("Años de Experiencia *") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    colors = fieldColors(),
                )

                SeccionTitulo("Información de la Operación")

                OutlinedTextField(
                    value = bankLabel,
                    onValueChange = { },
                    label = { Text("Banco Objetivo *") },
                    modifier = Modifier.fillMaxWidth(),
                    readOnly = true,
                    enabled = false,
                    colors = fieldColors(),
                )

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Box(modifier = Modifier
                        .weight(1f)
                        .clickable { showDatePicker = true }) {
                        OutlinedTextField(
                            value = fechaDisplay,
                            onValueChange = { },
                            label = { Text("Fecha *") },
                            modifier = Modifier.fillMaxWidth(),
                            readOnly = true,
                            enabled = false,
                            placeholder = { Text("AAAA-MM-DD") },
                            colors = fieldColors(),
                        )
                    }
                    Box(modifier = Modifier
                        .weight(1f)
                        .clickable { showTimePicker = true }) {
                        OutlinedTextField(
                            value = horaTexto,
                            onValueChange = { },
                            label = { Text("Hora *") },
                            modifier = Modifier.fillMaxWidth(),
                            readOnly = true,
                            enabled = false,
                            placeholder = { Text("HH:MM") },
                            colors = fieldColors(),
                        )
                    }
                }

                OutlinedTextField(
                    value = tamanoEquipo,
                    onValueChange = { tamanoEquipo = it },
                    label = { Text("Miembros del Equipo *") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    colors = fieldColors(),
                )

                SeccionTitulo("Detalles Operacionales")

                Box(modifier = Modifier.fillMaxWidth()) {
                    OutlinedTextField(
                        value = if (riskLevelApi.isEmpty()) "Selecciona un nivel" else riskLabel,
                        onValueChange = {},
                        label = { Text("Nivel de Riesgo Aceptado *") },
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .clickable { expandirRiesgo = true },
                        readOnly = true,
                        enabled = false,
                        trailingIcon = {
                            Icon(
                                Icons.Filled.ArrowDropDown,
                                contentDescription = null
                            )
                        },
                        colors = fieldColors(),
                    )
                    DropdownMenu(
                        expanded = expandirRiesgo,
                        onDismissRequest = { expandirRiesgo = false },
                        modifier = Modifier.background(HeistPalette.card),
                    ) {
                        listOf(
                            "bajo" to "Bajo - Operación Segura",
                            "medio" to "Medio - Riesgo Moderado",
                            "alto" to "Alto - Muy Peligroso",
                        ).forEach { (api, label) ->
                            DropdownMenuItem(
                                text = { Text(label, color = HeistPalette.text) },
                                onClick = {
                                    riskLevelApi = api
                                    expandirRiesgo = false
                                },
                            )
                        }
                    }
                }

                OutlinedTextField(
                    value = presupuesto,
                    onValueChange = { presupuesto = it },
                    label = { Text("Presupuesto Disponible (€) *") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    singleLine = true,
                    colors = fieldColors(),
                )
                OutlinedTextField(
                    value = equipoNecesario,
                    onValueChange = { equipoNecesario = it },
                    label = { Text("Equipo Necesario *") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 3,
                    maxLines = 4,
                    colors = fieldColors(),
                )
                OutlinedTextField(
                    value = planOperacion,
                    onValueChange = { planOperacion = it },
                    label = { Text("Plan de Operación *") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 4,
                    maxLines = 6,
                    colors = fieldColors(),
                )

                SeccionTitulo("Especialidades del Equipo")

                Column {
                    Row(modifier = Modifier.fillMaxWidth()) {
                        CheckboxConTexto(
                            "Hacking / Sistemas",
                            espHacking,
                            { espHacking = it },
                            Modifier.weight(1f)
                        )
                        CheckboxConTexto(
                            "Conducción",
                            espDriving,
                            { espDriving = it },
                            Modifier.weight(1f)
                        )
                    }
                    Row(modifier = Modifier.fillMaxWidth()) {
                        CheckboxConTexto(
                            "Explosivos",
                            espExplosives,
                            { espExplosives = it },
                            Modifier.weight(1f)
                        )
                        CheckboxConTexto(
                            "Infiltración",
                            espNegotiation,
                            { espNegotiation = it },
                            Modifier.weight(1f)
                        )
                    }
                    Row(modifier = Modifier.fillMaxWidth()) {
                        CheckboxConTexto(
                            "Combate",
                            espCombat,
                            { espCombat = it },
                            Modifier.weight(1f)
                        )
                        CheckboxConTexto(
                            "Logística",
                            espLogistics,
                            { espLogistics = it },
                            Modifier.weight(1f)
                        )
                    }
                }

                HorizontalDivider(color = HeistPalette.divider)

                CheckboxConTexto(
                    texto = "Asumo los riesgos de esta operación *",
                    checked = terminosAceptados,
                    onCheckedChange = { terminosAceptados = it },
                )

                val expOk = experiencia.toIntOrNull() != null
                val teamOk = (tamanoEquipo.toIntOrNull() ?: 0) >= 1
                val budgetOk = presupuesto.toDoubleOrNull() != null
                val formularioValido =
                    nombreLider.isNotBlank() &&
                            emailLider.isNotBlank() &&
                            fechaIso.isNotBlank() &&
                            horaTexto.isNotBlank() &&
                            riskLevelApi.isNotBlank() &&
                            equipoNecesario.isNotBlank() &&
                            planOperacion.isNotBlank() &&
                            terminosAceptados &&
                            expOk &&
                            teamOk &&
                            budgetOk

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("Cancelar", color = HeistPalette.muted)
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = {
                            val specialties =
                                buildList {
                                    if (espHacking) add("hacking")
                                    if (espDriving) add("driving")
                                    if (espExplosives) add("explosives")
                                    if (espNegotiation) add("negotiation")
                                    if (espCombat) add("combat")
                                    if (espLogistics) add("logistics")
                                }
                            val body =
                                ReservaCreateBody(
                                    leaderName = nombreLider.trim(),
                                    leaderEmail = emailLider.trim(),
                                    experience = experiencia.toIntOrNull() ?: 0,
                                    bankId = bankId,
                                    operationDate = fechaIso,
                                    operationTime = horaTexto,
                                    teamSize = tamanoEquipo.toIntOrNull() ?: 1,
                                    riskLevel = riskLevelApi,
                                    budget = presupuesto.toDoubleOrNull() ?: 0.0,
                                    equipment = equipoNecesario.trim(),
                                    plan = planOperacion.trim(),
                                    specialties = specialties,
                                    status = "pendiente",
                                    createdAt =
                                        editing?.createdAt
                                            ?: Instant.now().toString(),
                                )
                            onConfirm(body, editing?.id)
                        },
                        enabled = formularioValido,
                        colors =
                            ButtonDefaults.buttonColors(
                                containerColor = HeistPalette.positiveGreen,
                                disabledContainerColor = HeistPalette.divider,
                            ),
                    ) {
                        Text("Confirmar y Reservar")
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
                            val localDate =
                                Instant.ofEpochMilli(millis).atZone(zone).toLocalDate()
                            fechaIso = localDate.format(DateTimeFormatter.ISO_LOCAL_DATE)
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

    if (showTimePicker) {
        Dialog(onDismissRequest = { showTimePicker = false }) {
            Surface(
                shape = MaterialTheme.shapes.extraLarge,
                color = HeistPalette.card,
                modifier = Modifier.padding(16.dp),
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text(
                        "Selecciona la hora",
                        color = HeistPalette.text,
                        modifier = Modifier.padding(bottom = 20.dp)
                    )
                    TimePicker(state = timePickerState)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        TextButton(onClick = { showTimePicker = false }) {
                            Text("Cancelar")
                        }
                        TextButton(
                            onClick = {
                                horaTexto =
                                    String.format(
                                        Locale.getDefault(),
                                        "%02d:%02d",
                                        timePickerState.hour,
                                        timePickerState.minute,
                                    )
                                showTimePicker = false
                            },
                        ) {
                            Text("Confirmar")
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun fieldColors() =
    OutlinedTextFieldDefaults.colors(
        focusedTextColor = HeistPalette.text,
        unfocusedTextColor = HeistPalette.text,
        focusedLabelColor = HeistPalette.muted,
        unfocusedLabelColor = HeistPalette.muted,
        cursorColor = HeistPalette.text,
    )

@Composable
fun SeccionTitulo(titulo: String) {
    Text(
        text = titulo,
        color = HeistPalette.amber,
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(top = 8.dp, bottom = 4.dp),
    )
}

@Composable
fun CheckboxConTexto(
    texto: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier =
            modifier
                .clip(MaterialTheme.shapes.small)
                .clickable { onCheckedChange(!checked) }
                .padding(end = 8.dp, top = 4.dp, bottom = 4.dp),
    ) {
        Checkbox(
            checked = checked,
            onCheckedChange = onCheckedChange,
        )
        Text(
            text = texto,
            color = HeistPalette.text,
            style = MaterialTheme.typography.bodyMedium,
        )
    }
}
