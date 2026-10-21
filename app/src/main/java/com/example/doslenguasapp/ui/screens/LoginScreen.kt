package com.example.doslenguasapp.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext // NUEVO
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.doslenguasapp.ui.theme.DosLenguasAppTheme
import com.example.doslenguasapp.ui.components.AppButton
import com.example.doslenguasapp.ui.components.AppTextField
import com.example.doslenguasapp.viewmodel.AppViewModelFactory // NUEVO
import com.example.doslenguasapp.viewmodel.LoginViewModel
import androidx.compose.foundation.Image
import androidx.compose.ui.res.painterResource
import com.example.doslenguasapp.R

@Composable
fun LoginScreen(
    onGoToRegister: () -> Unit = {},
    onLoginSuccess: () -> Unit = {},
    // CAMBIÓ: ahora el ViewModel se pide usando la fábrica
    viewModel: LoginViewModel = viewModel(factory = AppViewModelFactory(LocalContext.current))
) {
    // La pantalla solo observa el estado; no valida nada por su cuenta.
    val state by viewModel.state.collectAsState()

    // Cuando el ViewModel marca el login como válido, avisamos hacia afuera.
    LaunchedEffect(state.loginValido) {
        if (state.loginValido) onLoginSuccess()
    }

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
            Image(
                painter = painterResource(id = R.drawable.logo_doslenguas),
                contentDescription = "Logo de DosLenguas",
                modifier = Modifier.size(160.dp)
            )

            Text("¡Merhaba!", style = MaterialTheme.typography.headlineLarge)
            Text(
                text = "Inicia sesión para seguir aprendiendo turco",
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center
            )

            Box(modifier = Modifier.height(8.dp))

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
                isPassword = true,
                imeAction = ImeAction.Done
            )
            AppButton(
                text = "Iniciar sesión",
                onClick = viewModel::onLoginClick
            )

            TextButton(onClick = onGoToRegister) {
                Text(
                    text = "¿No tienes cuenta? Regístrate",
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenpreview() {
    DosLenguasAppTheme {
        LoginScreen()
    }
}