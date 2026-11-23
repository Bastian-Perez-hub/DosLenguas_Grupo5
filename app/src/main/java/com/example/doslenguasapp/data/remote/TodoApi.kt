package com.example.doslenguasapp.data.remote

import retrofit2.http.GET

/**
 * Declaración del endpoint. Retrofit construye la implementación de esta interfaz.
 * La URL completa sería: https://jsonplaceholder.typicode.com/todos/1
 */
interface TodoApi {

    @GET("todos/1")
    suspend fun getTodo(): TodoDto
}
