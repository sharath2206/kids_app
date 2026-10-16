package com.example.myapplication.audio

import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioTrack
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.math.PI
import kotlin.math.exp
import kotlin.math.sin

/**
 * Procedural Soft & Melodious Sound Engine for Kids Learning.
 *
 * Generates soothing, bell-like chimes, music-box tones, bird chirps,
 * bubble pops, and counting scales with zero external dependencies.
 */
object MelodySoundEngine {

    private const val SAMPLE_RATE = 44100
    private val scope = CoroutineScope(Dispatchers.Default)

    // Musical note frequencies (Hz) for clear, soft pentatonic/diatonic bells
    val NOTE_C4 = 261.63
    val NOTE_D4 = 293.66
    val NOTE_E4 = 329.63
    val NOTE_F4 = 349.23
    val NOTE_G4 = 392.00
    val NOTE_A4 = 440.00
    val NOTE_B4 = 493.88
    val NOTE_C5 = 523.25
    val NOTE_D5 = 587.33
    val NOTE_E5 = 659.25
    val NOTE_F5 = 698.46
    val NOTE_G5 = 783.99
    val NOTE_A5 = 880.00
    val NOTE_B5 = 987.77
    val NOTE_C6 = 1046.50
    val NOTE_D6 = 1174.66
    val NOTE_E6 = 1318.51

    // Background lullaby playback job
    private var lullabyJob: Job? = null
    var isLullabyPlaying = false
        private set

    /**
     * Plays a soft, melodious celesta/bell chime for a specific letter (A-Z).
     * Creates a unique harmonious tone for each letter so kids associate sound with alphabet!
     */
    fun playAlphabetChime(letter: Char) {
        val noteFreq = getNoteForLetter(letter)
        playMusicBoxChime(noteFreq, durationMs = 550, volume = 0.70f)
    }

    /**
     * Maps each letter A-Z to a soothing, harmonic musical pitch.
     */
    private fun getNoteForLetter(letter: Char): Double {
        val uppercase = letter.uppercaseChar()
        val index = (uppercase - 'A').coerceIn(0, 25)
        val melodyScale = doubleArrayOf(
            NOTE_C5, NOTE_D5, NOTE_E5, NOTE_F5, NOTE_G5, NOTE_A5, NOTE_B5,
            NOTE_C6, NOTE_D6, NOTE_E6, NOTE_G5, NOTE_A5, NOTE_C5, NOTE_D5,
            NOTE_E5, NOTE_G5, NOTE_A5, NOTE_C6, NOTE_D5, NOTE_E5, NOTE_G5,
            NOTE_A5, NOTE_C5, NOTE_E5, NOTE_G5, NOTE_C6
        )
        return melodyScale[index % melodyScale.size]
    }

    /**
     * Plays a gentle, sweet music box chime tone.
     * Uses soft attack envelope (no clicks) and natural exponential decay.
     */
    fun playMusicBoxChime(freqHz: Double, durationMs: Int = 450, volume: Float = 0.65f) {
        scope.launch {
            val numSamples = (SAMPLE_RATE * (durationMs / 1000.0)).toInt()
            val buffer = ShortArray(numSamples)
            val attackSamples = (SAMPLE_RATE * 0.018).toInt() // 18ms smooth attack

            for (i in 0 until numSamples) {
                val t = i.toDouble() / SAMPLE_RATE
                // Fundamental sine wave + subtle warm 2nd & 3rd harmonics for music box feel
                val fundamental = sin(2.0 * PI * freqHz * t)
                val secondHarmonic = 0.22 * sin(2.0 * PI * (freqHz * 2.0) * t)
                val thirdHarmonic = 0.08 * sin(2.0 * PI * (freqHz * 3.0) * t)
                var sample = (fundamental + secondHarmonic + thirdHarmonic) / 1.30

                // Envelope: Smooth attack to avoid clicking, exponential decay for bell resonance
                val envelope = if (i < attackSamples) {
                    (i.toDouble() / attackSamples)
                } else {
                    exp(-4.2 * (t - 0.018))
                }

                sample *= envelope * volume
                buffer[i] = (sample.coerceIn(-1.0, 1.0) * Short.MAX_VALUE).toInt().toShort()
            }
            writeAudio(buffer)
        }
    }

