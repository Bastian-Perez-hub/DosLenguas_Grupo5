package com.example.doslenguasapp.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.doslenguasapp.data.AppDatabase
import com.example.doslenguasapp.data.PhraseProgressRepository
import com.example.doslenguasapp.data.UserRepository

// Le enseña a Android cómo construir los ViewModels que necesitan repositorios.
class AppViewModelFactory(private val context: Context) : ViewModelProvider.Factory {

    // "by lazy": la base de datos se abre solo cuando alguien la pide.
    private val database by lazy { AppDatabase.getInstance(context) }

    // Esta versión recibe "extras": ahí viene el SavedStateHandle con los argumentos de la ruta.
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
        if (modelClass.isAssignableFrom(DetailViewModel::class.java)) {
            return DetailViewModel(
                savedStateHandle = extras.createSavedStateHandle(),
                progressRepository = PhraseProgressRepository(database.phraseProgressDao())
            ) as T
        }
        return create(modelClass)
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(LoginViewModel::class.java) ->
                LoginViewModel(UserRepository(database.userDao())) as T

            modelClass.isAssignableFrom(RegisterViewModel::class.java) ->
                RegisterViewModel(UserRepository(database.userDao())) as T

            modelClass.isAssignableFrom(ProfileViewModel::class.java) ->
                ProfileViewModel(UserRepository(database.userDao())) as T

            modelClass.isAssignableFrom(EditProfileViewModel::class.java) ->
                EditProfileViewModel(UserRepository(database.userDao())) as T

            modelClass.isAssignableFrom(MyPhrasesViewModel::class.java) ->
                MyPhrasesViewModel(PhraseProgressRepository(database.phraseProgressDao())) as T

            else -> throw IllegalArgumentException("ViewModel desconocido: ${modelClass.name}")
        }
    }
}