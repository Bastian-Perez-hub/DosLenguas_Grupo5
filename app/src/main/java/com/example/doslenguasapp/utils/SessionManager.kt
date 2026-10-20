package com.example.doslenguasapp.utils

import android.content.Context
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

// Guarda quién inició sesión. Ahora también lo persiste en el teléfono.
object SessionManager {

    private const val PREFS_NAME = "session_prefs"
    private const val KEY_USER_ID = "user_id"

    private var prefs: android.content.SharedPreferences? = null

    private val _currentUserId = MutableStateFlow<Int?>(null)
    val currentUserId: StateFlow<Int?> = _currentUserId.asStateFlow()

    // Se llama una vez al abrir la app: recupera la sesión guardada (si existe).
    fun init(context: Context) {
        if (prefs != null) return
        val p = context.applicationContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs = p
        _currentUserId.value = if (p.contains(KEY_USER_ID)) p.getInt(KEY_USER_ID, -1) else null
    }

    fun isLoggedIn(): Boolean = _currentUserId.value != null

    fun login(userId: Int) {
        _currentUserId.value = userId
        prefs?.edit()?.putInt(KEY_USER_ID, userId)?.apply()
    }

    fun logout() {
        _currentUserId.value = null
        prefs?.edit()?.remove(KEY_USER_ID)?.apply()
    }
}