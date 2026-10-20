package com.example.doslenguasapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.doslenguasapp.data.UserRepository
import com.example.doslenguasapp.utils.SessionManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class ProfileUiState(
    val nombre: String = "",
    val email: String = ""
)

class ProfileViewModel(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _state = MutableStateFlow(ProfileUiState())
    val state: StateFlow<ProfileUiState> = _state.asStateFlow()

    init {
        // Quién inició sesión (lo guardó el Login en SessionManager).
        val userId = SessionManager.currentUserId.value
        if (userId != null) {
            viewModelScope.launch {
                // observeUser devuelve un Flow: cada vez que el usuario cambie en Room
                // (por ejemplo, tras Editar datos), esto se ejecuta de nuevo solo.
                userRepository.observeUser(userId).collect { user ->
                    if (user != null) {
                        _state.update { it.copy(nombre = user.nombre, email = user.email) }
                    }
                }
            }
        }
    }
}