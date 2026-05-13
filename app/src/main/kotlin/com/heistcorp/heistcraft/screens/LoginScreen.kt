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

private val loginFieldColors
    @Composable
    get() =
        OutlinedTextFieldDefaults.colors(
            focusedTextColor = HeistPalette.text,
            unfocusedTextColor = HeistPalette.text,
            focusedLabelColor = HeistPalette.amber,
            unfocusedLabelColor = HeistPalette.muted,
        )

@Composable
fun LoginScreen(
    onBack: () -> Unit,
    onNavigateToSignup: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var loginError by remember { mutableStateOf<String?>(null) }
    val context = LocalContext.current

    Column(
        modifier =
            modifier
                .fillMaxSize()
                .background(HeistPalette.screenBackground)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        IconButton(onClick = onBack) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver", tint = HeistPalette.text)
        }
        Text("Iniciar sesión", style = MaterialTheme.typography.headlineMedium, color = HeistPalette.text)
        OutlinedTextField(
            value = email,
            onValueChange = {
                email = it
                loginError = null
            },
            label = { Text(stringResource(R.string.field_correo)) },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            colors = loginFieldColors,
        )
        OutlinedTextField(
            value = password,
            onValueChange = {
                password = it
                loginError = null
            },
            label = { Text(stringResource(R.string.field_contrasena)) },
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
            colors = loginFieldColors,
        )
        loginError?.let { err ->
            Text(err, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
        }
        Button(
            onClick = {
                loginError = null
                if (AppAuth.signIn(email, password)) {
                    onBack()
                } else {
                    loginError = context.getString(R.string.error_credenciales)
                }
            },
            modifier =
                Modifier
                    .fillMaxWidth()
                    .height(48.dp),
            enabled = email.isNotBlank() && password.isNotBlank(),
        ) {
            Text(stringResource(R.string.entrar))
        }
        TextButton(onClick = onNavigateToSignup, modifier = Modifier.align(Alignment.CenterHorizontally)) {
            Text("¿No tienes cuenta? Regístrate", color = HeistPalette.amber)
        }
        Spacer(Modifier.height(24.dp))
    }
}
