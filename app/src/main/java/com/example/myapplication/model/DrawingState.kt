package com.example.myapplication.model

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color

data class DrawingStroke(
    val points: List<Offset>,
    val color: Color,
    val strokeWidth: Float,
    val isRainbow: Boolean = false,
    val isEraser: Boolean = false
)

enum class CanvasTheme {
    PAPER_TRACING,   // Light paper background with tracing guide
    MAGIC_SCRATCH,   // Black scratch pad where finger scratches reveal rainbow colors
    CHALKBOARD       // Green chalkboard with chalk style
}

enum class LetterCaseMode {
    UPPERCASE,       // A
    LOWERCASE,       // a
    BOTH             // Aa
}
