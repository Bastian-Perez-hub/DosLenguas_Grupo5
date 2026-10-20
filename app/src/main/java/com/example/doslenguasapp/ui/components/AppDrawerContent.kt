package com.example.doslenguasapp.ui.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.doslenguasapp.navigation.Routes

/**
 * Menú lateral único de la app. Solo dibuja las opciones y avisa qué se tocó.
 * No sabe cómo navegar: eso lo decide AppNavHost.
 */
@Composable
fun AppDrawerContent(
    currentRoute: String?,
    onNavigate: (String) -> Unit,
    onLogout: () -> Unit
) {
    ModalDrawerSheet {
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            "DosLenguas",
            modifier = Modifier.padding(16.dp),
            style = MaterialTheme.typography.headlineSmall
        )
        HorizontalDivider()
        NavigationDrawerItem(
            label = { Text("Inicio") },
            selected = currentRoute == Routes.HOME,
            onClick = { onNavigate(Routes.HOME) },
            icon = { Icon(Icons.Default.Home, contentDescription = null) }
        )
        NavigationDrawerItem(
            label = { Text("Mis frases") },
            selected = currentRoute == Routes.MY_PHRASES,
            onClick = { onNavigate(Routes.MY_PHRASES) },
            icon = { Icon(Icons.Default.Star, contentDescription = null) }
        )
        NavigationDrawerItem(
            label = { Text("Flashcards") },
            selected = currentRoute == Routes.FLASHCARDS,
            onClick = { onNavigate(Routes.FLASHCARDS) },
            icon = { Icon(Icons.Default.Quiz, contentDescription = null) }
        )
        NavigationDrawerItem(
            label = { Text("Mi perfil") },
            selected = currentRoute == Routes.PROFILE,
            onClick = { onNavigate(Routes.PROFILE) },
            icon = { Icon(Icons.Default.Person, contentDescription = null) }
        )
        HorizontalDivider()
        NavigationDrawerItem(
            label = { Text("Cerrar sesión") },
            selected = false,
            onClick = onLogout,
            icon = { Icon(Icons.AutoMirrored.Filled.ExitToApp, contentDescription = null) }
        )
    }
}