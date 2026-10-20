package com.example.doslenguasapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey

// Tabla "phrase_progress": guarda si cada frase está aprendida o es favorita.
// Las frases en sí viven en el código; aquí solo se guarda el estado de cada una.
@Entity(tableName = "phrase_progress")
data class PhraseProgressEntity(
    @PrimaryKey val phraseId: Int,
    val learned: Boolean = false,
    val favorite: Boolean = false
)