package com.example.doslenguasapp.model

import java.util.Locale

// Dirección en la que se usa la app. Se guarda como texto en la cuenta (Room).
enum class LearningDirection(val label: String) {
    ES_TO_TR("Español → Turco"),
    TR_TO_ES("Turco → Español");

    companion object {
        // Las cuentas creadas antes de esta versión no tenían el campo:
        // se mantiene el comportamiento original (español → turco).
        fun fromStored(value: String?): LearningDirection =
            values().firstOrNull { it.name == value } ?: ES_TO_TR
    }
}

// Lo que necesitan las pantallas para mostrar una frase según la dirección elegida.
data class PhraseView(
    val source: String,
    val target: String,
    val sourceLabel: String,
    val targetLabel: String,
    val targetLocale: Locale
)

fun Phrase.view(direction: LearningDirection): PhraseView = when (direction) {
    LearningDirection.ES_TO_TR -> PhraseView(
        source = spanish,
        target = turkish,
        sourceLabel = "Español",
        targetLabel = "Turco",
        targetLocale = Locale.forLanguageTag("tr-TR")
    )

    LearningDirection.TR_TO_ES -> PhraseView(
        source = turkish,
        target = spanish,
        sourceLabel = "Turco",
        targetLabel = "Español",
        targetLocale = Locale.forLanguageTag("es-ES")
    )
}
