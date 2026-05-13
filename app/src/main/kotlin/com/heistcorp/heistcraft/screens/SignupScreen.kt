package com.heistcorp.heistcraft.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.heistcorp.heistcraft.ui.theme.HeistPalette
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.heistcorp.heistcraft.R
import com.heistcorp.heistcraft.auth.AppAuth
import com.heistcorp.heistcraft.auth.UserProfile
import com.heistcorp.heistcraft.auth.UserSession

private val fieldColors
    @Composable
    get() =
        OutlinedTextFieldDefaults.colors(
            focusedTextColor = HeistPalette.text,
            unfocusedTextColor = HeistPalette.text,
            focusedLabelColor = HeistPalette.amber,
            unfocusedLabelColor = HeistPalette.muted,
        )

@Composable
fun SignupScreen(
    onBack: () -> Unit,
    onNavigateToLogin: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var fullName by remember { mutableStateOf("") }
    var nickname by remember { mutableStateOf("") }
    var yearsText by remember { mutableStateOf("") }
    var mainSkill by remember { mutableStateOf("") }
    var otherSkills by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirm by remember { mutableStateOf("") }
    var registerError by remember { mutableStateOf<Int?>(null) }

    val yearsInt = yearsText.toIntOrNull()
    val yearsOk = yearsInt != null && yearsInt in 0..80
    val match = password.isNotBlank() && password == confirm
    val canSubmit =
        fullName.isNotBlank() &&
            nickname.isNotBlank() &&
            mainSkill.isNotBlank() &&
            email.isNotBlank() &&
            yearsOk &&
            match

    Column(
        modifier =
            modifier
                .fillMaxSize()
                .background(HeistPalette.screenBackground)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        IconButton(onClick = onBack) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver", tint = HeistPalette.text)
        }
        Text("Crear cuenta", style = MaterialTheme.typography.headlineMedium, color = HeistPalette.text)
        Text(
            stringResource(R.string.registro_subtitulo),
            style = MaterialTheme.typography.bodyMedium,
            color = HeistPalette.muted,
        )
        OutlinedTextField(
            value = fullName,
            onValueChange = { fullName = it },
            label = { Text(stringResource(R.string.field_nombre_completo)) },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            colors = fieldColors,
        )
        OutlinedTextField(
            value = nickname,
            onValueChange = { nickname = it },
            label = { Text(stringResource(R.string.field_apodo)) },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            colors = fieldColors,
        )
        OutlinedTextField(
            value = yearsText,
            onValueChange = { v ->
                if (v.length <= 2 && v.all { it.isDigit() }) yearsText = v
            },
            label = { Text(stringResource(R.string.field_anos_experiencia)) },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            supportingText = {
                if (yearsText.isNotEmpty() && !yearsOk) {
                    Text(stringResource(R.string.field_anos_experiencia_ayuda), color = MaterialTheme.colorScheme.error)
                }
            },
            isError = yearsText.isNotEmpty() && !yearsOk,
            colors = fieldColors,
        )
        OutlinedTextField(
            value = mainSkill,
            onValueChange = { mainSkill = it },
            label = { Text(stringResource(R.string.field_habilidad_principal)) },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            colors = fieldColors,
        )
        OutlinedTextField(
            value = otherSkills,
            onValueChange = { otherSkills = it },
            label = { Text(stringResource(R.string.field_otras_habilidades)) },
            modifier = Modifier.fillMaxWidth(),
            minLines = 2,
            maxLines = 4,
            colors = fieldColors,
        )
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text(stringResource(R.string.field_correo)) },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            colors = fieldColors,
        )
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text(stringResource(R.string.field_contrasena)) },
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
            colors = fieldColors,
        )
        OutlinedTextField(
            value = confirm,
            onValueChange = { confirm = it },
            label = { Text(stringResource(R.string.field_confirmar_contrasena)) },
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
            colors = fieldColors,
        )
        registerError?.let { errResId ->
            Text(
                text = stringResource(id = errResId),
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall
            )
        }
        Button(
            onClick = {
                registerError = null
                val y = yearsInt ?: return@Button
                val profile =
                    UserProfile(
                        fullName = fullName.trim(),
                        nickname = nickname.trim(),
                        yearsExperience = y,
                        mainSkill = mainSkill.trim(),
                        otherSkills = otherSkills.trim(),
                    )
                val session =
                    UserSession(
                        email = email.trim().lowercase(),
                        password = password,
                        profile = profile,
                    )
                if (AppAuth.register(session)) {
                    onBack()
                } else {
                    registerError = R.string.error_correo_ya_registrado
                }
            },
            modifier =
                Modifier
                    .fillMaxWidth()
                    .height(48.dp),
            enabled = canSubmit,
        ) {
            Text(stringResource(R.string.registrarse))
        }
        TextButton(onClick = onNavigateToLogin, modifier = Modifier.align(Alignment.CenterHorizontally)) {
            Text("¿Ya tienes cuenta? Inicia sesión", color = HeistPalette.amber)
        }
        Spacer(Modifier.height(24.dp))
    }
}
