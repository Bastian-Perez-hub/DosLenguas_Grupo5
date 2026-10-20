package com.example.doslenguasapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.doslenguasapp.model.PhraseCatalog
import com.example.doslenguasapp.ui.components.PhraseCard
import com.example.doslenguasapp.ui.theme.DosLenguasAppTheme
import com.example.doslenguasapp.ui.components.AppButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onMenuClick: () -> Unit = {},
    onGoToMyPhrases: () -> Unit = {},
    onOpenPhrase: (Int) -> Unit = {}
) {
    val phrases = PhraseCatalog.phrases
    val phraseOfTheDay = phrases.first()
    val otherPhrases = phrases.drop(1)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("DosLenguas") },
                navigationIcon = {
                    IconButton(onClick = onMenuClick) {
                        Icon(Icons.Default.Menu, contentDescription = "Abrir menú")
                    }
                }
            )
        },
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = true,
                    onClick = { },
                    icon = { Icon(Icons.Default.Home, contentDescription = null) },
                    label = { Text("Inicio") }
                )
                NavigationBarItem(
                    selected = false,
                    onClick = onGoToMyPhrases,
                    icon = { Icon(Icons.Default.Star, contentDescription = null) },
                    label = { Text("Mis frases") }
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text("¡Hola, Bastián! 👋", style = MaterialTheme.typography.headlineMedium)
            Text("¿Qué frase aprendemos hoy?", style = MaterialTheme.typography.bodyLarge)

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Progreso de hoy", style = MaterialTheme.typography.titleMedium)
                LinearProgressIndicator(
                    progress = { 2 / 5f },
                    modifier = Modifier.fillMaxWidth()
                )
                Text("2 de 5 frases", style = MaterialTheme.typography.bodyMedium)
            }

            Card(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text("FRASE DEL DÍA", style = MaterialTheme.typography.labelLarge)
                    Text(
                        "\"${phraseOfTheDay.spanish}\"",
                        style = MaterialTheme.typography.headlineMedium
                    )
                    AppButton(
                        text = "Ver en turco",
                        onClick = { onOpenPhrase(phraseOfTheDay.id) }
                    )
                }
            }

            Text("Otras frases", style = MaterialTheme.typography.titleMedium)
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                otherPhrases.forEach { phrase ->
                    PhraseCard(
                        text = phrase.spanish,
                        onClick = { onOpenPhrase(phrase.id) },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    DosLenguasAppTheme {
        HomeScreen()
    }
}