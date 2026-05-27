package com.example.viewmodel

import android.app.Application
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.AppDatabase
import com.example.data.ProgressEntity
import com.example.data.ProgressRepository
import com.example.data.Vocabulary
import com.example.data.VocabularyItem
import com.example.sound.SoundPlayer
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random
import com.example.data.WordEntity
import com.example.data.CategoryEntity
import com.example.data.TranslationEntity
import com.example.data.CategoryTranslationEntity

sealed class Screen {
    object CategorySelection : Screen()
    data class TopicDetail(val category: String) : Screen()
}

data class Particle(
    val id: String,
    val startX: Float,
    val startY: Float,
    val currentX: Float,
    val currentY: Float,
    val deltaX: Float,
    val deltaY: Float,
    val alpha: Float,
    val scale: Float,
    val speed: Float
)

class GameViewModel(application: Application) : AndroidViewModel(application) {
    private val database = AppDatabase.getDatabase(application)
    private val repository = ProgressRepository(database.progressDao())
    private val vocabularyRepository = com.example.data.VocabularyRepository(database.vocabularyDao())

    val allProgress: StateFlow<List<ProgressEntity>> = repository.allProgress
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val allVocabularyItemsFromDb: StateFlow<List<VocabularyItem>> = vocabularyRepository.allVocabularyItems
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    private val _currentRoundItems = MutableStateFlow<List<VocabularyItem>>(emptyList())
    val currentRoundItems = _currentRoundItems.asStateFlow()

    private val _shuffledDutch = MutableStateFlow<List<VocabularyItem>>(emptyList())
    val shuffledDutch = _shuffledDutch.asStateFlow()

    private val _shuffledMacedonian = MutableStateFlow<List<VocabularyItem>>(emptyList())
    val shuffledMacedonian = _shuffledMacedonian.asStateFlow()

    private val _matchedIds = MutableStateFlow<Set<String>>(emptySet())
    val matchedIds = _matchedIds.asStateFlow()

    private val _selectedDutchId = MutableStateFlow<String?>(null)
    val selectedDutchId = _selectedDutchId.asStateFlow()

    private val _selectedMacedonianId = MutableStateFlow<String?>(null)
    val selectedMacedonianId = _selectedMacedonianId.asStateFlow()

    private val _totalStars = MutableStateFlow(0)
    val totalStars = _totalStars.asStateFlow()

    private val _selectedCategory = MutableStateFlow<String>("Alle")
    val selectedCategory = _selectedCategory.asStateFlow()

    private val _quizWordCount = MutableStateFlow(3)
    val quizWordCount = _quizWordCount.asStateFlow()

    private val _currentScreen = MutableStateFlow<Screen>(Screen.CategorySelection)
    val currentScreen = _currentScreen.asStateFlow()

