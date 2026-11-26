package com.example.doslenguasapp.utils

import android.content.Context
import com.example.doslenguasapp.model.LearningDirection
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

// Guarda quién inició sesión y en qué dirección quiere aprender.
// Ambos datos se persisten en el teléfono.
object SessionManager {

    private const val PREFS_NAME = "session_prefs"
    private const val KEY_USER_ID = "user_id"
    private const val KEY_DIRECTION = "learning_direction"

    private var prefs: android.content.SharedPreferences? = null

    private val _currentUserId = MutableStateFlow<Int?>(null)
    val currentUserId: StateFlow<Int?> = _currentUserId.asStateFlow()

    private val _direction = MutableStateFlow(LearningDirection.ES_TO_TR)
    val direction: StateFlow<LearningDirection> = _direction.asStateFlow()

    // Se llama una vez al abrir la app: recupera la sesión guardada (si existe).
    fun init(context: Context) {
        if (prefs != null) return
        val p = context.applicationContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs = p
        _currentUserId.value = if (p.contains(KEY_USER_ID)) p.getInt(KEY_USER_ID, -1) else null
        _direction.value = LearningDirection.fromStored(p.getString(KEY_DIRECTION, null))
    }

    fun isLoggedIn(): Boolean = _currentUserId.value != null

    fun login(userId: Int, direction: LearningDirection = LearningDirection.ES_TO_TR) {
        _currentUserId.value = userId
        setDirection(direction)
        prefs?.edit()?.putInt(KEY_USER_ID, userId)?.apply()
    }

    fun setDirection(direction: LearningDirection) {
        _direction.value = direction
        prefs?.edit()?.putString(KEY_DIRECTION, direction.name)?.apply()
    }

    fun logout() {
        _currentUserId.value = null
        _direction.value = LearningDirection.ES_TO_TR
        prefs?.edit()?.remove(KEY_USER_ID)?.remove(KEY_DIRECTION)?.apply()
    }
}
