package com.example.doslenguasapp.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val EsquemaClaro = lightColorScheme(
    primary = NaranjaPastel,
    onPrimary = TextoOscuro,
    primaryContainer = NaranjaSuave,
    onPrimaryContainer = TextoOscuro,
    secondary = AzulSuave,
    onSecondary = TextoOscuro,
    tertiary = VerdeMenta,
    onTertiary = TextoOscuro,
    background = FondoCrema,
    onBackground = TextoOscuro,
    surface = FondoCrema,
    onSurface = TextoOscuro
)

@Composable
fun DosLenguasAppTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = EsquemaClaro,
        typography = Typography,
        content = content,
        shapes = Shapes
    )
}