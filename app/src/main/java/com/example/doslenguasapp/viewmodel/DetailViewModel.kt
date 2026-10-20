package com.example.doslenguasapp.viewmodel

import androidx.lifecycle.SavedStateHandle
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

data class DetailUiState(
    val phrase: Phrase? = null,
    val learned: Boolean = false
)

class DetailViewModel(
    savedStateHandle: SavedStateHandle,
    private val progressRepository: PhraseProgressRepository
) : ViewModel() {

    // SavedStateHandle trae los argumentos de la ruta ("detail/{phraseId}").
    private val phraseId: Int = savedStateHandle["phraseId"] ?: -1

    private val _state = MutableStateFlow(
        DetailUiState(phrase = PhraseCatalog.byId(phraseId))
    )
    val state: StateFlow<DetailUiState> = _state.asStateFlow()

    init {
        // Observamos el progreso de ESTA frase en Room. Si todavía no hay fila, no está aprendida.
        viewModelScope.launch {
            progressRepository.observe(phraseId).collect { progress ->
                _state.update { it.copy(learned = progress?.learned == true) }
            }
        }
    }

    fun onLearnedClick() {
        viewModelScope.launch {
            progressRepository.markLearned(phraseId)
        }
    }
}