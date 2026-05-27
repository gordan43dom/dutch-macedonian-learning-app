package com.example.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "words",
    foreignKeys = [
        ForeignKey(
            entity = CategoryEntity::class,
            parentColumns = ["id"],
            childColumns = ["categoryId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class WordEntity(
    @PrimaryKey val id: String, // e.g. "mama", "fork", "icecream"
    val categoryId: String,
    val imagePath: String, // Matches the illustration itemId, e.g. "mama"
    val emoji: String, // Fallback emoji for new words, e.g. "👩"
    val themeColorHex: Long
)
