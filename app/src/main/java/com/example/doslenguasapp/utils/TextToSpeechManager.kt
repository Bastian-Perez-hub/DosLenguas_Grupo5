package com.example.doslenguasapp.utils

import android.content.Context
import android.speech.tts.TextToSpeech
import android.util.Log
import java.util.Locale

/**
 * Gestor de Texto a Voz (TTS). Pronuncia el idioma de destino elegido
 * (turco en Español → Turco, español en Turco → Español).
 */
class TextToSpeechManager(context: Context, private val onReady: (Boolean) -> Unit = {}) : TextToSpeech.OnInitListener {

    private var tts: TextToSpeech? = null
    private var isInitialized = false

    init {
        // Inicializamos el motor. 'this' se refiere a esta misma clase (OnInitListener)
        tts = TextToSpeech(context, this)
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            // El idioma se elige en cada speak(), según la dirección de aprendizaje.
            isInitialized = true
            onReady(true)
        } else {
            Log.e("TTS", "Fallo total en la inicialización del motor TTS.")
            isInitialized = false
            onReady(false)
        }
    }

    /**
     * Lee el texto en voz alta con la voz del idioma indicado.
     */
    fun speak(text: String, locale: Locale = Locale.forLanguageTag("tr-TR")) {
        if (isInitialized) {
            val result = tts?.setLanguage(locale)
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.w("TTS", "La voz para ${locale.language} no está disponible en este dispositivo.")
            }
            // QUEUE_FLUSH detiene cualquier audio previo antes de empezar el nuevo
            tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, null)
        } else {
            Log.w("TTS", "Se intentó reproducir audio pero el motor no está listo.")
        }
    }

    /**
     * Libera los recursos del motor de voz.
     * Debe llamarse cuando el componente que lo usa se destruye.
     */
    fun shutdown() {
        tts?.stop()
        tts?.shutdown()
    }
}
