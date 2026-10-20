package com.example.doslenguasapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.doslenguasapp.data.PhraseProgressRepository
import com.example.doslenguasapp.model.Phrase
import com.example.doslenguasapp.model.PhraseCatalog
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class MyPhrasesUiState(
    val learned: List<Phrase> = emptyList(),
    val favorites: List<Phrase> = emptyList()
)

class MyPhrasesViewModel(
    private val progressRepository: PhraseProgressRepository
) : ViewModel() {

    private val _state = MutableStateFlow(MyPhrasesUiState())
    val state: StateFlow<MyPhrasesUiState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            // observeAll es un Flow: cada vez que cambia el progreso en Room,
            // esto se ejecuta de nuevo y la lista se actualiza sola.
            progressRepository.observeAll().collect { progressList ->
                val learnedIds = progressList.filter { it.learned }.map { it.phraseId }.toSet()
                val favoriteIds = progressList.filter { it.favorite }.map { it.phraseId }.toSet()

                // Room guarda solo el estado; las frases completas salen del catálogo.
                _state.update {
                    it.copy(
                        learned = PhraseCatalog.phrases.filter { p -> p.id in learnedIds },
                        favorites = PhraseCatalog.phrases.filter { p -> p.id in favoriteIds }
                    )
                }
            }
        }
    }
}