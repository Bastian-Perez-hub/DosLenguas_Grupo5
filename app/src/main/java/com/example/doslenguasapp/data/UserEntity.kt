package com.example.doslenguasapp.data

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.doslenguasapp.model.LearningDirection

// Tabla "users": una fila por cuenta creada en el Registro.
@Entity(
    tableName = "users",
    indices = [Index(value = ["email"], unique = true)]
)
data class UserEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val nombre: String,
    val email: String,
    val password: String,
    // Guardamos el nombre del enum para no necesitar un TypeConverter de Room.
    val direction: String = LearningDirection.ES_TO_TR.name
)