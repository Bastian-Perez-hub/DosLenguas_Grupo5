<p align="center">
  <img src="app/src/main/ic_launcher-playstore.png" alt="Logo de DosLenguas" width="160">
</p>

# DosLenguasApp

App Android para **aprender turco** a través de frases cotidianas. No es un traductor: muestra frases en español ya cargadas (por ejemplo "¿Dónde estoy?" o "Gracias") y enseña cómo se dicen en turco, con su pronunciación en audio.

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
- Coil (carga de imágenes)
- Text-to-Speech de Android (audio en turco)

## Funcionalidades

- **Registro e inicio de sesión** con validación por campo (íconos y mensajes de error). Las validaciones están en el ViewModel, no en la pantalla. Al registrarse aparece un aviso de "cuenta creada" antes de volver al Login.
- **Sesión persistente:** si ya iniciaste sesión, la app abre directo en Inicio.
- **Inicio:** saludo con el nombre del usuario y progreso "X de Y frases".
- **Detalle de frase:** texto en español y turco, audio de pronunciación, botón "¡Ya la aprendí!" y marcado como favorita (corazón).
- **Mis frases:** lista de frases aprendidas y favoritas, guardadas en Room.
- **Flashcards:** tarjetas de memoria con animación de giro; muestran el español por un lado y el turco (con audio) por el otro.
- **Perfil:** datos del usuario, contadores de frases aprendidas y favoritas, y foto de perfil tomada con la cámara o elegida desde la galería. Se solicita el permiso de cámara y se muestra un aviso si se deniega.
- **Editar datos:** cambio de nombre, correo y contraseña (opcional) con validaciones.
- **Menú lateral** y navegación entre todas las pantallas (Inicio, Mis frases, Flashcards, Mi perfil y Cerrar sesión).
- **Vibración** al marcar una frase como aprendida o favorita.
- **Identidad visual propia:** logo de DosLenguas como ícono de la app y en las pantallas de Login y Registro, con tema Material 3 en naranja pastel y fondo crema.

## Recursos nativos

| Recurso | Dónde se usa | Permiso |
|---|---|---|
| Cámara y galería | Foto de perfil | `CAMERA` (se pide en tiempo de ejecución) |
| Vibración | Detalle de frase | `VIBRATE` |
| Text-to-Speech | Audio en turco | Ninguno |

## Estructura del proyecto

```
app/src/main/java/com/example/doslenguasapp/
├── data/        Room: entidades, DAOs, base de datos y repositorios
├── model/       Modelo de frase y catálogo de frases
├── navigation/  Rutas y NavHost
├── ui/
│   ├── components/  Componentes reutilizables (AppTextField, AppButton, PhraseCard, menú lateral)
│   ├── screens/     Pantallas
│   └── theme/       Tema Material 3
├── utils/       SessionManager, TextToSpeechManager, VibrationHelper
└── viewmodel/   ViewModels y validaciones (AuthValidator)
```

## Cómo ejecutar

1. Clonar el repositorio:
```
   git clone https://github.com/Bastian-Perez-hub/DosLenguas_Grupo5.git
```
2. Abrir la carpeta del proyecto en **Android Studio**.
3. Esperar a que termine la sincronización de Gradle.
4. Elegir un emulador o un celular con **Android 13 (API 33) o superior**.
5. Presionar **Run** (▶).

Para usar la cámara conviene probar en un celular real o en un emulador con cámara configurada. El audio en turco requiere que el dispositivo tenga instalada la voz de turco (tr-TR) de Google Text-to-Speech.

## Primeros pasos en la app

1. Crear una cuenta en **Registro** (contraseña de al menos 8 caracteres, con letra y número).
2. Iniciar sesión con ese correo y contraseña.
3. Elegir una frase en Inicio, escucharla y marcarla como aprendida.

## Notas

- Las frases vienen cargadas en la app (por ahora son 3); no hay formulario para agregar nuevas.
- La contraseña se guarda sin cifrar en la base local, lo cual es aceptable solo para fines académicos.
- La foto de perfil no se conserva al cerrar la app (mejora futura).