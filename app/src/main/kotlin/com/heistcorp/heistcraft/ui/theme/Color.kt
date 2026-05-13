package com.heistcorp.heistcraft.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import com.heistcorp.heistcraft.R

/**
 * Paleta única definida en `res/values/colors.xml`.
 * Usar estas propiedades en `@Composable` en lugar de literales [Color].
 */
object HeistPalette {
    val surface: Color
        @Composable @ReadOnlyComposable get() = colorResource(R.color.heist_surface)
    val raised: Color
        @Composable @ReadOnlyComposable get() = colorResource(R.color.heist_raised)
    val overlay: Color
        @Composable @ReadOnlyComposable get() = colorResource(R.color.heist_overlay)
    val highlight: Color
        @Composable @ReadOnlyComposable get() = colorResource(R.color.heist_highlight)

    val accent: Color
        @Composable @ReadOnlyComposable get() = colorResource(R.color.heist_accent)
    val success: Color
        @Composable @ReadOnlyComposable get() = colorResource(R.color.heist_success)
    val warn: Color
        @Composable @ReadOnlyComposable get() = colorResource(R.color.heist_warn)
    val error: Color
        @Composable @ReadOnlyComposable get() = colorResource(R.color.heist_error)

    val text: Color
        @Composable @ReadOnlyComposable get() = colorResource(R.color.heist_text)
    val muted: Color
        @Composable @ReadOnlyComposable get() = colorResource(R.color.heist_muted)

    val screenBackground: Color
        @Composable @ReadOnlyComposable get() = colorResource(R.color.heist_screen_background)
    val card: Color
        @Composable @ReadOnlyComposable get() = colorResource(R.color.heist_card)
    val divider: Color
        @Composable @ReadOnlyComposable get() = colorResource(R.color.heist_divider)

    val positiveGreen: Color
        @Composable @ReadOnlyComposable get() = colorResource(R.color.heist_positive_green)
    val errorSoft: Color
        @Composable @ReadOnlyComposable get() = colorResource(R.color.heist_error_soft)
    val buttonDanger: Color
        @Composable @ReadOnlyComposable get() = colorResource(R.color.heist_button_danger)
    val buttonDangerDark: Color
        @Composable @ReadOnlyComposable get() = colorResource(R.color.heist_button_danger_dark)
    val amber: Color
        @Composable @ReadOnlyComposable get() = colorResource(R.color.heist_amber)
    val delete: Color
        @Composable @ReadOnlyComposable get() = colorResource(R.color.heist_delete)

    val scrim: Color
        @Composable @ReadOnlyComposable get() = colorResource(R.color.heist_scrim)
    val scrimCart: Color
        @Composable @ReadOnlyComposable get() = colorResource(R.color.heist_scrim_cart)
    val scrimModal: Color
        @Composable @ReadOnlyComposable get() = colorResource(R.color.heist_scrim_modal)

    val accentFocusRing: Color
        @Composable @ReadOnlyComposable get() = colorResource(R.color.heist_accent_focus_ring)
    val accentGlowSoft: Color
        @Composable @ReadOnlyComposable get() = colorResource(R.color.heist_accent_glow_soft)
    val borderOnRaised: Color
        @Composable @ReadOnlyComposable get() = colorResource(R.color.heist_border_on_raised)

    val neutralMid: Color
        @Composable @ReadOnlyComposable get() = colorResource(R.color.heist_neutral_mid)
}
