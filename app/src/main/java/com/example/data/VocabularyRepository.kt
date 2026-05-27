package com.example.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map

class VocabularyRepository(private val vocabularyDao: VocabularyDao) {

    val allLanguages: Flow<List<LanguageEntity>> = vocabularyDao.getAllLanguagesFlow()

    val allCategories: Flow<List<CategoryEntity>> = vocabularyDao.getAllCategoriesFlow().map { list ->
        list.map { it.category }
    }

    val allVocabularyItems: Flow<List<VocabularyItem>> = combine(
        vocabularyDao.getAllWordsFlow(),
        vocabularyDao.getAllCategoriesFlow()
    ) { words, categories ->
        val categoriesMap = categories.associateBy { it.category.id }
        words.map { wordWithTrans ->
            val word = wordWithTrans.word
            val transList = wordWithTrans.translations
            
            val dutchTrans = transList.firstOrNull { it.languageCode == "NL" }?.translationValue ?: word.id.replaceFirstChar { it.uppercase() }
            val macedonianLatinTrans = transList.firstOrNull { it.languageCode == "MK" }?.translationValue ?: ""
            val macedonianCyrillicTrans = transList.firstOrNull { it.languageCode == "MK_CYR" }?.translationValue ?: ""
            val pronunciation = transList.firstOrNull { it.languageCode == "MK_CYR" }?.pronunciationText 
                ?: transList.firstOrNull { it.languageCode == "MK" }?.pronunciationText ?: ""
            val audioPath = transList.firstOrNull { it.languageCode == "MK_CYR" }?.audioPath 
                ?: transList.firstOrNull { it.audioPath.isNotBlank() }?.audioPath ?: ""
            
            val categoryName = categoriesMap[word.categoryId]?.category?.defaultName ?: word.categoryId
            
            VocabularyItem(
                id = word.id,
                dutch = dutchTrans,
                macedonian = macedonianLatinTrans,
                macedonianCyrillic = macedonianCyrillicTrans,
                pronunciation = pronunciation,
                category = categoryName,
                emoji = word.emoji,
                themeColorHex = word.themeColorHex,
                imagePath = word.imagePath,
                audioPath = audioPath
            )
        }
    }

    suspend fun insertCategory(category: CategoryEntity, localizedNames: List<CategoryTranslationEntity>) {
        vocabularyDao.insertCategory(category)
        vocabularyDao.insertCategoryTranslations(localizedNames)
    }

    suspend fun insertWord(word: WordEntity, translations: List<TranslationEntity>) {
        vocabularyDao.insertWord(word)
        vocabularyDao.insertTranslations(translations)
    }

    suspend fun addLanguage(language: LanguageEntity) {
        vocabularyDao.insertLanguage(language)
    }

    suspend fun deleteWord(wordId: String) {
        vocabularyDao.deleteWordById(wordId)
    }

    suspend fun deleteCategory(categoryId: String) {
        vocabularyDao.deleteCategoryById(categoryId)
    }

    suspend fun seedDatabaseIfEmpty() {
        val count = vocabularyDao.getAllLanguagesList().size
        if (count == 0) {
            // Seed languages
            val languages = listOf(
                LanguageEntity("NL", "Nederlands"),
                LanguageEntity("MK", "Makedonski (Latin)"),
                LanguageEntity("MK_CYR", "Македонски (Кирилица)")
            )
            vocabularyDao.insertLanguages(languages)

            // Seed categories
            val categoriesFromVocabulary = listOf(
                CategoryEntity("Gezin", "👨‍👩‍👦", "Gezin", "Mama, Papa, Oma, Baby...", 0xFFFFD180),
                CategoryEntity("Keuken", "🍳", "Keuken", "Vork, Lepel, Glas, Mes...", 0xFFFFF59D),
                CategoryEntity("Vakantie", "🏖️", "Vakantie", "Strand, Zee, IJsje, Bal...", 0xFF80DEEA)
            )

            for (cat in categoriesFromVocabulary) {
                vocabularyDao.insertCategory(cat)
                vocabularyDao.insertCategoryTranslation(
                    CategoryTranslationEntity(categoryId = cat.id, languageCode = "NL", name = cat.defaultName)
                )
            }

            // Seed words
            for (item in Vocabulary.items) {
                val word = WordEntity(
                    id = item.id,
                    categoryId = item.category,
                    imagePath = item.id,
                    emoji = item.emoji,
                    themeColorHex = item.themeColorHex
                )
                vocabularyDao.insertWord(word)

                val translations = listOf(
                    TranslationEntity(wordId = item.id, languageCode = "NL", translationValue = item.dutch),
                    TranslationEntity(wordId = item.id, languageCode = "MK", translationValue = item.macedonian),
                    TranslationEntity(
                        wordId = item.id,
                        languageCode = "MK_CYR",
                        translationValue = item.macedonianCyrillic,
                        pronunciationText = item.pronunciation,
                        audioPath = "sound/${item.id}.mp3"
                    )
                )
                vocabularyDao.insertTranslations(translations)
            }
        }
    }
}
