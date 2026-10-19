package com.example.doslenguasapp

import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.doslenguasapp.navigation.AppNavHost
import com.example.doslenguasapp.ui.theme.DosLenguasAppTheme
import java.util.Locale

class MainActivity : ComponentActivity(), TextToSpeech.OnInitListener {
    private var tts: TextToSpeech? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Inicializamos el motor de Texto a Voz (TTS)
        tts = TextToSpeech(this, this)

        setContent {
            DosLenguasAppTheme {
                // Pasamos la función de voz al NavHost para que llegue a las pantallas
                AppNavHost(onSpeak = { text -> speakTurkish(text) })
            }
        }
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            // Configuramos el idioma a Turco
            val result = tts?.setLanguage(Locale("tr", "TR"))
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "El idioma Turco no está soportado en este dispositivo")
            }
        } else {
            Log.e("TTS", "Fallo al inicializar TextToSpeech")
        }
    }

    private fun speakTurkish(text: String) {
        tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, null)
    }

    override fun onDestroy() {
        // Liberamos los recursos del motor de voz al cerrar la app
        tts?.stop()
        tts?.shutdown()
        super.onDestroy()
    }
}