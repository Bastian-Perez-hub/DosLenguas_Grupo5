package com.example.doslenguasapp.data

import kotlinx.coroutines.flow.Flow

class PhraseProgressRepository(private val dao: PhraseProgressDao) {

    fun observeAll(): Flow<List<PhraseProgressEntity>> = dao.observeAll()

    fun observe(phraseId: Int): Flow<PhraseProgressEntity?> = dao.observeById(phraseId)

    suspend fun markLearned(phraseId: Int) {
        val actual = dao.getById(phraseId) ?: PhraseProgressEntity(phraseId)
        dao.upsert(actual.copy(learned = true))
    }

    suspend fun toggleFavorite(phraseId: Int) {
        val actual = dao.getById(phraseId) ?: PhraseProgressEntity(phraseId)
        dao.upsert(actual.copy(favorite = !actual.favorite))
    }
}