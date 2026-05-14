package com.heistcorp.heistcraft.screens.reservas

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.heistcorp.heistcraft.data.ReservaApi
import com.heistcorp.heistcraft.network.toNetworkMessage
import kotlinx.coroutines.launch

data class ReservaDetalleUiState(
    val reserva: ReservaApi? = null,
    val cargando: Boolean = true,
    val error: String? = null,
    val deleting: Boolean = false,
    val deleted: Boolean = false,
    val userMessage: String? = null,
)

class ReservaDetalleViewModel(
    private val repository: ReservasRepository,
) : ViewModel() {

    var uiState by mutableStateOf(ReservaDetalleUiState())
        private set

    fun loadReserva(reservaId: String) {
        viewModelScope.launch {
            uiState = uiState.copy(cargando = true, error = null, deleted = false)
            try {
                val reserva = repository.getReserva(reservaId)
                uiState = uiState.copy(reserva = reserva, cargando = false, error = null)
            } catch (error: Exception) {
                uiState =
                    uiState.copy(
                        reserva = null,
                        cargando = false,
                        error = error.toNetworkMessage(defaultMessage = "No se encontró la reserva."),
                    )
            }
        }
    }

    fun deleteReserva() {
        val reservaId = uiState.reserva?.id ?: return
        viewModelScope.launch {
            uiState = uiState.copy(deleting = true)
            try {
                repository.deleteReserva(reservaId)
                uiState = uiState.copy(
                    deleting = false,
                    deleted = true,
                    userMessage = "Reserva eliminada"
                )
            } catch (error: Exception) {
                uiState =
                    uiState.copy(
                        deleting = false,
                        userMessage = "Error: ${error.toNetworkMessage(defaultMessage = "No se pudo eliminar")}",
                    )
            }
        }
    }

    fun consumeUserMessage() {
        uiState = uiState.copy(userMessage = null)
    }
}

class ReservaDetalleViewModelFactory(
    private val repository: ReservasRepository,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ReservaDetalleViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ReservaDetalleViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}
