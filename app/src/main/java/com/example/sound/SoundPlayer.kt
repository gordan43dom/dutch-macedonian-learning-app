package com.example.sound

import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioTrack
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.math.sin

object SoundPlayer {
    suspend fun playSuccessChime() = playToneSequence(listOf(523.25, 659.25, 783.99)) // C5, E5, G5 (rising cheerful major chord)

    suspend fun playErrorBuzzer() = playToneSequence(listOf(220.0, 196.0)) // A3, G3 (soft corrective low buzz)

    private suspend fun playToneSequence(frequencies: List<Double>) {
        withContext(Dispatchers.Default) {
            try {
                val sampleRate = 8000
                val durationMs = 120
                val numSamples = (durationMs / 1000.0 * sampleRate).toInt()
                val totalBytes = numSamples * 2 * frequencies.size
                val buffer = ByteArray(totalBytes)

                var byteIdx = 0
                for (freq in frequencies) {
                    for (i in 0 until numSamples) {
                        val angle = 2.0 * Math.PI * i / (sampleRate / freq)
                        
                        // Apply fade in & fade out envelope to eliminate hard transients (clicks)
                        val envelope = if (i < numSamples / 10) {
                            i / (numSamples / 10.0)
                        } else if (i > numSamples * 9 / 10) {
                            (numSamples - i) / (numSamples / 10.0)
                        } else {
                            1.0
                        }
                        
                        val sampleValue = (sin(angle) * 32767.0 * 0.3 * envelope).toInt()
                        buffer[byteIdx++] = (sampleValue and 0x00ff).toByte()
                        buffer[byteIdx++] = ((sampleValue and 0xff00) shr 8).toByte()
                    }
                }

                val audioTrack = AudioTrack.Builder()
                    .setAudioAttributes(
                        AudioAttributes.Builder()
                            .setUsage(AudioAttributes.USAGE_GAME)
                            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                            .build()
                    )
                    .setAudioFormat(
                        AudioFormat.Builder()
                            .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                            .setSampleRate(sampleRate)
                            .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
                            .build()
                    )
                    .setBufferSizeInBytes(buffer.size)
                    .setTransferMode(AudioTrack.MODE_STATIC)
                    .build()

                audioTrack.write(buffer, 0, buffer.size)
                audioTrack.play()
                
                // Allow tone sequence to finish playing
                kotlinx.coroutines.delay((frequencies.size * durationMs).toLong() + 30)
                audioTrack.stop()
                audioTrack.release()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
