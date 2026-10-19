package com.example.doslenguasapp.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class RegisterUiState(
    val nombre: String = "",
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val nombreError: String? = null,
    val emailError: String? = null,
    val passwordError: String? = null,
    val confirmPasswordError: String? = null,
    val registroValido: Boolean = false
)

class RegisterViewModel : ViewModel() {

    private val _state = MutableStateFlow(RegisterUiState())
    val state: StateFlow<RegisterUiState> = _state.asStateFlow()

    fun onNombreChange(valor: String) {
        _state.update { it.copy(nombre = valor, nombreError = AuthValidator.nombre(valor)) }
    }

    fun onEmailChange(valor: String) {
        _state.update { it.copy(email = valor, emailError = AuthValidator.email(valor)) }
    }

    fun onPasswordChange(valor: String) {
        _state.update {
            it.copy(
                password = valor,
                passwordError = AuthValidator.passwordRegistro(valor),
                // Si ya escribió la confirmación, la revisamos de nuevo:
                // cambiar la contraseña puede hacer que dejen de coincidir.
                confirmPasswordError = if (it.confirmPassword.isEmpty()) null
                else AuthValidator.confirmarPassword(valor, it.confirmPassword)
            )
        }
    }

    fun onConfirmPasswordChange(valor: String) {
        _state.update {
            it.copy(
                confirmPassword = valor,
                confirmPasswordError = AuthValidator.confirmarPassword(it.password, valor)
            )
        }
    }

    fun onRegisterClick() {
        val actual = _state.value
        val nombreError = AuthValidator.nombre(actual.nombre)
        val emailError = AuthValidator.email(actual.email)
        val passwordError = AuthValidator.passwordRegistro(actual.password)
        val confirmError = AuthValidator.confirmarPassword(actual.password, actual.confirmPassword)

        _state.update {
            it.copy(
                nombreError = nombreError,
                emailError = emailError,
                passwordError = passwordError,
                confirmPasswordError = confirmError,
                // Por ahora solo marcamos que el formulario es válido.
                // En la semana 9 aquí se guardará el usuario en Room.
                registroValido = nombreError == null && emailError == null &&
                        passwordError == null && confirmError == null
            )
        }
    }
}