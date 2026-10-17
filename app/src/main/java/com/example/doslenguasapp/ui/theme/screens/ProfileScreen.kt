package com.example.doslenguasapp.ui.theme.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.doslenguasapp.ui.theme.DosLenguasAppTheme

// Pantalla estática (Guía 8): sin cámara, permisos ni navegación todavía.
// Semana 8: nombre y correo vendrán del registro. Semana 10: cámara/galería con permisos.
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    name: String = "Bastián Pérez",
    email: String = "bastian@correo.com",
    learnedCount: Int = 3,
    favoritesCount: Int = 2
) {
    // Estado solo visual: si el cuadro "¿De dónde quieres la foto?" está abierto
    var showPhotoDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mi perfil") },
                navigationIcon = {
                    // Semana 7: aquí se abre el menú lateral
                    IconButton(onClick = { }) {
                        Icon(Icons.Default.Menu, contentDescription = "Abrir menú")
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
            // Foto circular: mientras no haya foto, se muestra un ícono de persona
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primaryContainer),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.Person,
                    contentDescription = "Foto de perfil",
                    modifier = Modifier.size(64.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
            }

            OutlinedButton(onClick = { showPhotoDialog = true }) {
                Text("Cambiar foto")
            }

            Text(name, style = MaterialTheme.typography.headlineMedium)
            Text(
                email,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            // Semana 8: abre la pantalla Editar datos
            Button(onClick = { }) {
                Icon(Icons.Default.Edit, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Editar datos")
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                SummaryCard(
                    number = learnedCount,
                    label = "Aprendidas",
                    modifier = Modifier.weight(1f)
                )
                SummaryCard(
                    number = favoritesCount,
                    label = "Favoritas",
                    modifier = Modifier.weight(1f)
                )
            }

            // Semana 7: vuelve al Login
            OutlinedButton(onClick = { }, modifier = Modifier.fillMaxWidth()) {
                Text("Cerrar sesión")
            }
        }
    }

    if (showPhotoDialog) {
        AlertDialog(
            onDismissRequest = { showPhotoDialog = false },
            title = { Text("¿De dónde quieres la foto?") },
            text = { Text("Puedes tomar una foto nueva o elegir una de tu galería.") },
            confirmButton = {
                // Semana 10: abre la cámara (con permiso)
                TextButton(onClick = { showPhotoDialog = false }) { Text("Cámara") }
            },
            dismissButton = {
                // Semana 10: abre la galería
                TextButton(onClick = { showPhotoDialog = false }) { Text("Galería") }
            }
        )
    }
}

// Tarjeta pequeña de resumen. En la semana 7 se puede mover a la carpeta components.
@Composable
private fun SummaryCard(number: Int, label: String, modifier: Modifier = Modifier) {
    Card(modifier = modifier) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(number.toString(), style = MaterialTheme.typography.headlineLarge)
            Text(label, style = MaterialTheme.typography.bodyMedium, textAlign = TextAlign.Center)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProfileScreenPreview() {
    DosLenguasAppTheme {
        ProfileScreen()
    }
}