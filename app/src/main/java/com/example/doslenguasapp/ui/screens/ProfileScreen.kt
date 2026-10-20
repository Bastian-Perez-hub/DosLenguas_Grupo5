package com.example.doslenguasapp.ui.screens

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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.doslenguasapp.ui.theme.DosLenguasAppTheme
import com.example.doslenguasapp.ui.components.AppButton
import com.example.doslenguasapp.viewmodel.AppViewModelFactory
import com.example.doslenguasapp.viewmodel.ProfileViewModel

// Semana 9: nombre y correo vienen de Room (usuario con sesión iniciada).
// Semana 10: cámara/galería con permisos.
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    onMenuClick: () -> Unit = {},
    onLogout: () -> Unit = {},
    onEditProfile: () -> Unit = {},
    viewModel: ProfileViewModel = viewModel(factory = AppViewModelFactory(LocalContext.current))
) {
    // La pantalla solo observa el estado; los datos los entrega el ViewModel.
    val state by viewModel.state.collectAsState()

    // Estado solo visual: si el cuadro "¿De dónde quieres la foto?" está abierto
    var showPhotoDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mi perfil") },
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

            AppButton(
                text = "Cambiar foto",
                onClick = { showPhotoDialog = true },
                outlined = true
            )

            Text(state.nombre, style = MaterialTheme.typography.headlineMedium)
            Text(
                state.email,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            AppButton(
                text = "Editar datos",
                onClick = onEditProfile,
                icon = Icons.Default.Edit
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                SummaryCard(
                    number = state.learnedCount,
                    label = "Aprendidas",
                    modifier = Modifier.weight(1f)
                )
                SummaryCard(
                    number = state.favoritesCount,
                    label = "Favoritas",
                    modifier = Modifier.weight(1f)
                )
            }

            // Vuelve al Login (la sesión se limpia en AppNavHost)
            AppButton(
                text = "Cerrar sesión",
                onClick = { onLogout() },
                outlined = true
            )
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