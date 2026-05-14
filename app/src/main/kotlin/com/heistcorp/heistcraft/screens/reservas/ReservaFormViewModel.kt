package com.heistcorp.heistcraft.screens.reservas

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.heistcorp.heistcraft.data.BancoApi
import com.heistcorp.heistcraft.data.ReservaApi
import com.heistcorp.heistcraft.data.ReservaCreateBody
import com.heistcorp.heistcraft.network.toNetworkMessage
import kotlinx.coroutines.launch

data class ReservaFormUiState(
    val bancos: List<BancoApi> = emptyList(),
    val editingReserva: ReservaApi? = null,
    val selectedBankId: String? = null,
    val selectedBankLabel: String = "",
    val loading: Boolean = true,
    val saving: Boolean = false,
    val error: String? = null,
    val userMessage: String? = null,
    val saved: Boolean = false,
)

class ReservaFormViewModel(
    private val repository: ReservasRepository,
) : ViewModel() {

    var uiState by mutableStateOf(ReservaFormUiState())
        private set

    fun loadForCreate() {
        viewModelScope.launch {
            uiState = uiState.copy(loading = true, error = null, saved = false)
            try {
                val bancos = repository.getBancos()
                uiState = uiState.copy(bancos = bancos, loading = false, error = null)
            } catch (error: Exception) {
                uiState = uiState.copy(
                    loading = false,
                    error = error.toNetworkMessage(defaultMessage = "No se pudieron cargar los bancos"),
                )
            }
        }
    }

    fun selectBank(bankId: String, bankLabel: String) {
        uiState = uiState.copy(selectedBankId = bankId, selectedBankLabel = bankLabel)
    }

    fun loadForEdit(reservaId: String) {
        viewModelScope.launch {
            uiState = uiState.copy(loading = true, error = null, saved = false)
            try {
                val reserva = repository.getReserva(reservaId)
                val bancos = repository.getBancos()
                val bid = reserva.bankId.orEmpty()
                val bankName = bancos.find { it.id == bid }?.name.orEmpty()

                uiState =
                    uiState.copy(
                        bancos = bancos,
                        editingReserva = reserva,
                        selectedBankId = bid.ifBlank { null },
                        selectedBankLabel = if (bankName.isNotBlank()) bankName else bid,
                        loading = false,
                        error = null,
                    )
            } catch (error: Exception) {
                uiState = uiState.copy(
                    loading = false,
                    error = error.toNetworkMessage(defaultMessage = "No se pudo cargar la reserva"),
                )
            }
        }
    }

    fun saveReservation(
        body: ReservaCreateBody,
        editingId: String?,
    ) {
        viewModelScope.launch {
            uiState = uiState.copy(saving = true)
            try {
                if (editingId != null) {
                    repository.updateReserva(editingId, body)
                    uiState = uiState.copy(
                        userMessage = "Reserva actualizada",
                        saving = false,
                        saved = true
                    )
                } else {
                    repository.createReserva(body)
                    uiState = uiState.copy(
                        userMessage = "Reserva creada correctamente",
                        saving = false,
                        saved = true
                    )
                }
            } catch (error: Exception) {
                uiState = uiState.copy(
                    saving = false,
                    userMessage = "Error: ${error.toNetworkMessage(defaultMessage = "No se pudo guardar")}",
                )
            }
        }
    }

    fun consumeUserMessage() {
        uiState = uiState.copy(userMessage = null)
    }
}

class ReservaFormViewModelFactory(
    private val repository: ReservasRepository,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ReservaFormViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ReservaFormViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}
