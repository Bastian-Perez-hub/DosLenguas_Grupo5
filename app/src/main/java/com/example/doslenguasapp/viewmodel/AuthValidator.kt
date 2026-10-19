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
    // ---- Reglas del registro ----

    fun nombre(valor: String): String? = when {
        valor.isBlank() -> "El nombre es obligatorio"
        valor.trim().length < 2 -> "El nombre es muy corto"
        // \p{L} acepta letras de cualquier idioma (tildes, ñ, ç, ş, ğ, ı...)
        !valor.trim().all { it.isLetter() || it == ' ' } -> "El nombre solo puede tener letras"
        else -> null
    }

    fun passwordRegistro(valor: String): String? = when {
        valor.isBlank() -> "La contraseña es obligatoria"
        valor.length < 8 -> "Debe tener al menos 8 caracteres"
        valor.none { it.isLetter() } -> "Debe incluir al menos una letra"
        valor.none { it.isDigit() } -> "Debe incluir al menos un número"
        else -> null
    }

    // Recibe las dos contraseñas porque necesita compararlas.
    fun confirmarPassword(password: String, confirmacion: String): String? = when {
        confirmacion.isBlank() -> "Confirma tu contraseña"
        password != confirmacion -> "Las contraseñas no coinciden"
        else -> null
    }
}