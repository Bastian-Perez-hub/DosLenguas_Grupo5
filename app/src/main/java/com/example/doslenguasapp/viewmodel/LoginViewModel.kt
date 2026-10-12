package com.example.doslenguasapp.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val emailError: String? = null,
    val passwordError: String? = null,
    val loginValido: Boolean = false
)

class LoginViewModel : ViewModel() {

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

        _state.update {
            it.copy(
                emailError = emailError,
                passwordError = passwordError,
                // Por ahora solo marcamos que el formulario es válido.
                // En la semana 9 aquí se consultará Room para verificar las credenciales.
                loginValido = emailError == null && passwordError == null
            )
        }
    }
}