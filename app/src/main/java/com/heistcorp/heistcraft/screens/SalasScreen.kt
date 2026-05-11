package com.heistcorp.heistcraft.screens

import android.content.Intent
import android.graphics.Color as AndroidColor
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.PictureAsPdf
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import com.heistcorp.heistcraft.data.SalaApi
import com.heistcorp.heistcraft.network.ApiClient
import java.io.File
import java.io.FileOutputStream
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SalasScreen(onBack: () -> Unit) {
    val context = LocalContext.current
    val api = remember { ApiClient.heistApi }
    val scope = rememberCoroutineScope()
    var salas by remember { mutableStateOf<List<SalaApi>>(emptyList()) }
    var error by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        try {
            salas = api.getSalas()
        } catch (e: Exception) {
            error = e.message
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Salas") },
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
                actions = {
                    IconButton(
                        onClick = {
                            scope.launch {
                                generarPdfSalas(context, salas)
                            }
                        },
                        enabled = salas.isNotEmpty(),
                    ) {
                        Icon(Icons.Filled.PictureAsPdf, contentDescription = "Descargar PDF", tint = Color.White)
                    }
                },
            )
        },
    ) { padding ->
        when {
            error != null ->
                Text(error!!, color = Color(0xFFFF7043), modifier = Modifier.padding(padding).padding(16.dp))
            else ->
                LazyColumn(
                    modifier =
                        Modifier
                            .fillMaxSize()
                            .background(Color(0xFF131722))
                            .padding(padding),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    items(salas, key = { it.id }) { s ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFF1E2333)),
                        ) {
                            Column(Modifier.padding(16.dp)) {
                                Text(s.name, color = Color.White, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                                Text(s.description, color = Color.LightGray, style = MaterialTheme.typography.bodyMedium)
                                Text("Capacidad: ${s.capacity} personas", color = Color(0xFF4CAF50))
                                Text("${s.pricePerNight} € / noche", color = Color(0xFFFFC107))
                            }
                        }
                    }
                }
        }
    }
}

private suspend fun generarPdfSalas(
    context: android.content.Context,
    salas: List<SalaApi>,
) {
    withContext(Dispatchers.IO) {
        try {
            val pdf = PdfDocument()
            val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create()
            val page = pdf.startPage(pageInfo)
            val canvas = page.canvas
            val paint =
                Paint().apply {
                    color = AndroidColor.BLACK
                    textSize = 14f
                    isAntiAlias = true
                }
            var y = 48f
            canvas.drawText("HeistCraft — Catálogo de salas", 40f, y, paint)
            y += 28f
            for (s in salas) {
                canvas.drawText("• ${s.name}", 40f, y, paint)
                y += 20f
                canvas.drawText("  ${s.description}", 45f, y, paint)
                y += 18f
                canvas.drawText("  Capacidad: ${s.capacity}  Precio/noche: ${s.pricePerNight} EUR", 45f, y, paint)
                y += 28f
                if (y > 780f) break
            }
            pdf.finishPage(page)
            val file = File(context.cacheDir, "salas-heistcraft.pdf")
            FileOutputStream(file).use { out -> pdf.writeTo(out) }
            pdf.close()

            val uri =
                FileProvider.getUriForFile(
                    context,
                    "${context.packageName}.fileprovider",
                    file,
                )
            val send =
                Intent(Intent.ACTION_SEND).apply {
                    type = "application/pdf"
                    putExtra(Intent.EXTRA_STREAM, uri)
                    addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                }
            withContext(Dispatchers.Main) {
                context.startActivity(Intent.createChooser(send, "Compartir o guardar PDF"))
            }
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                Toast.makeText(context, "No se pudo generar el PDF: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }
}
