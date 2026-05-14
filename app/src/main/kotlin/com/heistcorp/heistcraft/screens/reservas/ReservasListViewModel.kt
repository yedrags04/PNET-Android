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

data class ReservasListUiState(
    val reservas: List<ReservaApi> = emptyList(),
    val cargando: Boolean = true,
    val error: String? = null,
)

class ReservasListViewModel(
    private val repository: ReservasRepository,
) : ViewModel() {

    var uiState by mutableStateOf(ReservasListUiState())
        private set

    init {
        refresh()
    }

    fun refresh() {
        viewModelScope.launch {
            uiState = uiState.copy(cargando = true, error = null)
            try {
                val reservas = repository.getReservas()
                uiState = uiState.copy(reservas = reservas, cargando = false, error = null)
            } catch (error: Exception) {
                uiState =
                    uiState.copy(
                        cargando = false,
                        error = error.toNetworkMessage(defaultMessage = "Error al cargar reservas"),
                    )
            }
        }
    }
}

class ReservasListViewModelFactory(
    private val repository: ReservasRepository,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ReservasListViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ReservasListViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}
