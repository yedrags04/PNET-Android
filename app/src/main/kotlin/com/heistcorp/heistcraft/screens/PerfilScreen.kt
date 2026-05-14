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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.heistcorp.heistcraft.R
import com.heistcorp.heistcraft.auth.AppAuth
import com.heistcorp.heistcraft.ui.theme.HeistPalette

@Composable
fun PerfilScreen(
    onBack: () -> Unit,
    onNavigateToLogin: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val session = AppAuth.currentSession

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
            Icon(
                Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Volver",
                tint = HeistPalette.text
            )
        }
        Text("Perfil", style = MaterialTheme.typography.headlineMedium, color = HeistPalette.text)
        if (session == null) {
            Text(
                stringResource(R.string.perfil_no_sesion),
                style = MaterialTheme.typography.bodyLarge,
                color = HeistPalette.muted,
            )
            Button(
                onClick = onNavigateToLogin,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = HeistPalette.positiveGreen),
            ) {
                Text(stringResource(R.string.ir_iniciar_sesion))
            }
        } else {
            Text(
                stringResource(R.string.perfil_seccion_ficha),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary,
            )
            PerfilCampo(
                etiqueta = stringResource(R.string.field_nombre_completo),
                valor = session.profile.fullName,
            )
            PerfilCampo(
                etiqueta = stringResource(R.string.field_apodo),
                valor = session.profile.nickname,
            )
            PerfilCampo(
                etiqueta = stringResource(R.string.field_anos_experiencia),
                valor =
                    pluralStringResource(
                        R.plurals.perfil_anos_valor,
                        session.profile.yearsExperience,
                        session.profile.yearsExperience,
                    ),
            )
            PerfilCampo(
                etiqueta = stringResource(R.string.field_habilidad_principal),
                valor = session.profile.mainSkill,
            )
            if (session.profile.otherSkills.isNotBlank()) {
                PerfilCampo(
                    etiqueta = stringResource(R.string.field_otras_habilidades),
                    valor = session.profile.otherSkills,
                )
            }
            Spacer(Modifier.height(8.dp))
            PerfilCampo(
                etiqueta = stringResource(R.string.field_correo),
                valor = session.email,
            )
            Button(
                onClick = {
                    AppAuth.signOut()
                    onBack()
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = HeistPalette.buttonDangerDark),
            ) {
                Text(stringResource(R.string.cerrar_sesion))
            }
        }
    }
}

@Composable
private fun PerfilCampo(
    etiqueta: String,
    valor: String,
) {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(
            etiqueta,
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.primary,
        )
        Text(
            valor,
            style = MaterialTheme.typography.bodyLarge,
            color = HeistPalette.text,
        )
    }
}
