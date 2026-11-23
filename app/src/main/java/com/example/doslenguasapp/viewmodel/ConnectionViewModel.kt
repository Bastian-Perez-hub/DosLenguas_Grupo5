package com.example.doslenguasapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.doslenguasapp.data.remote.TodoDto
import com.example.doslenguasapp.data.remote.TodoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class ConnectionUiState(
    val todo: TodoDto? = null,
    val loading: Boolean = false,
    val error: String? = null
)

class ConnectionViewModel(
    private val todoRepository: TodoRepository
) : ViewModel() {

    private val _state = MutableStateFlow(ConnectionUiState())
    val state: StateFlow<ConnectionUiState> = _state.asStateFlow()

    // La pantalla llama a esta función cuando se toca el botón "Consultar API".
    fun loadTodo() {
        viewModelScope.launch {
            _state.update { it.copy(loading = true, error = null) }

            todoRepository.getTodo()
                .onSuccess { todo ->
                    _state.update { it.copy(todo = todo, loading = false, error = null) }
                }
                .onFailure { e ->
                    _state.update {
                        it.copy(
                            loading = false,
                            error = e.message ?: "No se pudo conectar con la API"
                        )
                    }
                }
        }
    }
}
