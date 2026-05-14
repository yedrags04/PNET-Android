package com.heistcorp.heistcraft.screens.bancos

import com.heistcorp.heistcraft.data.BancoApi
import com.heistcorp.heistcraft.data.ReservaApi

data class BancoRow(
    val banco: BancoApi,
    val reserva: ReservaApi?,
) {
    val reserved: Boolean get() = reserva != null
}

data class BancosUiState(
    val rows: List<BancoRow> = emptyList(),
    val loading: Boolean = true,
    val loadError: String? = null,
    val selectedRow: BancoRow? = null,
    val showForm: Boolean = false,
    val editingReserva: ReservaApi? = null,
    val userMessage: String? = null,
)

fun BancoRow.matchesFilter(
    location: String,
    difficulty: String,
    maxReward: Int,
    availability: String,
): Boolean {
    val addr = banco.address.lowercase()
    val diff = banco.difficulty.lowercase()
    val displayedAvailability =
        when {
            reserved -> "no"
            banco.available -> "si"
            else -> "no"
        }

    val locationMatches = location.isBlank() || addr.contains(location.lowercase())
    val difficultyMatches = difficulty.isBlank() || diff == difficulty.lowercase()
    val rewardMatches = banco.reward <= maxReward
    val availabilityMatches = availability == "todos" || displayedAvailability == availability

    return locationMatches && difficultyMatches && rewardMatches && availabilityMatches
}
