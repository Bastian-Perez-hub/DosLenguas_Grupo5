package com.example.doslenguasapp.ui.theme.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.doslenguasapp.ui.theme.DosLenguasAppTheme
import com.example.doslenguasapp.ui.theme.components.AppButton
import com.example.doslenguasapp.ui.theme.components.AppTextField
import com.example.doslenguasapp.viewmodel.EditProfileViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(
    onBack: () -> Unit = {},
    onSaved: () -> Unit = {},
    viewModel: EditProfileViewModel = viewModel()
) {
    // La pantalla solo observa el estado; no valida nada por su cuenta.
    val state by viewModel.state.collectAsState()

    // Cuando el ViewModel marca el guardado como válido, avisamos hacia afuera.
    LaunchedEffect(state.guardadoValido) {
        if (state.guardadoValido) onSaved()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Editar datos") },
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
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            AppTextField(
                value = state.nombre,
                onValueChange = viewModel::onNombreChange,
                label = "Nombre",
                leadingIcon = Icons.Default.Person,
                error = state.nombreError
            )

            AppTextField(
                value = state.email,
                onValueChange = viewModel::onEmailChange,
                label = "Correo",
                leadingIcon = Icons.Default.Email,
                error = state.emailError,
                keyboardType = KeyboardType.Email
            )

            Text(
                text = "Deja las contraseñas vacías si no quieres cambiarla.",
                style = MaterialTheme.typography.bodyMedium
            )

            AppTextField(
                value = state.password,
                onValueChange = viewModel::onPasswordChange,
                label = "Nueva contraseña (opcional)",
                leadingIcon = Icons.Default.Lock,
                error = state.passwordError,
                isPassword = true
            )

            AppTextField(
                value = state.confirmPassword,
                onValueChange = viewModel::onConfirmPasswordChange,
                label = "Confirmar nueva contraseña",
                leadingIcon = Icons.Default.Lock,
                error = state.confirmPasswordError,
                isPassword = true,
                imeAction = ImeAction.Done
            )

            AppButton(
                text = "Guardar cambios",
                onClick = viewModel::onGuardarClick
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun EditProfileScreenPreview() {
    DosLenguasAppTheme {
        EditProfileScreen()
    }
}