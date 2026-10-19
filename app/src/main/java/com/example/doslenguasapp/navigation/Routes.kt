package com.example.doslenguasapp.navigation

/**
 * Todas las rutas de la app en un solo lugar.
 * Si cambia un nombre, se cambia aquí y no en cada pantalla.
 */
object Routes {
    const val LOGIN = "login"
    const val REGISTER = "register"
    const val HOME = "home"
    const val MY_PHRASES = "my_phrases"
    const val PROFILE = "profile"
    const val EDIT_PROFILE = "edit_profile"
    const val FLASHCARDS = "flashcards"

    // El detalle necesita saber QUÉ frase mostrar, así que lleva un argumento.
    const val DETAIL = "detail/{phraseId}"

    fun detail(phraseId: Int) = "detail/$phraseId"
}