package com.example.doslenguasapp.model

// Las frases de la app, en un solo lugar. Inicio, Detalle y Mis frases leen de aquí.
// (Lo que se guarda en Room no son las frases, sino si cada una está aprendida o es favorita.)
object PhraseCatalog {

    val phrases: List<Phrase> = listOf(
        Phrase(
            id = 1,
            spanish = "¿Dónde estoy?",
            turkish = "Neredeyim?",
            intro = "Cuando estás de viaje, esta pregunta te puede sacar de más de un apuro."
        ),
        Phrase(
            id = 2,
            spanish = "Hola, ¿cómo estás?",
            turkish = "Merhaba, nasılsın?",
            intro = "Una de las frases más importantes en cada idioma es saludar y preguntar cómo está la otra persona."
        ),
        Phrase(
            id = 3,
            spanish = "Gracias",
            turkish = "Teşekkürler",
            intro = "Dar las gracias abre puertas en cualquier idioma."
        )
    )

    // Devuelve la frase con ese id, o null si no existe.
    fun byId(id: Int): Phrase? = phrases.firstOrNull { it.id == id }
}