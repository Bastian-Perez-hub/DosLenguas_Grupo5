package com.example.doslenguasapp.viewmodel

import androidx.lifecycle.SavedStateHandle
import com.example.doslenguasapp.data.PhraseProgressEntity
import com.example.doslenguasapp.data.PhraseProgressRepository
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain

/**
 * Test de funcionamiento del detalle de frase.
 * Kotest arma el escenario (given / when / then) y MockK reemplaza al repositorio
 * para comprobar que los botones envían el recado sin tocar la base de datos real.
 */
@OptIn(ExperimentalCoroutinesApi::class)
class DetailSpect : BehaviorSpec({

    beforeSpec { Dispatchers.setMain(UnconfinedTestDispatcher()) }
    afterSpec { Dispatchers.resetMain() }

    val repository = mockk<PhraseProgressRepository>()
    every { repository.observe(1) } returns flowOf<PhraseProgressEntity?>(
        PhraseProgressEntity(phraseId = 1)
    )
    coEvery { repository.markLearned(1) } just Runs
    coEvery { repository.toggleFavorite(1) } just Runs

    given("la pantalla de detalle de la frase 1") {
        val viewModel = DetailViewModel(
            savedStateHandle = SavedStateHandle(mapOf("phraseId" to 1)),
            progressRepository = repository
        )

        `when`("toco ¡Ya la aprendí!") {
            runTest {
                viewModel.onLearnedClick()
                advanceUntilIdle()
            }
            then("el recado llega al repositorio") {
                coVerify { repository.markLearned(1) }
                viewModel.state.value.phrase?.id shouldBe 1
            }
        }

        `when`("toco el corazón") {
            runTest {
                viewModel.onFavoriteClick()
                advanceUntilIdle()
            }
            then("el recado llega al repositorio") {
                coVerify { repository.toggleFavorite(1) }
            }
        }
    }
})
