package com.example.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "translations",
    foreignKeys = [
        ForeignKey(
            entity = WordEntity::class,
            parentColumns = ["id"],
            childColumns = ["wordId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = LanguageEntity::class,
            parentColumns = ["code"],
            childColumns = ["languageCode"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["wordId", "languageCode"], unique = true)
    ]
)
data class TranslationEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val wordId: String,
    val languageCode: String,
    val translationValue: String, // e.g. "Mama", "Majka", "Мајка"
    val pronunciationText: String = "", // e.g. "MAYE-kah"
    val audioPath: String = "" // Optional asset path or URL for pronunciation mp3
)
