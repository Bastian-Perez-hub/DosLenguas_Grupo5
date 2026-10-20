package com.example.doslenguasapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.doslenguasapp.data.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

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

class RegisterViewModel(
    private val userRepository: UserRepository
) : ViewModel() {

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

        // Primero se validan los campos. Si hay errores, se muestran y no se toca Room.
        if (nombreError != null || emailError != null ||
            passwordError != null || confirmError != null
        ) {
            _state.update {
                it.copy(
                    nombreError = nombreError,
                    emailError = emailError,
                    passwordError = passwordError,
                    confirmPasswordError = confirmError
                )
            }
            return
        }

        // Los campos son válidos: ahora se intenta guardar el usuario en Room.
        viewModelScope.launch {
            val creado = userRepository.register(actual.nombre, actual.email, actual.password)
            if (creado) {
                _state.update {
                    it.copy(
                        nombreError = null,
                        emailError = null,
                        passwordError = null,
                        confirmPasswordError = null,
                        registroValido = true
                    )
                }
            } else {
                // El repositorio devuelve false cuando ese correo ya tiene cuenta.
                _state.update { it.copy(emailError = "Ya existe una cuenta con este correo") }
            }
        }
    }
}