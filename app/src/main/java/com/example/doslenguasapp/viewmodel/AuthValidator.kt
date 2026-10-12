package com.example.doslenguasapp.viewmodel

/**
 * Reglas de validación. No usa nada de Compose: devuelve el mensaje de error
 * o null si el campo es válido.
 */
object AuthValidator {
    private val EMAIL_REGEX = Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")

    fun email(valor: String): String? = when {
        valor.isBlank() -> "El correo es obligatorio"
        !EMAIL_REGEX.matches(valor.trim()) -> "Formato de correo inválido"
        else -> null
    }

    // En el login solo pedimos que no esté vacía.
    // Las reglas de largo y formato se aplican en el registro.
    fun passwordLogin(valor: String): String? = when {
        valor.isBlank() -> "La contraseña es obligatoria"
        else -> null
    }
}