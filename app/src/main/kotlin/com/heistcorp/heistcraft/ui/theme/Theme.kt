package com.heistcorp.heistcraft.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.colorResource
import com.heistcorp.heistcraft.R

@Composable
private fun heistDarkColorScheme() = darkColorScheme(
    primary = colorResource(R.color.heist_accent),
    onPrimary = colorResource(R.color.heist_surface),
    primaryContainer = colorResource(R.color.heist_highlight),
    onPrimaryContainer = colorResource(R.color.heist_text),

    secondary = colorResource(R.color.heist_raised),
    onSecondary = colorResource(R.color.heist_text),
    secondaryContainer = colorResource(R.color.heist_overlay),
    onSecondaryContainer = colorResource(R.color.heist_text),

    tertiary = colorResource(R.color.heist_warn),
    onTertiary = colorResource(R.color.heist_surface),
    tertiaryContainer = colorResource(R.color.heist_highlight),
    onTertiaryContainer = colorResource(R.color.heist_text),

    error = colorResource(R.color.heist_error),
    onError = colorResource(R.color.heist_surface),
    errorContainer = colorResource(R.color.heist_overlay),
    onErrorContainer = colorResource(R.color.heist_text),

    background = colorResource(R.color.heist_surface),
    onBackground = colorResource(R.color.heist_text),
    surface = colorResource(R.color.heist_surface),
    onSurface = colorResource(R.color.heist_text),
    surfaceVariant = colorResource(R.color.heist_overlay),
    onSurfaceVariant = colorResource(R.color.heist_muted),
    outline = colorResource(R.color.heist_muted),
    outlineVariant = colorResource(R.color.heist_highlight),
    inverseSurface = colorResource(R.color.heist_text),
    inverseOnSurface = colorResource(R.color.heist_surface),
    inversePrimary = colorResource(R.color.heist_accent)
)

@Composable
fun MyApplicationTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = heistDarkColorScheme(),
        typography = AppTypography,
        content = content
    )
}
