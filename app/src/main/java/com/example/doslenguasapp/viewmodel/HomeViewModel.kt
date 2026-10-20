package com.example.doslenguasapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.doslenguasapp.data.PhraseProgressRepository
import com.example.doslenguasapp.data.UserRepository
import com.example.doslenguasapp.model.Phrase
import com.example.doslenguasapp.model.PhraseCatalog
import com.example.doslenguasapp.utils.SessionManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class HomeUiState(
    val nombre: String = "",
    val phraseOfTheDay: Phrase = PhraseCatalog.phrases.first(),
    val otherPhrases: List<Phrase> = PhraseCatalog.phrases.drop(1),
    val learnedCount: Int = 0,
    val totalCount: Int = PhraseCatalog.phrases.size
)

class HomeViewModel(
    private val userRepository: UserRepository,
    private val progressRepository: PhraseProgressRepository
) : ViewModel() {

    private val _state = MutableStateFlow(HomeUiState())
    val state: StateFlow<HomeUiState> = _state.asStateFlow()

    init {
        // Nombre del usuario con sesión iniciada. Solo usamos el primer nombre para el saludo.
        val userId = SessionManager.currentUserId.value
        if (userId != null) {
            viewModelScope.launch {
                userRepository.observeUser(userId).collect { user ->
                    if (user != null) {
                        val primerNombre = user.nombre.trim().substringBefore(" ")
                        _state.update { it.copy(nombre = primerNombre) }
                    }
                }
            }
        }

        // Progreso: cuántas frases aprendidas hay, se actualiza solo al cambiar Room.
        viewModelScope.launch {
            progressRepository.observeAll().collect { progressList ->
                _state.update { it.copy(learnedCount = progressList.count { p -> p.learned }) }
            }
        }
    }
}