    val categories: StateFlow<List<String>> = vocabularyRepository.allCategories
        .map { list -> listOf("Alle") + list.map { it.defaultName } }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = listOf("Alle", "Gezin", "Keuken", "Vakantie")
        )

    val particles = mutableStateListOf<Particle>()

    init {
        viewModelScope.launch {
            vocabularyRepository.seedDatabaseIfEmpty()
        }

        viewModelScope.launch {
            allVocabularyItemsFromDb.collect { items ->
                if (items.isNotEmpty() && _currentRoundItems.value.isEmpty()) {
                    generateNewRound()
                }
            }
        }

        viewModelScope.launch {
            repository.allProgress.collect { list ->
                // Dynamically summarize total stars earned in past successful play records (1 star per correct match)
                val stars = list.count { it.isCorrect }
                _totalStars.value = stars
            }
        }
    }

    fun setCategory(category: String) {
        _selectedCategory.value = category
        generateNewRound()
    }

    fun navigateToCategory(category: String) {
        _quizWordCount.value = 3 // Reset to default toddlers round scale
        _selectedCategory.value = category
        generateNewRound()
        _currentScreen.value = Screen.TopicDetail(category)
    }

    fun navigateBackToHome() {
        _currentScreen.value = Screen.CategorySelection
    }

    fun generateNewRound() {
        val cat = _selectedCategory.value
        val totalList = if (cat == "Alle") {
            allVocabularyItemsFromDb.value
        } else {
            allVocabularyItemsFromDb.value.filter { it.category.equals(cat, ignoreCase = true) }
        }
        val count = minOf(_quizWordCount.value, totalList.size)
        // Draw dynamically-sized unique vocabulary pairs randomly
        val selected = totalList.shuffled().take(count)
        _currentRoundItems.value = selected
        _shuffledDutch.value = selected.shuffled()
        _shuffledMacedonian.value = selected.shuffled()
        _matchedIds.value = emptySet()
        _selectedDutchId.value = null
        _selectedMacedonianId.value = null
    }

    fun startCustomQuiz(wordCount: Int, category: String) {
        _quizWordCount.value = wordCount
        _selectedCategory.value = category
        generateNewRound()
        _currentScreen.value = Screen.TopicDetail(category)
    }

    fun addNewWord(
        id: String,
        categoryId: String,
        dutch: String,
        macedonianLatin: String,
        macedonianCyrillic: String,
        pronunciation: String,
        emoji: String,
        imagePath: String = "",
        audioPath: String = ""
    ) {
        viewModelScope.launch {
            val word = WordEntity(
                id = id.lowercase().trim(),
                categoryId = categoryId,
                imagePath = imagePath.ifBlank { id.lowercase().trim() },
                emoji = emoji.ifEmpty { "✨" },
                themeColorHex = 0xFFECEFF1
            )
            val translations = listOf(
                TranslationEntity(wordId = word.id, languageCode = "NL", translationValue = dutch),
                TranslationEntity(wordId = word.id, languageCode = "MK", translationValue = macedonianLatin),
                TranslationEntity(
                    wordId = word.id,
                    languageCode = "MK_CYR",
                    translationValue = macedonianCyrillic,
                    pronunciationText = pronunciation,
                    audioPath = audioPath.ifBlank { "sound/${word.id}.mp3" }
                )
            )
            vocabularyRepository.insertWord(word, translations)
            generateNewRound() // Refresh
        }
    }

    fun addNewCategory(
        id: String,
        emoji: String,
        defaultName: String,
        description: String
    ) {
        viewModelScope.launch {
            val category = CategoryEntity(
                id = id.trim(),
                emoji = emoji.ifEmpty { "🌈" },
                defaultName = defaultName,
                description = description,
                themeColorHex = 0xFFCE93D8
            )
            val localization = CategoryTranslationEntity(
                categoryId = category.id,
                languageCode = "NL",
                name = defaultName
            )
            vocabularyRepository.insertCategory(category, listOf(localization))
        }
    }

    fun selectDutch(itemId: String) {
        if (_matchedIds.value.contains(itemId)) return
        _selectedDutchId.value = itemId
        checkMatch()
    }

    fun selectMacedonian(itemId: String) {
        if (_matchedIds.value.contains(itemId)) return
        _selectedMacedonianId.value = itemId
        checkMatch()
    }

    private fun checkMatch() {
        val dId = _selectedDutchId.value
        val mId = _selectedMacedonianId.value

        if (dId != null && mId != null) {
            val dItem = allVocabularyItemsFromDb.value.find { it.id == dId }
            val mItem = allVocabularyItemsFromDb.value.find { it.id == mId }

            if (dId == mId && dItem != null) {
                // Correct match!
                viewModelScope.launch {
                    SoundPlayer.playSuccessChime()
                    com.example.sound.TtsManager.playAudio(dItem.audioPath, dItem.macedonianCyrillic, "MK_CYR")
                }
                
                // Add to matched set
                val updatedMatch = _matchedIds.value + dId
                _matchedIds.value = updatedMatch

                // Create nice star burst particles from the middle to award the kid!
                triggerStarExplosion()

                // Save persistent log to database
                viewModelScope.launch {
                    repository.saveProgress(
                        ProgressEntity(
                            dutchWord = dItem.dutch,
                            macedonianWord = dItem.macedonian,
                            isCorrect = true,
                            starsEarned = 1
                        )
                    )
                }
            } else if (dItem != null && mItem != null) {
                // Wrong Match
                viewModelScope.launch {
                    SoundPlayer.playErrorBuzzer()
                }

                // Log mistaken trial to database for parent's tracking purposes
                viewModelScope.launch {
                    repository.saveProgress(
                        ProgressEntity(
                            dutchWord = dItem.dutch,
                            macedonianWord = mItem.macedonian,
                            isCorrect = false,
                            starsEarned = 0
                        )
                    )
                }
            }

            // Reset selection handles
            _selectedDutchId.value = null
            _selectedMacedonianId.value = null
        }
    }

    fun triggerStarExplosion() {
        val count = 20
        repeat(count) {
            val pId = java.util.UUID.randomUUID().toString()
            val angle = Random.nextFloat() * 2 * Math.PI
            val speed = Random.nextFloat() * 12f + 6f
            val dx = (cos(angle) * speed).toFloat()
            val dy = (sin(angle) * speed).toFloat()
            particles.add(
                Particle(
                    id = pId,
                    startX = 0f,
                    startY = 0f,
                    currentX = 0f,
                    currentY = 0f,
                    deltaX = dx,
                    deltaY = dy - 5f, // add float-up bias
                    alpha = 1.0f,
                    scale = Random.nextFloat() * 0.5f + 0.6f,
                    speed = speed
                )
            )
        }
    }

    fun updateParticles() {
        val iterator = particles.listIterator()
        while (iterator.hasNext()) {
            val part = iterator.next()
            val nextAlpha = part.alpha - 0.04f
            if (nextAlpha <= 0f) {
                iterator.remove()
            } else {
                iterator.set(
                    part.copy(
                        currentX = part.currentX + part.deltaX,
                        currentY = part.currentY + part.deltaY,
                        alpha = nextAlpha,
                        scale = part.scale * 0.97f
                    )
                )
            }
        }
    }

    fun clearHistory() {
        viewModelScope.launch {
            repository.clearProgress()
        }
    }
}
