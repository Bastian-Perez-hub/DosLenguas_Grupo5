package com.example.doslenguasapp.ui.theme.screens


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.doslenguasapp.ui.theme.DosLenguasAppTheme
import com.example.doslenguasapp.ui.theme.components.AppTextField
import com.example.doslenguasapp.viewmodel.RegisterViewModel

@Composable
fun RegisterScreen(
    onGoToLogin: () -> Unit = {},   // la navegación real se conecta en la semana 7
    viewModel: RegisterViewModel = viewModel()
) {
    // La pantalla solo observa el estado; no valida nada por su cuenta.
    val state by viewModel.state.collectAsState()

    Scaffold { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Reemplazar por: Image(painterResource(R.drawable.tu_icono), contentDescription = "Logo")
            Surface(
                modifier = Modifier.size(104.dp),
                shape = CircleShape,
                color = MaterialTheme.colorScheme.primaryContainer
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text("Tu icono", style = MaterialTheme.typography.bodyMedium)
                }
            }

            Text("¡Hoş geldin!", style = MaterialTheme.typography.headlineLarge)
            Text(
                text = "Crea tu cuenta para empezar a aprender turco",
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center
            )

            Box(modifier = Modifier.height(8.dp))

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

            AppTextField(
                value = state.password,
                onValueChange = viewModel::onPasswordChange,
                label = "Contraseña",
                leadingIcon = Icons.Default.Lock,
                error = state.passwordError,
                isPassword = true
            )

            AppTextField(
                value = state.confirmPassword,
                onValueChange = viewModel::onConfirmPasswordChange,
                label = "Confirmar contraseña",
                leadingIcon = Icons.Default.Lock,
                error = state.confirmPasswordError,
                isPassword = true,
                imeAction = ImeAction.Done
            )

            Button(
                onClick = viewModel::onRegisterClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = MaterialTheme.shapes.medium
            ) {
                Text("Crear cuenta", style = MaterialTheme.typography.labelLarge)
            }

            TextButton(onClick = onGoToLogin) {
                Text("¿Ya tienes cuenta? Inicia sesión")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RegisterScreenPreview() {
    DosLenguasAppTheme {
        RegisterScreen()
    }
}