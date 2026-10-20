package com.example.doslenguasapp.data

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface PhraseProgressDao {

    @Query("SELECT * FROM phrase_progress")
    fun observeAll(): Flow<List<PhraseProgressEntity>>

    @Query("SELECT * FROM phrase_progress WHERE phraseId = :phraseId")
    fun observeById(phraseId: Int): Flow<PhraseProgressEntity?>

    @Query("SELECT * FROM phrase_progress WHERE phraseId = :phraseId")
    suspend fun getById(phraseId: Int): PhraseProgressEntity?

    // Upsert = "inserta, y si ya existe, actualiza".
    @Upsert
    suspend fun upsert(progress: PhraseProgressEntity)
}