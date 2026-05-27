package com.example.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [
        ProgressEntity::class,
        CategoryEntity::class,
        WordEntity::class,
        LanguageEntity::class,
        TranslationEntity::class,
        CategoryTranslationEntity::class
    ],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun progressDao(): ProgressDao
    abstract fun vocabularyDao(): VocabularyDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val builder = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "matching_game_database"
                )

                // Dynamically check if offline-packaged database exists in assets
                val assetsList = try {
                    context.assets.list("database")
                } catch (e: Exception) {
                    null
                }
                if (assetsList?.contains("vocabulary.db") == true) {
                    builder.createFromAsset("database/vocabulary.db")
                }

                val instance = builder
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
