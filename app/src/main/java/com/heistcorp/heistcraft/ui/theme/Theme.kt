package com.heistcorp.heistcraft.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val HeistColorScheme = darkColorScheme(
    primary = HeistAccent,
    onPrimary = HeistSurface,
    primaryContainer = HeistHighlight,
    onPrimaryContainer = HeistText,

    secondary = HeistRaised,
    onSecondary = HeistText,
    secondaryContainer = HeistOverlay,
    onSecondaryContainer = HeistText,

    tertiary = HeistWarn,
    onTertiary = HeistSurface,
    tertiaryContainer = HeistHighlight,
    onTertiaryContainer = HeistText,

    error = HeistError,
    onError = HeistSurface,
    errorContainer = HeistOverlay,
    onErrorContainer = HeistText,

    background = HeistSurface,
    onBackground = HeistText,
    surface = HeistSurface,
    onSurface = HeistText,
    surfaceVariant = HeistOverlay,
    onSurfaceVariant = HeistMuted,
    outline = HeistMuted,
    outlineVariant = HeistHighlight,
    inverseSurface = HeistText,
    inverseOnSurface = HeistSurface,
    inversePrimary = HeistAccent
)

@Composable
fun MyApplicationTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = HeistColorScheme,
        typography = Typography,
        content = content
    )
}
