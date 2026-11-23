package com.example.doslenguasapp.data.remote

// Modelo de lo que devuelve la API (un "todo" de JSONPlaceholder).
// Los nombres de los campos coinciden con el JSON, así que no hace falta Gson @SerializedName.
data class TodoDto(
    val userId: Int,
    val id: Int,
    val title: String,
    val completed: Boolean
)
