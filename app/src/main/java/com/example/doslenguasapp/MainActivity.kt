package com.example.doslenguasapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.doslenguasapp.navigation.AppNavHost
import com.example.doslenguasapp.ui.theme.DosLenguasAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DosLenguasAppTheme {
                AppNavHost()
            }
        }
    }
}