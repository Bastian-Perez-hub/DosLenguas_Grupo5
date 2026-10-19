package com.example.doslenguasapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.doslenguasapp.ui.theme.DosLenguasAppTheme
import com.example.doslenguasapp.ui.components.AppButton

// Pantalla estática (Guía 8): solo diseño, sin navegación, ViewModel ni Room.
// Los textos llegan como parámetros para que el molde sirva para cualquier frase.
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    intro: String = "Una de las frases más importantes en cada idioma es saludar...",
    spanish: String = "Hola, ¿cómo estás?",
    turkish: String = "Merhaba, nasılsın?",
    onBack: () -> Unit = {}
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalle de frase") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Espacio de la imagen: por ahora un emoji. La animación de saludo va en la semana 10.
            Text("👋", fontSize = 72.sp)

            Text(
                text = intro,
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center
            )

            Card(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text("Español", style = MaterialTheme.typography.labelLarge)
                    Text("\"$spanish\"", style = MaterialTheme.typography.headlineSmall)
                }
            }

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text("Turco", style = MaterialTheme.typography.labelLarge)
                    Text("\"$turkish\"", style = MaterialTheme.typography.headlineMedium)
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            // Semana 9-10: aquí se guarda en Room, vibra y aparece el mensaje animado
            AppButton(
                text = "¡Ya la aprendí!",
                onClick = { }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DetailScreenPreview() {
    DosLenguasAppTheme {
        DetailScreen()
    }
}