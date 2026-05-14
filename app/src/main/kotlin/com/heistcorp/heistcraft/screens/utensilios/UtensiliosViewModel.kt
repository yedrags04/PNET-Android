package com.heistcorp.heistcraft.screens.utensilios

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.heistcorp.heistcraft.network.toNetworkMessage
import kotlinx.coroutines.launch

class UtensiliosViewModel(
    private val repository: UtensiliosRepository,
) : ViewModel() {

    var uiState by mutableStateOf(UtensiliosUiState())
        private set

    init {
        refresh()
    }

    fun refresh() {
        viewModelScope.launch {
            uiState = uiState.copy(cargando = true, error = null)
            try {
                val catalogo = repository.getUtensilios()
                uiState = uiState.copy(catalogo = catalogo, cargando = false, error = null)
            } catch (error: Exception) {
                uiState =
                    uiState.copy(
                        cargando = false,
                        error = error.toNetworkMessage(defaultMessage = "Error al cargar"),
                    )
            }
        }
    }
}

class UtensiliosViewModelFactory(
    private val repository: UtensiliosRepository,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UtensiliosViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return UtensiliosViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}

