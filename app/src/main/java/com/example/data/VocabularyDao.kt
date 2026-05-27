package com.example.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

data class CategoryWithTranslations(
    @Embedded val category: CategoryEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "categoryId"
    )
    val translations: List<CategoryTranslationEntity>
)

data class WordWithTranslations(
    @Embedded val word: WordEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "wordId"
    )
    val translations: List<TranslationEntity>
)

@Dao
interface VocabularyDao {
    @Transaction
    @Query("SELECT * FROM categories")
    fun getAllCategoriesFlow(): Flow<List<CategoryWithTranslations>>

    @Transaction
    @Query("SELECT * FROM words")
    fun getAllWordsFlow(): Flow<List<WordWithTranslations>>

    @Transaction
    @Query("SELECT * FROM categories")
    suspend fun getAllCategoriesList(): List<CategoryWithTranslations>

    @Transaction
    @Query("SELECT * FROM words")
    suspend fun getAllWordsList(): List<WordWithTranslations>

    @Query("SELECT * FROM languages")
    fun getAllLanguagesFlow(): Flow<List<LanguageEntity>>

    @Query("SELECT * FROM languages")
    suspend fun getAllLanguagesList(): List<LanguageEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategory(category: CategoryEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategoryTranslation(translation: CategoryTranslationEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategoryTranslations(translations: List<CategoryTranslationEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWord(word: WordEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTranslation(translation: TranslationEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTranslations(translations: List<TranslationEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLanguage(language: LanguageEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLanguages(languages: List<LanguageEntity>)

    @Query("DELETE FROM categories WHERE id = :id")
    suspend fun deleteCategoryById(id: String)

    @Query("DELETE FROM words WHERE id = :id")
    suspend fun deleteWordById(id: String)
}
