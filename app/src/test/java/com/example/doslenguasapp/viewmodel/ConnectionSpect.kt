package com.example.doslenguasapp.viewmodel

import com.example.doslenguasapp.data.remote.TodoApi
import com.example.doslenguasapp.data.remote.TodoDto
import com.example.doslenguasapp.data.remote.TodoRepository
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import java.io.IOException

/**
 * Test de funcionamiento de la opción "Conexión" del menú.
 * Kotest nombra y verifica cada caso; MockK simula la API para no depender de internet.
 */
@OptIn(ExperimentalCoroutinesApi::class)
class ConnectionSpect : StringSpec({

    beforeSpec { Dispatchers.setMain(UnconfinedTestDispatcher()) }
    afterSpec { Dispatchers.resetMain() }

    val api = mockk<TodoApi>()
    val repository = TodoRepository(api)

    "Consultar API trae el dato y se comprueba la llamada" {
        val viewModel = ConnectionViewModel(repository)
        coEvery { api.getTodo() } returns TodoDto(
            userId = 1,
            id = 1,
            title = "delectus aut autem",
            completed = false
        )

        runTest {
            viewModel.loadTodo()
            advanceUntilIdle()
        }

        coVerify { api.getTodo() }
        viewModel.state.value.todo?.title shouldBe "delectus aut autem"
        viewModel.state.value.error shouldBe null
    }

    "si la API falla muestra error" {
        val viewModel = ConnectionViewModel(repository)
        coEvery { api.getTodo() } throws IOException("sin internet")

        runTest {
            viewModel.loadTodo()
            advanceUntilIdle()
        }

        coVerify { api.getTodo() }
        viewModel.state.value.error shouldBe "sin internet"
        viewModel.state.value.todo shouldBe null
    }
})
