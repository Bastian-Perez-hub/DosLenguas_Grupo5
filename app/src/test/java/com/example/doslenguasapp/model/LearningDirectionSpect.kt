package com.example.doslenguasapp.model

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import java.util.Locale

/**
 * Comprueba que la dirección elegida invierte origen, destino y voz,
 * y que las cuentas antiguas siguen aprendiendo español → turco.
 */
class LearningDirectionSpect : StringSpec({

    val phrase = Phrase(id = 1, spanish = "Gracias", turkish = "Teşekkürler")

    "por defecto se aprende español → turco" {
        val vista = phrase.view(LearningDirection.ES_TO_TR)
        vista.source shouldBe "Gracias"
        vista.target shouldBe "Teşekkürler"
        vista.targetLocale shouldBe Locale.forLanguageTag("tr-TR")
    }

    "con la casilla marcada se aprende turco → español" {
        val vista = phrase.view(LearningDirection.TR_TO_ES)
        vista.source shouldBe "Teşekkürler"
        vista.target shouldBe "Gracias"
        vista.targetLocale shouldBe Locale.forLanguageTag("es-ES")
    }

    "un valor guardado desconocido vuelve a español → turco" {
        LearningDirection.fromStored("OTRO") shouldBe LearningDirection.ES_TO_TR
        LearningDirection.fromStored(LearningDirection.TR_TO_ES.name) shouldBe LearningDirection.TR_TO_ES
    }
})
