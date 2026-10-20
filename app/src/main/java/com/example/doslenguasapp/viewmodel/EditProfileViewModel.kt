package com.example.doslenguasapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.doslenguasapp.data.UserRepository
import com.example.doslenguasapp.utils.SessionManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class EditProfileUiState(
    val nombre: String = "",
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val nombreError: String? = null,
    val emailError: String? = null,
    val passwordError: String? = null,
    val confirmPasswordError: String? = null,
    val guardadoValido: Boolean = false
)

class EditProfileViewModel(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _state = MutableStateFlow(EditProfileUiState())
    val state: StateFlow<EditProfileUiState> = _state.asStateFlow()

    // Quién inició sesión (lo guardó el Login en SessionManager).
    private val userId: Int? = SessionManager.currentUserId.value

    init {
        // Se cargan los datos actuales UNA sola vez, para llenar el formulario.
        // Usamos first() y no collect() para que, mientras escribes,
        // Room no te pise lo que estás editando.
        if (userId != null) {
            viewModelScope.launch {
                val user = userRepository.observeUser(userId).first()
                if (user != null) {
                    _state.update { it.copy(nombre = user.nombre, email = user.email) }
                }
            }
        }
    }

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
                passwordError = AuthValidator.passwordNuevaOpcional(valor),
                // Cambiar la contraseña puede hacer que la confirmación deje de coincidir.
                confirmPasswordError = if (it.confirmPassword.isEmpty()) null
                else AuthValidator.confirmarPasswordNueva(valor, it.confirmPassword)
            )
        }
    }

    fun onConfirmPasswordChange(valor: String) {
        _state.update {
            it.copy(
                confirmPassword = valor,
                confirmPasswordError = AuthValidator.confirmarPasswordNueva(it.password, valor)
            )
        }
    }

    fun onGuardarClick() {
        val actual = _state.value
        val nombreError = AuthValidator.nombre(actual.nombre)
        val emailError = AuthValidator.email(actual.email)
        val passwordError = AuthValidator.passwordNuevaOpcional(actual.password)
        val confirmError = AuthValidator.confirmarPasswordNueva(actual.password, actual.confirmPassword)

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

        // Sin usuario con sesión no hay nada que guardar.
        if (userId == null) return

        // Los campos son válidos: ahora se guardan los cambios en Room.
        viewModelScope.launch {
            val guardado = userRepository.updateProfile(
                id = userId,
                nombre = actual.nombre,
                email = actual.email,
                newPassword = actual.password
            )
            if (guardado) {
                _state.update {
                    it.copy(
                        nombreError = null,
                        emailError = null,
                        passwordError = null,
                        confirmPasswordError = null,
                        guardadoValido = true
                    )
                }
            } else {
                // El repositorio devuelve false si el correo nuevo ya lo usa otra cuenta.
                _state.update { it.copy(emailError = "Ese correo ya lo usa otra cuenta") }
            }
        }
    }
}