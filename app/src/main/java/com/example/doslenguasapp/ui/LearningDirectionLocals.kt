package com.example.doslenguasapp.ui

import androidx.compose.runtime.compositionLocalOf
import com.example.doslenguasapp.model.LearningDirection

// La dirección elegida se reparte a las pantallas desde el NavHost.
// Si cambia desde Mi perfil, Compose vuelve a dibujarlas automáticamente.
val LocalLearningDirection = compositionLocalOf<LearningDirection> {
    LearningDirection.ES_TO_TR
}
