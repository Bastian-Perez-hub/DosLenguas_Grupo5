package com.example.doslenguasapp.ui.theme.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.doslenguasapp.model.Phrase
import com.example.doslenguasapp.ui.theme.components.PhraseCard
import com.example.doslenguasapp.ui.theme.DosLenguasAppTheme
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen() {
    val phrases = listOf(
        Phrase(1, "¿Dónde estoy?", "Neredeyim?"),
        Phrase(2, "Hola, ¿cómo estás?", "Merhaba, nasılsın?"),
        Phrase(3, "Gracias", "Teşekkürler")
    )
    val phraseOfTheDay = phrases.first()
    val otherPhrases = phrases.drop(1)

    // Estado para el menú lateral (Hamburguesa)
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
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
                    selected = true,
                    onClick = { scope.launch { drawerState.close() } },
                    icon = { Icon(Icons.Default.Home, contentDescription = null) }
                )
                NavigationDrawerItem(
                    label = { Text("Mis frases") },
                    selected = false,
                    onClick = { /* Semana 7: navegar a Mis frases */ },
                    icon = { Icon(Icons.Default.Star, contentDescription = null) }
                )
                NavigationDrawerItem(
                    label = { Text("Mi perfil") },
                    selected = false,
                    onClick = { /* Semana 7: navegar a Perfil */ },
                    icon = { Icon(Icons.Default.Person, contentDescription = null) }
                )
                HorizontalDivider()
                NavigationDrawerItem(
                    label = { Text("Cerrar sesión") },
                    selected = false,
                    onClick = { /* Semana 7: volver al Login */ },
                    icon = {
                        Icon(Icons.AutoMirrored.Filled.ExitToApp, contentDescription = null)
                    }
                )
            }
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("DosLenguas") },
                    navigationIcon = {
                        IconButton(onClick = {
                            scope.launch { drawerState.open() }
                        }) {
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
                        onClick = { },
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
                        Button(onClick = { }, modifier = Modifier.fillMaxWidth()) {
                            Text("Ver en turco")
                        }
                    }
                }

                Text("Otras frases", style = MaterialTheme.typography.titleMedium)
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    otherPhrases.forEach { phrase ->
                        PhraseCard(text = phrase.spanish, modifier = Modifier.weight(1f))
                    }
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