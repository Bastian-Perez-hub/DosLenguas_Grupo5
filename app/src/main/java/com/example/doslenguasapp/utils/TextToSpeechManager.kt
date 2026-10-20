package com.example.doslenguasapp.utils

import android.content.Context
import android.speech.tts.TextToSpeech
import android.util.Log
import java.util.Locale

/**
 * Gestor de Texto a Voz (TTS) para centralizar la pronunciación en turco.
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
            // Intentamos configurar el idioma Turco
            val result = tts?.setLanguage(Locale("tr", "TR"))

            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "El idioma Turco no está disponible o no está descargado.")
                isInitialized = false
                onReady(false)
            } else {
                isInitialized = true
                onReady(true)
            }
        } else {
            Log.e("TTS", "Fallo total en la inicialización del motor TTS.")
            isInitialized = false
            onReady(false)
        }
    }

    /**
     * Lee el texto en voz alta.
     */
    fun speak(text: String) {
        if (isInitialized) {
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
