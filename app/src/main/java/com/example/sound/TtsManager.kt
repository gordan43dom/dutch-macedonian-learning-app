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

    fun playAudio(audioPath: String, textFallback: String, languageCode: String) {
        val path = audioPath.trim()
        if (path.isEmpty() || (!path.startsWith("http") && !path.startsWith("file://") && !path.startsWith("content://"))) {
            // No custom web path or local media URI - fall back to Text to Speech speaking
            speak(textFallback, languageCode)
            return
        }

        try {
            val mediaPlayer = android.media.MediaPlayer()
            mediaPlayer.setDataSource(path)
            mediaPlayer.setOnPreparedListener { mp ->
                mp.start()
            }
            mediaPlayer.setOnCompletionListener { mp ->
                mp.release()
            }
            mediaPlayer.setOnErrorListener { mp, _, _ ->
                mp.release()
                speak(textFallback, languageCode)
                true
            }
            mediaPlayer.prepareAsync()
        } catch (e: Exception) {
            Log.e("TtsManager", "Failed to play custom source: $path. Fallback to TTS.", e)
            speak(textFallback, languageCode)
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
