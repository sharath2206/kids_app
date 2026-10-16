package com.example.myapplication

import android.media.AudioAttributes
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.speech.tts.Voice
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.myapplication.audio.MelodySoundEngine
import com.example.myapplication.model.AgeGroup
import com.example.myapplication.model.CategoryType
import com.example.myapplication.ui.AlphabetDrawingScreen
import com.example.myapplication.ui.AnimalsScreen
import com.example.myapplication.ui.BirdsScreen
import com.example.myapplication.ui.LandingScreen
import com.example.myapplication.ui.NumbersScreen
import com.example.myapplication.ui.WordsScreen
import com.example.myapplication.ui.theme.MyApplicationTheme
import java.util.Locale

enum class AppScreen {
    LANDING,
    ALPHABETS,
    WORDS,
    ANIMALS,
    BIRDS,
    NUMBERS
}

class MainActivity : ComponentActivity(), TextToSpeech.OnInitListener {

    private var textToSpeech: TextToSpeech? = null
    private var isTtsReady = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        textToSpeech = TextToSpeech(this, this)

        setContent {
            MyApplicationTheme {
                var currentScreen by remember { mutableStateOf(AppScreen.LANDING) }
                var selectedAgeGroup by remember { mutableStateOf(AgeGroup.PRESCHOOL) }

                // Intercept back button when inside category screens
                BackHandler(enabled = currentScreen != AppScreen.LANDING) {
                    MelodySoundEngine.playBubblePop()
                    currentScreen = AppScreen.LANDING
                }

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    AnimatedContent(
                        targetState = currentScreen,
                        transitionSpec = { fadeIn() togetherWith fadeOut() },
                        label = "screen_transition"
                    ) { screen ->
                        when (screen) {
                            AppScreen.LANDING -> {
                                LandingScreen(
                                    currentAgeGroup = selectedAgeGroup,
                                    onAgeGroupSelected = { age ->
                                        selectedAgeGroup = age
                                    },
                                    onCategorySelected = { category ->
                                        currentScreen = when (category) {
                                            CategoryType.ALPHABETS, CategoryType.COLORS_SHAPES -> AppScreen.ALPHABETS
                                            CategoryType.WORDS -> AppScreen.WORDS
                                            CategoryType.ANIMALS -> AppScreen.ANIMALS
                                            CategoryType.BIRDS -> AppScreen.BIRDS
                                            CategoryType.NUMBERS, CategoryType.MATH_FUN -> AppScreen.NUMBERS
                                        }
                                    },
                                    onSpeak = { text ->
                                        speakPleasant(text)
                                    },
                                    modifier = Modifier.padding(innerPadding)
                                )
                            }
                            AppScreen.ALPHABETS -> {
                                AlphabetDrawingScreen(
                                    onSpeak = { text ->
                                        speakPleasant(text)
                                    },
                                    onSpeakPhonic = { phonicSound ->
                                        speakClearPhonic(phonicSound)
                                    },
                                    onBackToHome = {
                                        currentScreen = AppScreen.LANDING
                                    },
                                    modifier = Modifier.padding(innerPadding)
                                )
                            }
                            AppScreen.WORDS -> {
                                WordsScreen(
                                    ageGroup = selectedAgeGroup,
                                    onBackToHome = {
                                        currentScreen = AppScreen.LANDING
                                    },
                                    onSpeak = { text ->
                                        speakPleasant(text)
                                    },
                                    onSpeakPhonic = { phonicSound ->
                                        speakClearPhonic(phonicSound)
                                    },
                                    modifier = Modifier.padding(innerPadding)
                                )
                            }
                            AppScreen.ANIMALS -> {
                                AnimalsScreen(
                                    ageGroup = selectedAgeGroup,
                                    onBackToHome = {
                                        currentScreen = AppScreen.LANDING
                                    },
                                    onSpeak = { text ->
                                        speakPleasant(text)
                                    },
                                    modifier = Modifier.padding(innerPadding)
                                )
                            }
                            AppScreen.BIRDS -> {
                                BirdsScreen(
                                    ageGroup = selectedAgeGroup,
                                    onBackToHome = {
                                        currentScreen = AppScreen.LANDING
                                    },
                                    onSpeak = { text ->
                                        speakPleasant(text)
                                    },
                                    modifier = Modifier.padding(innerPadding)
                                )
                            }
                            AppScreen.NUMBERS -> {
                                NumbersScreen(
                                    ageGroup = selectedAgeGroup,
                                    onBackToHome = {
                                        currentScreen = AppScreen.LANDING
                                    },
                                    onSpeak = { text ->
                                        speakPleasant(text)
                                    },
                                    modifier = Modifier.padding(innerPadding)
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            textToSpeech?.let { tts ->
                tts.setAudioAttributes(
                    AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_ASSISTANCE_SONIFICATION)
                        .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                        .build()
                )

                val result = tts.setLanguage(Locale.US)
                if (result != TextToSpeech.LANG_MISSING_DATA && result != TextToSpeech.LANG_NOT_SUPPORTED) {
                    isTtsReady = true
                    applySoftPleasantVoice(tts)
                }
            }
        }
    }

    private fun applySoftPleasantVoice(tts: TextToSpeech) {
        try {
            val availableVoices = tts.voices
            if (!availableVoices.isNullOrEmpty()) {
                val softVoice = availableVoices
                    .filter { it.locale.language.equals("en", ignoreCase = true) && !it.isNetworkConnectionRequired }
                    .maxByOrNull { voice ->
                        var score = 0
                        val name = voice.name.lowercase()
                        if (voice.quality >= Voice.QUALITY_HIGH) score += 40
                        if (name.contains("female") || name.contains("-fem") ||
                            name.contains("sfg") || name.contains("tpd") ||
                            name.contains("iob") || name.contains("rjs")
                        ) {
                            score += 100
                        }
                        if (!name.contains("male") || name.contains("female")) {
                            score += 20
                        }
                        score
                    }

                if (softVoice != null) {
                    tts.voice = softVoice
                }
            }
        } catch (_: Exception) {
            // Fallback to default engine configuration
        }

        tts.setPitch(1.02f)     // Warm, pleasant, friendly pitch
        tts.setSpeechRate(0.82f) // Gentle, calm, soothing pace for kids
    }

    /**
     * Speaks sentences or words in a soft, encouraging tone.
     */
    private fun speakPleasant(text: String) {
        if (isTtsReady) {
            textToSpeech?.let { tts ->
                tts.setPitch(1.02f)
                tts.setSpeechRate(0.82f)
                val params = Bundle().apply {
                    putFloat(TextToSpeech.Engine.KEY_PARAM_VOLUME, 0.88f)
                }
                tts.speak(text, TextToSpeech.QUEUE_FLUSH, params, "PleasantVoice")
            }
        }
    }

    /**
     * Speaks clear, isolated phonic sounds (e.g. "Ah", "Buh", "Kuh").
     */
    private fun speakClearPhonic(sound: String) {
        if (isTtsReady) {
            textToSpeech?.let { tts ->
                tts.setPitch(1.04f)
                tts.setSpeechRate(0.75f)
                val params = Bundle().apply {
                    putFloat(TextToSpeech.Engine.KEY_PARAM_VOLUME, 0.90f)
                }
                tts.speak(sound, TextToSpeech.QUEUE_FLUSH, params, "PhonicVoice")
            }
        }
    }

    override fun onDestroy() {
        MelodySoundEngine.stopLullaby()
        textToSpeech?.stop()
        textToSpeech?.shutdown()
        super.onDestroy()
    }
}

@Preview(showBackground = true)
@Composable
fun LandingScreenPreview() {
    MyApplicationTheme {
        LandingScreen(
            currentAgeGroup = AgeGroup.PRESCHOOL,
            onAgeGroupSelected = {},
            onCategorySelected = {},
            onSpeak = {}
        )
    }
}