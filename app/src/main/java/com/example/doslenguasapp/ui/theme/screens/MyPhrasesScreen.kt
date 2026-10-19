package com.example.doslenguasapp.ui.theme.screens



import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.doslenguasapp.ui.theme.DosLenguasAppTheme
import com.example.doslenguasapp.ui.theme.components.AppButton

// Pantalla estática (Guía 8): datos de ejemplo, sin Room ni navegación.
// Semana 9: las listas pasarán a venir de la base de datos vía ViewModel.
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyPhrasesScreen(
    learned: List<Pair<String, String>> = listOf(
        "Hola, ¿cómo estás?" to "Merhaba, nasılsın?",
        "¿Dónde estoy?" to "Neredeyim?",
        "Gracias" to "Teşekkürler"
    ),
    favorites: List<Pair<String, String>> = listOf(
        "Gracias" to "Teşekkürler",
        "¿Dónde estoy?" to "Neredeyim?"
    ),
    onMenuClick: () -> Unit = {},
    onGoToHome: () -> Unit = {}
) {
    // Estado solo visual: qué pestaña está seleccionada (0 = Aprendidas, 1 = Favoritas)
    var selectedTab by remember { mutableIntStateOf(0) }
    val tabs = listOf("Aprendidas", "Favoritas")
    val currentList = if (selectedTab == 0) learned else favorites

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mis frases") },
                navigationIcon = {
                    IconButton(onClick = onMenuClick) {
                        Icon(Icons.Default.Menu, contentDescription = "Abrir menú")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            Text(
                text = when (learned.size) {
                    0 -> "Todavía no has aprendido frases. ¡Tú puedes! 💪"
                    1 -> "¡Muy bien! Llevas 1 frase aprendida 🎉"
                    else -> "¡Vas muy bien! Llevas ${learned.size} frases aprendidas 🎉"
                },
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(16.dp)
            )

            TabRow(selectedTabIndex = selectedTab) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        text = { Text(title) }
                    )
                }
            }

            if (currentList.isEmpty()) {
                // Estado vacío
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text("📖", fontSize = 64.sp)
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Aún no has aprendido frases. ¡Empecemos por la de hoy!",
                        style = MaterialTheme.typography.bodyLarge,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    // Semana 7: aquí se navega al Inicio
                    AppButton(
                        text = "Ir al inicio",
                        onClick = { onGoToHome }
                    )
                }
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    currentList.forEach { (spanish, turkish) ->
                        Card(modifier = Modifier.fillMaxWidth()) {
                            Row(
                                modifier = Modifier.padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(
                                    modifier = Modifier.weight(1f),
                                    verticalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Text(spanish, style = MaterialTheme.typography.titleMedium)
                                    Text(
                                        turkish,
                                        style = MaterialTheme.typography.bodyLarge,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                }
                                Icon(
                                    Icons.Default.Check,
                                    contentDescription = "Aprendida",
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MyPhrasesScreenPreview() {
    DosLenguasAppTheme {
        MyPhrasesScreen()
    }
}

@Preview(showBackground = true)
@Composable
fun MyPhrasesScreenEmptyPreview() {
    DosLenguasAppTheme {
        MyPhrasesScreen(learned = emptyList(), favorites = emptyList())

    }
}