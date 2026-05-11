package com.heistcorp.heistcraft.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TutorialScreen(onBack: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Tutorial de la app") },
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
    ) { padding ->
        Text(
            text =
                """
                1. Menú lateral: pulsa el icono de tres rayas (☰) arriba a la izquierda para abrir el cajón. Desde ahí accedes a Salas, Reservas, Animales, Localización y este tutorial.

                2. Salas: consulta la información de cada sala y genera un PDF con el icono del documento en la barra superior; compártelo o guárdalo desde el selector del sistema.

                3. Reservas: lista todas las reservas (GET API). Pulsa una fila para ver el detalle, editar (PUT) o eliminar (DELETE). El botón flotante “Nueva reserva” abre un formulario en pantalla completa (POST).

                4. Bancos y utensilios: siguen conectados al mismo backend HeistCraft (MongoDB en Atlas o local según tu .env).

                5. Animales: CRUD sobre la API tipo animal-api-mongo (id numérico autoincremental en colección counters).

                6. Conexión: la app usa la URL definida en BuildConfig.API_BASE_URL (por defecto emulador: http://10.0.2.2:8080/). El servidor debe tener `npm run start` en el proyecto PNET.
                """.trimIndent(),
            style = MaterialTheme.typography.bodyLarge,
            color = Color.White,
            fontWeight = FontWeight.Normal,
            modifier =
                Modifier
                    .padding(padding)
                    .padding(16.dp)
                    .background(Color(0xFF131722))
                    .verticalScroll(rememberScrollState()),
        )
    }
}
