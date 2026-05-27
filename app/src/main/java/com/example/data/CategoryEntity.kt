package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "categories")
data class CategoryEntity(
    @PrimaryKey val id: String, // e.g. "Gezin", "Keuken", "Vakantie"
    val emoji: String, // e.g. "👨‍👩‍👦"
    val defaultName: String, // e.g. "Gezin" for fallback
    val description: String, // e.g. "Mama, Papa, Oma, Baby..."
    val themeColorHex: Long
)
