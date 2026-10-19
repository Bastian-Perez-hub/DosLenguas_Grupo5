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

data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val emailError: String? = null,
    val passwordError: String? = null,
    val loginValido: Boolean = false
)

class LoginViewModel(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _state = MutableStateFlow(LoginUiState())
    val state: StateFlow<LoginUiState> = _state.asStateFlow()

    fun onEmailChange(valor: String) {
        _state.update { it.copy(email = valor, emailError = AuthValidator.email(valor)) }
    }

    fun onPasswordChange(valor: String) {
        _state.update { it.copy(password = valor, passwordError = AuthValidator.passwordLogin(valor)) }
    }

    fun onLoginClick() {
        val actual = _state.value
        val emailError = AuthValidator.email(actual.email)
        val passwordError = AuthValidator.passwordLogin(actual.password)

        // Primero se validan los campos. Si hay errores, se muestran y no se consulta Room.
        if (emailError != null || passwordError != null) {
            _state.update { it.copy(emailError = emailError, passwordError = passwordError) }
            return
        }

        // Los campos son válidos: ahora sí se comprueban las credenciales en Room.
        // viewModelScope.launch corre esto en segundo plano para no congelar la pantalla.
        viewModelScope.launch {
            val user = userRepository.login(actual.email, actual.password)
            if (user == null) {
                _state.update {
                    it.copy(emailError = null, passwordError = "Correo o contraseña incorrectos")
                }
            } else {
                SessionManager.login(user.id)
                _state.update { it.copy(emailError = null, passwordError = null, loginValido = true) }
            }
        }
    }
}