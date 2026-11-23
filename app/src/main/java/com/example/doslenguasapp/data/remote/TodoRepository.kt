package com.example.doslenguasapp.data.remote

/**
 * Capa intermedia entre el ViewModel y la API.
 * Devuelve Result para que la pantalla pueda distinguir éxito y error.
 */
class TodoRepository(private val api: TodoApi = ApiClient.todoApi) {

    suspend fun getTodo(): Result<TodoDto> = try {
        Result.success(api.getTodo())
    } catch (e: Exception) {
        Result.failure(e)
    }
}
