package com.example.doslenguasapp.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [UserEntity::class, PhraseProgressEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun phraseProgressDao(): PhraseProgressDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        // Una sola base de datos para toda la app (patrón "singleton").
        fun getInstance(context: Context): AppDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "doslenguas.db"
                ).build().also { INSTANCE = it }
            }
    }
}