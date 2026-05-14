package com.heistcorp.heistcraft.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.heistcorp.heistcraft.ui.theme.HeistPalette

private data class FaqTextEntry(val question: String, val answer: String)

@Composable
fun FaqScreen(onBack: () -> Unit, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val mapUrl =
        "https://www.google.com/maps/search/?api=1&query=Escuela+Superior+de+Ingeniería+Cádiz"

    val textEntries =
        remember {
            listOf(
                FaqTextEntry(
                    "¿Cómo elegir el objetivo correcto?",
                    "Consulta el catálogo de bancos filtrado por dificultad, ubicación y recompensa. " +
                            "Considera tu experiencia y equipo disponible antes de elegir. Los objetivos con " +
                            "dificultad fácil son ideales para principiantes, mientras que los de dificultad alta " +
                            "requieren equipo avanzado.",
                ),
                FaqTextEntry(
                    "¿Qué equipamiento es esencial?",
                    "El equipo mínimo incluye guantes de microfibra para evitar huellas, pasamontañas para " +
                            "proteger tu identidad y herramientas básicas como cortacristales. En operaciones de " +
                            "alta dificultad recomendamos cortacristales con ventosa para mayor precisión.",
                ),
                FaqTextEntry(
                    "¿Puedo cambiar de objetivo después de hacer la reserva?",
                    "Sí, puedes cambiar de objetivo hasta 48 horas antes de la operación. Los cambios " +
                            "realizados después de este período incurrirán en una penalización del 10% sobre la " +
                            "recompensa estimada.",
                ),
                FaqTextEntry(
                    "¿Cómo funciona el sistema de dificultad?",
                    "• Fácil: Horarios comerciales, mínimos guardias.\n" +
                            "• Media: Horarios limitados, sistemas de seguridad básicos.\n" +
                            "• Alta: Vigilancia 24h, sistemas avanzados. La dificultad afecta directamente a tu " +
                            "recompensa potencial.",
                ),
                FaqTextEntry(
                    "¿Qué tipos de objetos puedo obtener?",
                    "Disponemos de tres categorías principales: efectivo en euros, lingotes de oro y obras de " +
                            "arte. Cada categoría tiene su propio mercado y valor.",
                ),
                FaqTextEntry(
                    "¿Hay límite de operaciones por mes?",
                    "No hay límite de operaciones, pero recomendamos no exceder 3 operaciones por mes en el " +
                            "mismo objetivo para evitar sospechas.",
                ),
                FaqTextEntry(
                    "¿Qué pasa si algo sale mal durante la operación?",
                    "HeistCraft no se responsabiliza por complicaciones durante las operaciones. Sin embargo, " +
                            "proporcionamos mapas detallados, horarios actualizados y asesoramiento sobre rutas de " +
                            "escape seguras. Recomendamos siempre planificar exhaustivamente.",
                ),
                FaqTextEntry(
                    "¿Cómo puedo mejorar mi equipo?",
                    "Visita la sección Utensilios donde encontrarás desde equipamiento básico hasta " +
                            "herramientas profesionales. A medida que aumentes tu experiencia, podrás acceder a " +
                            "equipos de mayor calidad.",
                ),
            )
        }

    Column(
        modifier =
            modifier
                .fillMaxSize()
                .background(HeistPalette.screenBackground)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            IconButton(onClick = onBack) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Volver",
                    tint = HeistPalette.text,
                )
            }
            Text(
                "Preguntas frecuentes",
                style = MaterialTheme.typography.headlineSmall,
                color = HeistPalette.text,
                fontWeight = FontWeight.Bold,
            )
        }

        Text(
            "Todo lo que necesitas saber sobre las operaciones en HeistCraft",
            style = MaterialTheme.typography.bodyMedium,
            color = HeistPalette.muted,
        )

        HorizontalDivider(color = HeistPalette.neutralMid)

        FaqExpandableMapItem(
            question = "¿Dónde encontrarnos?",
            onOpenMap = {
                context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(mapUrl)))
            },
        )

        HorizontalDivider(color = HeistPalette.divider)

        textEntries.forEachIndexed { index, entry ->
            FaqExpandableTextItem(question = entry.question, answer = entry.answer)
            if (index < textEntries.lastIndex) {
                HorizontalDivider(color = HeistPalette.divider)
            }
        }
    }
}

@Composable
private fun FaqExpandableTextItem(question: String, answer: String, modifier: Modifier = Modifier) {
    var expanded by remember(question) { mutableStateOf(false) }
    FaqExpandableShell(
        question = question,
        expanded = expanded,
        onToggle = { expanded = !expanded },
        modifier = modifier,
    ) {
        Text(answer, color = HeistPalette.muted, style = MaterialTheme.typography.bodyMedium)
    }
}

@Composable
private fun FaqExpandableMapItem(
    question: String,
    onOpenMap: () -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember(question) { mutableStateOf(false) }
    FaqExpandableShell(
        question = question,
        expanded = expanded,
        onToggle = { expanded = !expanded },
        modifier = modifier,
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text(
                "Nuestra base logística está en la Escuela Superior de Ingeniería (Cádiz).",
                color = HeistPalette.muted,
                style = MaterialTheme.typography.bodyMedium,
            )
            Button(onClick = onOpenMap) {
                Text("Abrir en Google Maps")
            }
        }
    }
}

@Composable
private fun FaqExpandableShell(
    question: String,
    expanded: Boolean,
    onToggle: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .clickable(onClick = onToggle)
                    .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                question,
                style = MaterialTheme.typography.titleMedium,
                color = HeistPalette.amber,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.weight(1f),
            )
            Icon(
                imageVector = if (expanded) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
                contentDescription = null,
                tint = HeistPalette.text,
            )
        }
        if (expanded) {
            Column(modifier = Modifier.padding(bottom = 8.dp, start = 4.dp)) {
                content()
            }
        }
    }
}
