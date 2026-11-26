package com.example.doslenguasapp.data

import com.example.doslenguasapp.model.LearningDirection
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

class UserRepository(private val userDao: UserDao) {

    // true si la cuenta se creó; false si ya existía ese correo.
    suspend fun register(
        nombre: String,
        email: String,
        password: String,
        direction: LearningDirection = LearningDirection.ES_TO_TR
    ): Boolean {
        val user = UserEntity(
            nombre = nombre.trim(),
            email = email.trim().lowercase(),
            password = password,
            direction = direction.name
        )
        return userDao.insert(user) != -1L
    }

    // Devuelve el usuario si las credenciales son correctas, o null si no.
    suspend fun login(email: String, password: String): UserEntity? {
        val user = userDao.getByEmail(email.trim().lowercase()) ?: return null
        return if (user.password == password) user else null
    }

    fun observeUser(id: Int): Flow<UserEntity?> = userDao.observeById(id)

    // false si el correo nuevo ya lo usa otra cuenta.
    // Si newPassword viene vacía, se conserva la contraseña actual.
    // Si direction es null, se conserva la dirección actual.
    suspend fun updateProfile(
        id: Int,
        nombre: String,
        email: String,
        newPassword: String,
        direction: LearningDirection? = null
    ): Boolean {
        val current = userDao.observeById(id).first() ?: return false
        val emailNormalizado = email.trim().lowercase()
        val otro = userDao.getByEmail(emailNormalizado)
        if (otro != null && otro.id != id) return false

        userDao.update(
            current.copy(
                nombre = nombre.trim(),
                email = emailNormalizado,
                password = if (newPassword.isEmpty()) current.password else newPassword,
                direction = direction?.name ?: current.direction
            )
        )
        return true
    }
}