    /**
     * Plays a celebratory victory chime chord (C - E - G - C arpeggio) when a kid completes a drawing or activity.
     */
    fun playCelebrationChime() {
        scope.launch {
            val arpeggio = listOf(NOTE_C5, NOTE_E5, NOTE_G5, NOTE_C6, NOTE_E6)
            for (note in arpeggio) {
                playMusicBoxChime(note, durationMs = 380, volume = 0.60f)
                delay(95)
            }
        }
    }

    /**
     * Plays a soft, tactile bubble pop / water drop sound when touching or drawing.
     */
    fun playBubblePop(volume: Float = 0.35f) {
        scope.launch {
            val durationMs = 70
            val numSamples = (SAMPLE_RATE * (durationMs / 1000.0)).toInt()
            val buffer = ShortArray(numSamples)

            for (i in 0 until numSamples) {
                val t = i.toDouble() / SAMPLE_RATE
                // Frequency sweeps down rapidly (water drop pitch dip)
                val currentFreq = 720.0 - (420.0 * (t / 0.07))
                var sample = sin(2.0 * PI * currentFreq * t)
                val envelope = exp(-35.0 * t) // fast decay
                sample *= envelope * volume
                buffer[i] = (sample.coerceIn(-1.0, 1.0) * Short.MAX_VALUE).toInt().toShort()
            }
            writeAudio(buffer)
        }
    }

    /**
     * Plays a cheerful, melodious sweet bird chirp.
     */
    fun playSweetBirdChirp(volume: Float = 0.55f) {
        scope.launch {
            // First chirp note
            playFrequencyModulatedChirp(startFreq = 2200.0, endFreq = 3200.0, durationMs = 120, volume = volume)
            delay(110)
            // Second chirp note
            playFrequencyModulatedChirp(startFreq = 2600.0, endFreq = 3600.0, durationMs = 140, volume = volume)
        }
    }

    private fun playFrequencyModulatedChirp(startFreq: Double, endFreq: Double, durationMs: Int, volume: Float) {
        val numSamples = (SAMPLE_RATE * (durationMs / 1000.0)).toInt()
        val buffer = ShortArray(numSamples)
        var phase = 0.0

        for (i in 0 until numSamples) {
            val t = i.toDouble() / SAMPLE_RATE
            val progress = t / (durationMs / 1000.0)
            val currentFreq = startFreq + (endFreq - startFreq) * progress
            phase += 2.0 * PI * currentFreq / SAMPLE_RATE
            var sample = sin(phase)
            // Soft envelope
            val envelope = sin(PI * progress)
            sample *= envelope * volume
            buffer[i] = (sample.coerceIn(-1.0, 1.0) * Short.MAX_VALUE).toInt().toShort()
        }
        writeAudio(buffer)
    }

    /**
     * Plays a warm marimba / woodblock boing tone for animal cards.
     */
    fun playMarimbaBoing(freqHz: Double = NOTE_G4, volume: Float = 0.60f) {
        scope.launch {
            val durationMs = 300
            val numSamples = (SAMPLE_RATE * (durationMs / 1000.0)).toInt()
            val buffer = ShortArray(numSamples)

            for (i in 0 until numSamples) {
                val t = i.toDouble() / SAMPLE_RATE
                val fundamental = sin(2.0 * PI * freqHz * t)
                val overtone = 0.35 * sin(2.0 * PI * (freqHz * 3.8) * t) // Woodblock harmonic
                var sample = (fundamental + overtone) / 1.35
                val envelope = exp(-12.0 * t)
                sample *= envelope * volume
                buffer[i] = (sample.coerceIn(-1.0, 1.0) * Short.MAX_VALUE).toInt().toShort()
            }
            writeAudio(buffer)
        }
    }

