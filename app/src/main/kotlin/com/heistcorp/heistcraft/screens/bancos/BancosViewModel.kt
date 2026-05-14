package com.heistcorp.heistcraft.screens.bancos

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.heistcorp.heistcraft.data.ReservaCreateBody
import com.heistcorp.heistcraft.network.toNetworkMessage
import kotlinx.coroutines.launch

class BancosViewModel(
    private val repository: BancosRepository,
) : ViewModel() {

    var uiState by mutableStateOf(BancosUiState())
        private set

    init {
        refreshData()
    }

    fun refreshData() {
        viewModelScope.launch {
            uiState = uiState.copy(loading = true, loadError = null)

            try {
                val rows = repository.loadRows()
                val selectedBankId = uiState.selectedRow?.banco?.id

                uiState =
                    uiState.copy(
                        rows = rows,
                        loading = false,
                        loadError = null,
                        selectedRow = rows.firstOrNull { it.banco.id == selectedBankId },
                    )
            } catch (error: Exception) {
                uiState =
                    uiState.copy(
                        loading = false,
                        loadError = error.toNetworkMessage(defaultMessage = "No se pudo conectar con el servidor"),
                    )
            }
        }
    }

    fun selectRow(row: BancoRow) {
        uiState = uiState.copy(selectedRow = row)
    }

    fun dismissDetails() {
        uiState = uiState.copy(selectedRow = null)
    }

    fun startCreateReservation() {
        uiState = uiState.copy(showForm = true, editingReserva = null)
    }

    fun startEditReservation() {
        uiState = uiState.copy(showForm = true, editingReserva = uiState.selectedRow?.reserva)
    }

    fun dismissForm() {
        uiState = uiState.copy(showForm = false, selectedRow = null, editingReserva = null)
    }

    fun cancelSelectedReservation() {
        val reservaId = uiState.selectedRow?.reserva?.id ?: return

        viewModelScope.launch {
            try {
                repository.deleteReserva(reservaId)
                uiState =
                    uiState.copy(
                        userMessage = "Reserva cancelada",
                        showForm = false,
                        selectedRow = null,
                        editingReserva = null,
                    )
                refreshData()
            } catch (error: Exception) {
                uiState = uiState.copy(
                    userMessage = "No se pudo cancelar: ${
                        error.toNetworkMessage(defaultMessage = "Error inesperado")
                    }"
                )
            }
        }
    }

    fun saveReservation(
        body: ReservaCreateBody,
        editingId: String?,
    ) {
        viewModelScope.launch {
            try {
                if (editingId != null) {
                    repository.updateReserva(editingId, body)
                    uiState = uiState.copy(userMessage = "Reserva actualizada")
                } else {
                    repository.createReserva(body)
                    uiState = uiState.copy(userMessage = "Reserva creada")
                }

                uiState = uiState.copy(showForm = false, selectedRow = null, editingReserva = null)
                refreshData()
            } catch (error: Exception) {
                uiState = uiState.copy(
                    userMessage = "Error al guardar: ${
                        error.toNetworkMessage(defaultMessage = "Error inesperado")
                    }"
                )
            }
        }
    }

    fun consumeUserMessage() {
        uiState = uiState.copy(userMessage = null)
    }
}

class BancosViewModelFactory(
    private val repository: BancosRepository,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BancosViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return BancosViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}

