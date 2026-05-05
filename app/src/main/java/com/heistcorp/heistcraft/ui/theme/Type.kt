package com.heistcorp.heistcraft.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import com.heistcorp.heistcraft.R

private val DmSans = FontFamily(
    Font(R.font.dm_sans_variable, weight = FontWeight.Normal),
    Font(R.font.dm_sans_variable, weight = FontWeight.Medium),
    Font(R.font.dm_sans_variable, weight = FontWeight.Bold),
    Font(R.font.dm_sans_italic_variable, weight = FontWeight.Normal, style = FontStyle.Italic),
    Font(R.font.dm_sans_italic_variable, weight = FontWeight.Medium, style = FontStyle.Italic),
    Font(R.font.dm_sans_italic_variable, weight = FontWeight.Bold, style = FontStyle.Italic)
)

private val DmSerifDisplay = FontFamily(
    Font(R.font.dm_serif_display_regular, weight = FontWeight.Normal),
    Font(R.font.dm_serif_display_italic, weight = FontWeight.Normal, style = FontStyle.Italic)
)

private val DefaultTypography = Typography()

val Typography = Typography(
    // Títulos
    displayLarge = DefaultTypography.displayLarge.copy(fontFamily = DmSerifDisplay),
    displayMedium = DefaultTypography.displayMedium.copy(fontFamily = DmSerifDisplay),
    displaySmall = DefaultTypography.displaySmall.copy(fontFamily = DmSerifDisplay),
    headlineLarge = DefaultTypography.headlineLarge.copy(fontFamily = DmSerifDisplay),
    headlineMedium = DefaultTypography.headlineMedium.copy(fontFamily = DmSerifDisplay),
    headlineSmall = DefaultTypography.headlineSmall.copy(fontFamily = DmSerifDisplay),
    titleLarge = DefaultTypography.titleLarge.copy(fontFamily = DmSerifDisplay),
    titleMedium = DefaultTypography.titleMedium.copy(fontFamily = DmSerifDisplay),
    titleSmall = DefaultTypography.titleSmall.copy(fontFamily = DmSerifDisplay),

    // Botones (M3 usa labelLarge por defecto)
    labelLarge = DefaultTypography.labelLarge.copy(fontFamily = DmSerifDisplay),

    // Resto del contenido
    bodyLarge = DefaultTypography.bodyLarge.copy(fontFamily = DmSans),
    bodyMedium = DefaultTypography.bodyMedium.copy(fontFamily = DmSans),
    bodySmall = DefaultTypography.bodySmall.copy(fontFamily = DmSans),
    labelMedium = DefaultTypography.labelMedium.copy(fontFamily = DmSans),
    labelSmall = DefaultTypography.labelSmall.copy(fontFamily = DmSans)
)
