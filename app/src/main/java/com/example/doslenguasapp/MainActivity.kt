package com.example.doslenguasapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.doslenguasapp.navigation.AppNavHost
import com.example.doslenguasapp.ui.theme.DosLenguasAppTheme
import com.example.doslenguasapp.utils.TextToSpeechManager

class MainActivity : ComponentActivity() {
    private var ttsManager: TextToSpeechManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Inicializamos el gestor de audio
        ttsManager = TextToSpeechManager(this)

        setContent {
            DosLenguasAppTheme {
                // Pasamos la función de voz al NavHost
                AppNavHost(onSpeak = { text -> ttsManager?.speak(text) })
            }
        }
    }

    override fun onDestroy() {
        // Liberamos recursos
        ttsManager?.shutdown()
        super.onDestroy()
    }
}