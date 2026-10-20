package com.example.doslenguasapp.ui.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.doslenguasapp.ui.theme.DosLenguasAppTheme
import com.example.doslenguasapp.ui.components.AppButton
import com.example.doslenguasapp.viewmodel.AppViewModelFactory
import com.example.doslenguasapp.viewmodel.ProfileViewModel
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    onMenuClick: () -> Unit = {},
    onLogout: () -> Unit = {},
    onEditProfile: () -> Unit = {},
    viewModel: ProfileViewModel = viewModel(factory = AppViewModelFactory(LocalContext.current))
) {
    val state by viewModel.state.collectAsState()
    val context = LocalContext.current
    var showPhotoDialog by remember { mutableStateOf(false) }
    var showPermissionDenied by remember { mutableStateOf(false) }
    var tempCameraUri by remember { mutableStateOf<Uri?>(null) }

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri != null) viewModel.onPhotoPicked(uri)
    }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success && tempCameraUri != null) {
            viewModel.onPhotoPicked(tempCameraUri)
        }
    }

    // Launcher para pedir permiso de cámara en tiempo real
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            val uri = createImageUri(context)
            tempCameraUri = uri
            cameraLauncher.launch(uri)
        } else {
            showPermissionDenied = true
        }
    }

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
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primaryContainer),
                contentAlignment = Alignment.Center
            ) {
                if (state.photoUri != null) {
                    AsyncImage(
                        model = state.photoUri,
                        contentDescription = "Foto de perfil",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Icon(
                        Icons.Default.Person,
                        contentDescription = "Sin foto de perfil",
                        modifier = Modifier.size(64.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
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
                TextButton(onClick = {
                    showPhotoDialog = false
                    // Pedimos el permiso. Si ya lo tiene, el launcher dispara el éxito directo.
                    permissionLauncher.launch(android.Manifest.permission.CAMERA)
                }) { Text("Cámara") }
            },
            dismissButton = {
                TextButton(onClick = {
                    showPhotoDialog = false
                    galleryLauncher.launch(
                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                    )
                }) { Text("Galería") }
            }

        )
    }
    if (showPermissionDenied) {
        AlertDialog(
            onDismissRequest = { showPermissionDenied = false },
            title = { Text("Permiso de cámara denegado") },
            text = {
                Text(
                    "Sin el permiso no podemos abrir la cámara. " +
                            "Puedes activarlo en Ajustes > Aplicaciones, " +
                            "o elegir una foto de tu galería."
                )
            },
            confirmButton = {
                AppButton(
                    text = "Entendido",
                    onClick = { showPermissionDenied = false }
                )
            }
        )
    }
}

private fun createImageUri(context: android.content.Context): Uri {
    val directory = File(context.filesDir, "fotos")
    if (!directory.exists()) directory.mkdirs()
    val file = File(directory, "perfil_${System.currentTimeMillis()}.jpg")
    return FileProvider.getUriForFile(
        context,
        "${context.packageName}.fileprovider",
        file
    )
}

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
