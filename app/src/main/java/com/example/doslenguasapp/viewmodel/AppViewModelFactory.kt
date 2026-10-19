package com.example.doslenguasapp.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.doslenguasapp.data.AppDatabase
import com.example.doslenguasapp.data.UserRepository

// Le enseña a Android cómo construir los ViewModels que necesitan repositorios.
class AppViewModelFactory(private val context: Context) : ViewModelProvider.Factory {

    // "by lazy": la base de datos se abre solo cuando alguien la pide.
    private val database by lazy { AppDatabase.getInstance(context) }

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(LoginViewModel::class.java) ->
                LoginViewModel(UserRepository(database.userDao())) as T

            else -> throw IllegalArgumentException("ViewModel desconocido: ${modelClass.name}")
        }
    }
}