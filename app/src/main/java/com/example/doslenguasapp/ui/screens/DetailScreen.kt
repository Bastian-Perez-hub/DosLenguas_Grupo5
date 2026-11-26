package com.example.doslenguasapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
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
import com.example.doslenguasapp.model.view
import com.example.doslenguasapp.ui.LocalLearningDirection
import com.example.doslenguasapp.ui.theme.DosLenguasAppTheme
import com.example.doslenguasapp.ui.components.AppButton
import com.example.doslenguasapp.viewmodel.AppViewModelFactory
import com.example.doslenguasapp.viewmodel.DetailViewModel
import com.example.doslenguasapp.utils.VibrationHelper
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    onBack: () -> Unit = {},
    onSpeak: (String, Locale) -> Unit = { _, _ -> }, // Audio en el idioma de destino
    viewModel: DetailViewModel = viewModel(factory = AppViewModelFactory(LocalContext.current))
) {
    val state by viewModel.state.collectAsState()
    val phrase = state.phrase
    val direction = LocalLearningDirection.current
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalle de frase") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                },
                actions = {
                    if (phrase != null) {
                        IconButton(onClick = {
                            viewModel.onFavoriteClick()
                            if (!state.favorite) {
                                VibrationHelper.vibrateSuccess(context)
                            }
                        }) {
                            Icon(
                                imageVector = if (state.favorite) Icons.Default.Favorite
                                else Icons.Default.FavoriteBorder,
                                contentDescription = "Favorito",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }
            )
        }
    ) { innerPadding ->
        if (phrase == null) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            val vista = phrase.view(direction)
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .padding(16.dp)
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text("👋", fontSize = 72.sp)

                Text(
                    text = phrase.intro,
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center
                )

                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Text(vista.sourceLabel, style = MaterialTheme.typography.labelLarge)
                        Text("\"${vista.source}\"", style = MaterialTheme.typography.headlineSmall)
                    }
                }

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
                ) {
                    Row(
                        modifier = Modifier.padding(20.dp).fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(vista.targetLabel, style = MaterialTheme.typography.labelLarge)
                            Text("\"${vista.target}\"", style = MaterialTheme.typography.headlineMedium)
                        }
                        IconButton(onClick = { onSpeak(vista.target, vista.targetLocale) }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.VolumeUp,
                                contentDescription = "Oír",
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(32.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.weight(1f))

                AppButton(
                    text = if (state.learned) "¡Ya la aprendiste!" else "¡Ya la aprendí!",
                    onClick = {
                        viewModel.onLearnedClick()
                        VibrationHelper.vibrateSuccess(context)
                    },
                    enabled = !state.learned,
                    icon = if (state.learned) Icons.Default.Check else null
                )
            }
        }
    }
}
