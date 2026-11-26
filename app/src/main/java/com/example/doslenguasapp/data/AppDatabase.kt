package com.example.doslenguasapp.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(
    entities = [UserEntity::class, PhraseProgressEntity::class],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun phraseProgressDao(): PhraseProgressDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        // Las cuentas ya existentes siguen funcionando: la nueva columna
        // se crea con el valor por defecto (español → turco).
        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    "ALTER TABLE users ADD COLUMN direction TEXT NOT NULL DEFAULT 'ES_TO_TR'"
                )
            }
        }

        // Una sola base de datos para toda la app (patrón "singleton").
        fun getInstance(context: Context): AppDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "doslenguas.db"
                )
                    .addMigrations(MIGRATION_1_2)
                    .build().also { INSTANCE = it }
            }
    }
}