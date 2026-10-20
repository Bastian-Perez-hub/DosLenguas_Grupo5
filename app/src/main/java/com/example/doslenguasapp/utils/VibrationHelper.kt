package com.example.doslenguasapp.utils


import android.content.Context
import android.os.VibrationEffect
import android.os.VibratorManager

// Centraliza la vibración del teléfono (recurso nativo).
object VibrationHelper {

    fun vibrateSuccess(context: Context) {
        val manager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE)
                as VibratorManager
        val vibrator = manager.defaultVibrator
        if (vibrator.hasVibrator()) {
            // Una vibración corta de 150 ms
            vibrator.vibrate(
                VibrationEffect.createOneShot(150, VibrationEffect.DEFAULT_AMPLITUDE)
            )
        }
    }
}