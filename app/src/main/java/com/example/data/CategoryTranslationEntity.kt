package com.example.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "category_translations",
    foreignKeys = [
        ForeignKey(
            entity = CategoryEntity::class,
            parentColumns = ["id"],
            childColumns = ["categoryId"],
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
        Index(value = ["categoryId", "languageCode"], unique = true)
    ]
)
data class CategoryTranslationEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val categoryId: String,
    val languageCode: String,
    val name: String // e.g. "Gezin", "Keuken", "Vakantie"
)
