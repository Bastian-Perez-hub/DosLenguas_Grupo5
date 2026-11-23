package com.example.doslenguasapp.data.remote

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

// Cliente HTTP único para toda la app ( patrón singleton, igual que AppDatabase ).
object ApiClient {

    // Importante: la base URL debe terminar con "/".
    private const val BASE_URL = "https://jsonplaceholder.typicode.com/"

    val todoApi: TodoApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(TodoApi::class.java)
    }
}