    /**
     * Plays a musical scale note for counting (1 to 20).
     * Kids hear each number step higher like a joyful staircase!
     */
    fun playNumberChime(number: Int) {
        val scale = doubleArrayOf(
            NOTE_C4, NOTE_D4, NOTE_E4, NOTE_F4, NOTE_G4, NOTE_A4, NOTE_B4,
            NOTE_C5, NOTE_D5, NOTE_E5, NOTE_F5, NOTE_G5, NOTE_A5, NOTE_B5,
            NOTE_C6, NOTE_D6, NOTE_E6, NOTE_G5, NOTE_A5, NOTE_C6
        )
        val note = scale[(number - 1).coerceIn(0, scale.size - 1)]
        playMusicBoxChime(note, durationMs = 450, volume = 0.70f)
    }

    /**
     * Toggle soft calming music box lullaby in background.
     */
    fun toggleLullaby(onStateChanged: (Boolean) -> Unit) {
        if (isLullabyPlaying) {
            stopLullaby()
            onStateChanged(false)
        } else {
            startLullaby()
            onStateChanged(true)
        }
    }

    fun startLullaby() {
        if (isLullabyPlaying) return
        isLullabyPlaying = true
        lullabyJob = scope.launch {
            // Melodic phrase of "Twinkle Twinkle Little Star" notes
            val lullabyNotes = listOf(
                NOTE_C5, NOTE_C5, NOTE_G5, NOTE_G5, NOTE_A5, NOTE_A5, NOTE_G5,
                NOTE_F5, NOTE_F5, NOTE_E5, NOTE_E5, NOTE_D5, NOTE_D5, NOTE_C5,
                NOTE_G5, NOTE_G5, NOTE_F5, NOTE_F5, NOTE_E5, NOTE_E5, NOTE_D5,
                NOTE_G5, NOTE_G5, NOTE_F5, NOTE_F5, NOTE_E5, NOTE_E5, NOTE_D5,
                NOTE_C5, NOTE_C5, NOTE_G5, NOTE_G5, NOTE_A5, NOTE_A5, NOTE_G5,
                NOTE_F5, NOTE_F5, NOTE_E5, NOTE_E5, NOTE_D5, NOTE_D5, NOTE_C5
            )

            val durations = listOf(
                500, 500, 500, 500, 500, 500, 950,
                500, 500, 500, 500, 500, 500, 950,
                500, 500, 500, 500, 500, 500, 950,
                500, 500, 500, 500, 500, 500, 950,
                500, 500, 500, 500, 500, 500, 950,
                500, 500, 500, 500, 500, 500, 950
            )

            while (isActive && isLullabyPlaying) {
                for (i in lullabyNotes.indices) {
                    if (!isLullabyPlaying || !isActive) break
                    val note = lullabyNotes[i]
                    val duration = durations[i]
                    playMusicBoxChime(note, durationMs = duration, volume = 0.18f) // soft, gentle lullaby volume
                    delay(duration.toLong() + 40L)
                }
                delay(1200L) // pause before repeating
            }
        }
    }

    fun stopLullaby() {
        isLullabyPlaying = false
        lullabyJob?.cancel()
        lullabyJob = null
    }

    /**
     * Direct playback of PCM buffer via AudioTrack.
     */
    private fun writeAudio(pcmData: ShortArray) {
        try {
            val audioTrack = AudioTrack.Builder()
                .setAudioAttributes(
                    AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_ASSISTANCE_SONIFICATION)
                        .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                        .build()
                )
                .setAudioFormat(
                    AudioFormat.Builder()
                        .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                        .setSampleRate(SAMPLE_RATE)
                        .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
                        .build()
                )
                .setBufferSizeInBytes(pcmData.size * 2)
                .setTransferMode(AudioTrack.MODE_STATIC)
                .build()

            audioTrack.write(pcmData, 0, pcmData.size)
            audioTrack.play()

            // Automatically clean up AudioTrack after playback finishes
            val playbackTimeMs = ((pcmData.size.toDouble() / SAMPLE_RATE) * 1000).toLong() + 100
            scope.launch {
                delay(playbackTimeMs)
                try {
                    audioTrack.stop()
                    audioTrack.release()
                } catch (_: Exception) {}
            }
        } catch (_: Exception) {
            // Handle gracefully if audio device is unavailable
        }
    }
}
