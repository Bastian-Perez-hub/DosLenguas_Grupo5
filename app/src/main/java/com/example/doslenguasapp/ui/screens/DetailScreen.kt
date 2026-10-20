package com.example.doslenguasapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.doslenguasapp.ui.theme.DosLenguasAppTheme
import com.example.doslenguasapp.ui.components.AppButton
import com.example.doslenguasapp.viewmodel.AppViewModelFactory
import com.example.doslenguasapp.viewmodel.DetailViewModel

// Semana 9: la frase llega por la ruta y "aprendida" se guarda en Room.
// Semana 10: vibración y mensaje animado al marcarla.
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    onBack: () -> Unit = {},
    viewModel: DetailViewModel = viewModel(factory = AppViewModelFactory(LocalContext.current))
) {
    // La pantalla solo observa el estado; los datos y las acciones están en el ViewModel.
    val state by viewModel.state.collectAsState()
    val phrase = state.phrase

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
        if (phrase == null) {
            // No debería pasar (los ids salen del catálogo), pero así la pantalla no queda en blanco.
            Text(
                text = "No encontramos esta frase.",
                modifier = Modifier
                    .padding(innerPadding)
                    .padding(16.dp)
            )
        } else {
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
                    text = phrase.intro,
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center
                )

                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text("Español", style = MaterialTheme.typography.labelLarge)
                        Text("\"${phrase.spanish}\"", style = MaterialTheme.typography.headlineSmall)
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
                        Text("\"${phrase.turkish}\"", style = MaterialTheme.typography.headlineMedium)
                    }
                }

                Spacer(modifier = Modifier.weight(1f))

                // Si ya está aprendida, el botón lo refleja y se desactiva.
                AppButton(
                    text = if (state.learned) "¡Ya la aprendiste!" else "¡Ya la aprendí!",
                    onClick = viewModel::onLearnedClick,
                    enabled = !state.learned,
                    icon = if (state.learned) Icons.Default.Check else null
                )
            }
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