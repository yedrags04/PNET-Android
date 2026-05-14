package com.heistcorp.heistcraft.screens.utensilios

import com.heistcorp.heistcraft.data.UtensilioApi

data class UtensiliosUiState(
    val catalogo: List<UtensilioApi> = emptyList(),
    val cargando: Boolean = true,
    val error: String? = null,
)
