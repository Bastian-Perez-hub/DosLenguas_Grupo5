package com.example.doslenguasapp.ui.screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.doslenguasapp.model.PhraseCatalog
import com.example.doslenguasapp.model.view
import com.example.doslenguasapp.ui.LocalLearningDirection
import com.example.doslenguasapp.ui.theme.DosLenguasAppTheme
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FlashcardsScreen(
    onMenuClick: () -> Unit = {},
    onSpeak: (String, Locale) -> Unit = { _, _ -> }
) {
    // Se usa el mismo catálogo que el resto de la app (antes había una copia aquí).
    val phrases = remember { PhraseCatalog.phrases.shuffled() }
    val direction = LocalLearningDirection.current

    var currentIndex by remember { mutableIntStateOf(0) }
    var rotated by remember { mutableStateOf(false) }

    val rotation by animateFloatAsState(
        targetValue = if (rotated) 180f else 0f,
        animationSpec = tween(durationMillis = 500), label = ""
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Flashcards") },
                navigationIcon = {
                    IconButton(onClick = onMenuClick) {
                        Icon(Icons.Default.Menu, contentDescription = "Menú")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Frase ${currentIndex + 1} de ${phrases.size}",
                style = MaterialTheme.typography.labelLarge
            )

            Spacer(modifier = Modifier.height(24.dp))

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
                    .graphicsLayer {
                        rotationY = rotation
                        cameraDistance = 8 * density
                    }
                    .clickable { rotated = !rotated },
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    if (rotation <= 90f) {
                        Text(
                            text = phrases[currentIndex].view(direction).source,
                            style = MaterialTheme.typography.headlineMedium,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(16.dp)
                        )
                    } else {
                        Column(
                            modifier = Modifier
                                .graphicsLayer { rotationY = 180f }
                                .padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = phrases[currentIndex].view(direction).target,
                                style = MaterialTheme.typography.headlineLarge,
                                color = MaterialTheme.colorScheme.primary,
                                textAlign = TextAlign.Center
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            IconButton(onClick = {
                                val vista = phrases[currentIndex].view(direction)
                                onSpeak(vista.target, vista.targetLocale)
                            }) {
                                Icon(Icons.AutoMirrored.Filled.VolumeUp, contentDescription = "Oír", modifier = Modifier.size(48.dp))
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(48.dp))

            Button(
                onClick = {
                    rotated = false
                    currentIndex = (currentIndex + 1) % phrases.size
                },
                modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp)
            ) {
                Text("Siguiente")
            }
        }
    }
}
