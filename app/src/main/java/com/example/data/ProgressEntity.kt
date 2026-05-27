package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "progress_record")
data class ProgressEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val timestamp: Long = System.currentTimeMillis(),
    val dutchWord: String,
    val macedonianWord: String,
    val isCorrect: Boolean,
    val starsEarned: Int
)
