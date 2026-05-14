package com.heistcorp.heistcraft.ui.components

import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.runtime.Composable
import com.heistcorp.heistcraft.ui.theme.HeistPalette

@Composable
fun heistFormFieldColors() =
    OutlinedTextFieldDefaults.colors(
        focusedTextColor = HeistPalette.text,
        unfocusedTextColor = HeistPalette.text,
        focusedLabelColor = HeistPalette.amber,
        unfocusedLabelColor = HeistPalette.muted,
    )
