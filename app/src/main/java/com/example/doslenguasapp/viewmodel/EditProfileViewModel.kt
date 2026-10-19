package com.example.doslenguasapp.viewmodel


import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class EditProfileUiState(
    // Valores iniciales de ejemplo. En la semana 9 vendrán de la base de datos.
    val nombre: String = "Bastián Pérez",
    val email: String = "bastian@correo.com",
    val password: String = "",
    val confirmPassword: String = "",
    val nombreError: String? = null,
    val emailError: String? = null,
    val passwordError: String? = null,
    val confirmPasswordError: String? = null,
    val guardadoValido: Boolean = false
)

class EditProfileViewModel : ViewModel() {

    private val _state = MutableStateFlow(EditProfileUiState())
    val state: StateFlow<EditProfileUiState> = _state.asStateFlow()

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

        _state.update {
            it.copy(
                nombreError = nombreError,
                emailError = emailError,
                passwordError = passwordError,
                confirmPasswordError = confirmError,
                // Por ahora solo marcamos que es válido. En la semana 9 se guarda en Room.
                guardadoValido = nombreError == null && emailError == null &&
                        passwordError == null && confirmError == null
            )
        }
    }
}