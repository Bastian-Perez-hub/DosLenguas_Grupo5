<p align="center">
  <img src="app/src/main/ic_launcher-playstore.png" alt="Logo de DosLenguas" width="160">
</p>

# DosLenguasApp

App Android para **aprender turco y español** a través de frases cotidianas. No es un traductor: muestra frases ya cargadas (por ejemplo "¿Dónde estoy?" o "Gracias") y enseña su traducción con pronunciación en audio. Al crear la cuenta se elige la dirección: **Español → Turco** (por defecto) o **Turco → Español** (marcando la casilla).

Proyecto de la **Evaluación Parcial 2** de la asignatura Desarrollo de Aplicaciones Móviles (DSY1105), Duoc UC.

## Integrantes

- Bastian Perez (GitHub: [Bastian-Perez-hub](https://github.com/Bastian-Perez-hub))
- Jonas Valencia (GitHub: [jonasvalenciat](https://github.com/jonasvalenciat))

**Equipo:** Grupo 5

## Tecnologías

- Kotlin
- Jetpack Compose con Material 3
- Arquitectura MVVM
- Navigation Compose
- Room (base de datos local)
- Retrofit + Gson (consulta a una API REST)
- Coil (carga de imágenes)
- Text-to-Speech de Android (audio en turco)

## Funcionalidades

- **Registro e inicio de sesión** con validación por campo (íconos y mensajes de error). Las validaciones están en el ViewModel, no en la pantalla. Al final del registro hay una casilla para elegir la dirección (sin marcar = Español → Turco, marcada = Turco → Español). Aparece un aviso de "cuenta creada" antes de volver al Login.
- **Dirección de aprendizaje:** la elección se guarda en la cuenta (Room) y en la sesión, e invierte el origen y el destino en Inicio, Detalle, Flashcards y Mis frases. El audio usa la voz del idioma de destino (turco o español). Se puede cambiar después en **Mi perfil > Editar datos**.
- **Sesión persistente:** si ya iniciaste sesión, la app abre directo en Inicio.
- **Inicio:** saludo con el nombre del usuario y progreso "X de Y frases".
- **Detalle de frase:** texto en español y turco, audio de pronunciación, botón "¡Ya la aprendí!" y marcado como favorita (corazón).
- **Mis frases:** lista de frases aprendidas y favoritas, guardadas en Room.
- **Flashcards:** tarjetas de memoria con animación de giro; muestran el idioma de origen por un lado y el de destino (con audio) por el otro, según la dirección elegida.
- **Conexión:** pantalla con un botón "Consultar API" que pide un dato a JSONPlaceholder (`GET /todos/1`) y lo muestra en una tarjeta. Es un ejemplo de consumo de API con Retrofit.
- **Perfil:** datos del usuario, modo de aprendizaje actual, contadores de frases aprendidas y favoritas, y foto de perfil tomada con la cámara o elegida desde la galería. Se solicita el permiso de cámara y se muestra un aviso si se deniega.
- **Editar datos:** cambio de nombre, correo y contraseña (opcional) con validaciones.
- **Menú lateral** y navegación entre todas las pantallas (Inicio, Mis frases, Flashcards, Mi perfil y Cerrar sesión).
- **Vibración** al marcar una frase como aprendida o favorita.
- **Identidad visual propia:** logo de DosLenguas como ícono de la app y en las pantallas de Login y Registro, con tema Material 3 en naranja pastel y fondo crema.

## Recursos nativos

| Recurso | Dónde se usa | Permiso |
|---|---|---|
| Cámara y galería | Foto de perfil | `CAMERA` (se pide en tiempo de ejecución) |
| Vibración | Detalle de frase | `VIBRATE` |
| Text-to-Speech | Audio en turco o español (según la dirección) | Ninguno |

## Estructura del proyecto

```
app/src/main/java/com/example/doslenguasapp/
├── data/        Room: entidades, DAOs, base de datos y repositorios
│   └── remote/  Cliente Retrofit, DTO y repositorio de la API
├── model/       Modelo de frase y catálogo de frases
├── navigation/  Rutas y NavHost
├── ui/
│   ├── components/  Componentes reutilizables (AppTextField, AppButton, PhraseCard, menú lateral)
│   ├── screens/     Pantallas
│   └── theme/       Tema Material 3
├── utils/       SessionManager, TextToSpeechManager, VibrationHelper
└── viewmodel/   ViewModels y validaciones (AuthValidator)
```

## Tests unitarios

Se ejecutan en la PC (sin emulador) con **Kotest + MockK**:

- `DetailSpect.kt`: comprueba que los botones "¡Ya la aprendí!" y el corazón del detalle envían el recado al repositorio (`coVerify`).
- `ConnectionSpect.kt`: comprueba que "Consultar API" llama a la API simulada y muestra el dato, y que un fallo muestra el mensaje de error.
- `LearningDirectionSpect.kt`: comprueba que la dirección elegida invierte origen, destino y voz, y que las cuentas antiguas mantienen Español → Turco.

Para ejecutarlos:

```
.\gradlew.bat testDebugUnitTest
```

El reporte queda en `app/build/reports/tests/testDebugUnitTest/index.html`.

## Cómo ejecutar

1. Clonar el repositorio:
```
   git clone https://github.com/Bastian-Perez-hub/DosLenguas_Grupo5.git
```
2. Abrir la carpeta del proyecto en **Android Studio**.
3. Esperar a que termine la sincronización de Gradle.
4. Elegir un emulador o un celular con **Android 13 (API 33) o superior**.
5. Presionar **Run** (▶).

Para usar la cámara conviene probar en un celular real o en un emulador con cámara configurada. El audio requiere que el dispositivo tenga instalada la voz de Google Text-to-Speech del idioma de destino (tr-TR o es-ES).

## Primeros pasos en la app

1. Crear una cuenta en **Registro** (contraseña de al menos 8 caracteres, con letra y número) y marcar la casilla solo si quieres aprender **Turco → Español**.
2. Iniciar sesión con ese correo y contraseña.
3. Elegir una frase en Inicio, escucharla y marcarla como aprendida.
4. Si quieres cambiar de dirección, ve a **Mi perfil > Editar datos** y guarda los cambios.

## Notas

- Las frases vienen cargadas en la app (por ahora son 3); no hay formulario para agregar nuevas.
- La contraseña se guarda sin cifrar en la base local, lo cual es aceptable solo para fines académicos.
- La foto de perfil no se conserva al cerrar la app (mejora futura).