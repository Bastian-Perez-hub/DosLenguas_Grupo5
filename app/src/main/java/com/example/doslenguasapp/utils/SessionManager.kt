package com.example.doslenguasapp.utils

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

// Guarda quién inició sesión. Es un "object": hay una sola copia para toda la app.
object SessionManager {

    // null significa que no hay nadie con sesión iniciada.
    private val _currentUserId = MutableStateFlow<Int?>(null)
    val currentUserId: StateFlow<Int?> = _currentUserId.asStateFlow()

    fun login(userId: Int) {
        _currentUserId.value = userId
    }

    fun logout() {
        _currentUserId.value = null
    }
}