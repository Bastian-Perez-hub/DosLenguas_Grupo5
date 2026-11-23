package com.example.doslenguasapp.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.doslenguasapp.ui.components.AppButton
import com.example.doslenguasapp.viewmodel.AppViewModelFactory
import com.example.doslenguasapp.viewmodel.ConnectionViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConnectionScreen(
    onMenuClick: () -> Unit = {},
    viewModel: ConnectionViewModel = viewModel(factory = AppViewModelFactory(LocalContext.current))
) {
    val state by viewModel.state.collectAsState()
    val todo = state.todo

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Conexión") },
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
            Text(
                text = "Prueba de conexión con una API pública",
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center
            )

            AppButton(
                text = "Consultar API",
                onClick = viewModel::loadTodo,
                enabled = !state.loading,
                icon = Icons.Default.Refresh
            )

            when {
                state.loading -> {
                    CircularProgressIndicator()
                    Text("Consultando...", style = MaterialTheme.typography.bodyMedium)
                }

                state.error != null -> {
                    Card(modifier = Modifier.fillMaxWidth()) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("Error", style = MaterialTheme.typography.titleMedium)
                            Spacer(Modifier.height(8.dp))
                            Text(
                                text = state.error ?: "",
                                color = MaterialTheme.colorScheme.error,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }

                todo != null -> {
                    Card(modifier = Modifier.fillMaxWidth()) {
                        Column(
                            modifier = Modifier.padding(20.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text("Datos recibidos", style = MaterialTheme.typography.titleMedium)
                            Text("ID: ${todo.id}", style = MaterialTheme.typography.bodyLarge)
                            Text("Usuario: ${todo.userId}", style = MaterialTheme.typography.bodyMedium)
                            Text("Título: ${todo.title}", style = MaterialTheme.typography.bodyLarge)
                            Text(
                                text = if (todo.completed) "✅ Completada" else "⬜ Pendiente",
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }

                else -> {
                    Text(
                        text = "Todavía no hay datos. Toca «Consultar API».",
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}
