package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "languages")
data class LanguageEntity(
    @PrimaryKey val code: String, // e.g. "NL", "MK", "MK_CYR"
    val name: String // e.g. "Nederlands", "Macedonian (Latin)", "MacedonianCyrillic"
)
