package com.example.sound

import android.content.Context
import android.speech.tts.TextToSpeech
import android.util.Log
import java.util.Locale

object TtsManager {
    private var tts: TextToSpeech? = null
    private var isInitialized = false

    fun init(context: Context) {
        if (tts != null) return
        tts = TextToSpeech(context.applicationContext) { status ->
            if (status == TextToSpeech.SUCCESS) {
                isInitialized = true
                Log.d("TtsManager", "TextToSpeech Initialized successfully!")
            } else {
                Log.e("TtsManager", "TextToSpeech Initialization failed!")
            }
        }
    }

    fun speak(text: String, languageCode: String) {
        val engine = tts
        if (engine == null || !isInitialized) {
            Log.w("TtsManager", "TTS engine not initialized yet.")
            return
        }

        val locale = when (languageCode.uppercase()) {
            "NL" -> Locale("nl", "NL")
            "MK", "MK_CYR" -> Locale("mk", "MK")
            "EN" -> Locale.ENGLISH
            else -> Locale.getDefault()
        }

        try {
            val result = engine.setLanguage(locale)
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.w("TtsManager", "Language $locale is not supported or missing data. Falling back to default.")
                engine.language = Locale.getDefault()
            }
            engine.speak(text, TextToSpeech.QUEUE_FLUSH, null, "VocabularyTTS")
        } catch (e: Exception) {
            Log.e("TtsManager", "Failed to speak: ${e.message}", e)
        }
    }

    fun shutdown() {
        try {
            tts?.stop()
            tts?.shutdown()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        tts = null
        isInitialized = false
    }
}
