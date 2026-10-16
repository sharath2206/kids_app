package com.example.myapplication.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.myapplication.model.AlphabetList
import com.example.myapplication.model.CanvasTheme
import com.example.myapplication.model.DrawingStroke
import com.example.myapplication.model.LetterCaseMode
import com.example.myapplication.ui.components.AlphabetSelectorBar
import com.example.myapplication.ui.components.ConfettiEffect
import com.example.myapplication.ui.components.DrawingCanvas
import com.example.myapplication.ui.components.DrawingControls
import com.example.myapplication.ui.components.KidColors
import com.example.myapplication.ui.components.NavigationFooter
import com.example.myapplication.ui.components.TopAlphabetHeader

import com.example.myapplication.audio.MelodySoundEngine

@Composable
fun AlphabetDrawingScreen(
    onSpeak: (String) -> Unit,
    onSpeakPhonic: (String) -> Unit,
    onBackToHome: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    var currentIndex by remember { mutableIntStateOf(0) }
    val currentAlphabet = AlphabetList[currentIndex]

    // Preserve drawings for each letter
    val drawingsPerLetter = remember { mutableStateMapOf<Char, List<DrawingStroke>>() }
    val currentLetterStrokes = drawingsPerLetter[currentAlphabet.letter] ?: emptyList()

    var currentStroke by remember { mutableStateOf<DrawingStroke?>(null) }
    var selectedColor by remember { mutableStateOf(KidColors[0]) }
    var isRainbowMode by remember { mutableStateOf(false) }
    var isEraserMode by remember { mutableStateOf(false) }
    var strokeWidth by remember { mutableFloatStateOf(28f) }
    var showLetterGuide by remember { mutableStateOf(true) }
    var canvasTheme by remember { mutableStateOf(CanvasTheme.PAPER_TRACING) }
    var letterCaseMode by remember { mutableStateOf(LetterCaseMode.UPPERCASE) }
    var confettiTrigger by remember { mutableLongStateOf(0L) }

    // Phonic cadence management during drawing
    var lastPhonicTime by remember { mutableLongStateOf(0L) }
    var strokeCount by remember { mutableIntStateOf(0) }

    fun speakCurrentLetterIntro() {
        MelodySoundEngine.playAlphabetChime(currentAlphabet.letter)
        onSpeak("Letter ${currentAlphabet.letter}. ${currentAlphabet.letter} says ${currentAlphabet.phonicSound}... ${currentAlphabet.word}.")
    }

    // Soft intro speech whenever letter changes
    LaunchedEffect(currentIndex) {
        strokeCount = 0
        speakCurrentLetterIntro()
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF9FAFD))
            .padding(horizontal = 12.dp, vertical = 8.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // 1. Top Header with Letter, Word, Phonics, Pronounce, and Done Button
            TopAlphabetHeader(
                currentAlphabet = currentAlphabet,
                letterCaseMode = letterCaseMode,
                onCaseModeToggle = {
                    letterCaseMode = when (letterCaseMode) {
                        LetterCaseMode.UPPERCASE -> LetterCaseMode.LOWERCASE
                        LetterCaseMode.LOWERCASE -> LetterCaseMode.BOTH
                        LetterCaseMode.BOTH -> LetterCaseMode.UPPERCASE
                    }
                },
                onSpeakClicked = {
                    MelodySoundEngine.playAlphabetChime(currentAlphabet.letter)
                    onSpeak(currentAlphabet.phonicExplanation)
                },
                onCelebrateClicked = {
                    confettiTrigger = System.currentTimeMillis()
                    MelodySoundEngine.playCelebrationChime()
                    onSpeak("Wonderful drawing! You drew letter ${currentAlphabet.letter}!")
                },
                onBackToHome = onBackToHome
            )

            // 2. Alphabet Carousel Selector (A - Z)
            AlphabetSelectorBar(
                currentIndex = currentIndex,
                onAlphabetSelected = { index ->
                    currentIndex = index
                }
            )

            // 3. Central Drawing Pad / Canvas with Phonics Trigger
            DrawingCanvas(
                strokes = currentLetterStrokes,
                currentStroke = currentStroke,
                onStrokeStarted = { offset ->
                    currentStroke = DrawingStroke(
                        points = listOf(offset),
                        color = selectedColor,
                        strokeWidth = strokeWidth,
                        isRainbow = isRainbowMode || canvasTheme == CanvasTheme.MAGIC_SCRATCH,
                        isEraser = isEraserMode
                    )

                    // Clear phonic sound triggered immediately when drawing on letter
                    if (!isEraserMode) {
                        MelodySoundEngine.playBubblePop(0.20f)
                        val now = System.currentTimeMillis()
                        if (now - lastPhonicTime > 700L) {
                            lastPhonicTime = now
                            strokeCount++
                            MelodySoundEngine.playAlphabetChime(currentAlphabet.letter)
                            if (strokeCount % 3 == 0) {
                                onSpeak("${currentAlphabet.phonicSound}... ${currentAlphabet.word}!")
                            } else {
                                onSpeakPhonic(currentAlphabet.phonicSound)
                            }
                        }
                    }
                },
                onStrokeMoved = { offset ->
                    currentStroke?.let { stroke ->
                        currentStroke = stroke.copy(points = stroke.points + offset)
                    }
                },
                onStrokeFinished = {
                    currentStroke?.let { stroke ->
                        if (stroke.points.isNotEmpty()) {
                            drawingsPerLetter[currentAlphabet.letter] = currentLetterStrokes + stroke
                        }
                    }
                    currentStroke = null
                },
                currentAlphabet = currentAlphabet,
                letterCaseMode = letterCaseMode,
                showLetterGuide = showLetterGuide,
                canvasTheme = canvasTheme,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            )

            // 4. Drawing Tools & Palette
            DrawingControls(
                selectedColor = selectedColor,
                onColorSelected = { color ->
                    selectedColor = color
                    isRainbowMode = false
                    isEraserMode = false
                },
                isRainbowMode = isRainbowMode,
                onRainbowToggle = {
                    isRainbowMode = !isRainbowMode
                    if (isRainbowMode) isEraserMode = false
                },
                isEraserMode = isEraserMode,
                onEraserToggle = {
                    isEraserMode = !isEraserMode
                    if (isEraserMode) isRainbowMode = false
                },
                strokeWidth = strokeWidth,
                onStrokeWidthSelected = { width ->
                    strokeWidth = width
                },
                canUndo = currentLetterStrokes.isNotEmpty(),
                onUndo = {
                    if (currentLetterStrokes.isNotEmpty()) {
                        drawingsPerLetter[currentAlphabet.letter] = currentLetterStrokes.dropLast(1)
                    }
                },
                onClear = {
                    drawingsPerLetter[currentAlphabet.letter] = emptyList()
                },
                showLetterGuide = showLetterGuide,
                onToggleLetterGuide = {
                    showLetterGuide = !showLetterGuide
                },
                canvasTheme = canvasTheme,
                onThemeSelected = { newTheme ->
                    canvasTheme = newTheme
                    if (newTheme == CanvasTheme.MAGIC_SCRATCH) {
                        isRainbowMode = true
                    }
                }
            )

            // 5. Navigation Footer (Previous / Next)
            NavigationFooter(
                onPrevious = {
                    if (currentIndex > 0) currentIndex--
                },
                onNext = {
                    if (currentIndex < AlphabetList.size - 1) currentIndex++
                },
                hasPrevious = currentIndex > 0,
                hasNext = currentIndex < AlphabetList.size - 1
            )
        }

        // Celebratory Confetti & Stars Overlay
        ConfettiEffect(trigger = confettiTrigger)
    }
}
