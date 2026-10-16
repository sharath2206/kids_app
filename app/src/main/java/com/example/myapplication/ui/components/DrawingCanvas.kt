package com.example.myapplication.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.VolumeUp
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.model.AlphabetItem
import com.example.myapplication.model.CanvasTheme
import com.example.myapplication.model.DrawingStroke
import com.example.myapplication.model.LetterCaseMode

@Composable
fun DrawingCanvas(
    strokes: List<DrawingStroke>,
    currentStroke: DrawingStroke?,
    onStrokeStarted: (Offset) -> Unit,
    onStrokeMoved: (Offset) -> Unit,
    onStrokeFinished: () -> Unit,
    currentAlphabet: AlphabetItem,
    letterCaseMode: LetterCaseMode,
    showLetterGuide: Boolean,
    canvasTheme: CanvasTheme,
    modifier: Modifier = Modifier
) {
    val backgroundColor = when (canvasTheme) {
        CanvasTheme.PAPER_TRACING -> Color(0xFFFFFDF5) // warm sketch paper
        CanvasTheme.MAGIC_SCRATCH -> Color(0xFF1E1E28) // dark scratch pad
        CanvasTheme.CHALKBOARD -> Color(0xFF20402E)    // classic chalkboard green
    }

    val borderColor = when (canvasTheme) {
        CanvasTheme.PAPER_TRACING -> Color(0xFFFFD54F)
        CanvasTheme.MAGIC_SCRATCH -> Color(0xFF9C27B0)
        CanvasTheme.CHALKBOARD -> Color(0xFF8D6E63)
    }

    val displayLetter = when (letterCaseMode) {
        LetterCaseMode.UPPERCASE -> "${currentAlphabet.letter}"
        LetterCaseMode.LOWERCASE -> "${currentAlphabet.letter.lowercaseChar()}"
        LetterCaseMode.BOTH -> "${currentAlphabet.letter} ${currentAlphabet.letter.lowercaseChar()}"
    }

    val guideColor = when (canvasTheme) {
        CanvasTheme.PAPER_TRACING -> Color(0x33000000)
        CanvasTheme.MAGIC_SCRATCH -> Color(0x2EFFFFFF)
        CanvasTheme.CHALKBOARD -> Color(0x40FFFFFF)
    }

    Box(
        modifier = modifier
            .shadow(8.dp, RoundedCornerShape(24.dp))
            .clip(RoundedCornerShape(24.dp))
            .background(backgroundColor)
            .border(4.dp, borderColor, RoundedCornerShape(24.dp)),
        contentAlignment = Alignment.Center
    ) {
        // Background Tracing Guide
        if (showLetterGuide) {
            Text(
                text = displayLetter,
                fontSize = if (letterCaseMode == LetterCaseMode.BOTH) 130.sp else 190.sp,
                fontWeight = FontWeight.Black,
                fontFamily = FontFamily.SansSerif,
                color = guideColor,
                letterSpacing = 4.sp
            )
        }

        // Live Phonics Pill Indicator while drawing
        AnimatedVisibility(
            visible = currentStroke != null && !currentStroke.isEraser,
            enter = fadeIn() + scaleIn(),
            exit = fadeOut() + scaleOut(),
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 16.dp)
        ) {
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = currentAlphabet.color.copy(alpha = 0.95f),
                shadowElevation = 6.dp
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Rounded.VolumeUp,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(18.dp)
                    )
                    Text(
                        text = "${currentAlphabet.letter} says \"${currentAlphabet.phonicSound}\"",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
        }

        // Foreground Drawing Canvas
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(canvasTheme) {
                    detectDragGestures(
                        onDragStart = { offset ->
                            onStrokeStarted(offset)
                        },
                        onDrag = { change, _ ->
                            change.consume()
                            onStrokeMoved(change.position)
                        },
                        onDragEnd = {
                            onStrokeFinished()
                        },
                        onDragCancel = {
                            onStrokeFinished()
                        }
                    )
                }
        ) {
            val allStrokes = if (currentStroke != null) strokes + currentStroke else strokes

            for (stroke in allStrokes) {
                if (stroke.points.size < 2) {
                    if (stroke.points.isNotEmpty()) {
                        drawCircle(
                            color = if (stroke.isEraser) backgroundColor else stroke.color,
                            radius = stroke.strokeWidth / 2,
                            center = stroke.points[0]
                        )
                    }
                    continue
                }

                if (stroke.isRainbow && !stroke.isEraser) {
                    // Draw each segment with cycling vibrant hues
                    for (i in 0 until stroke.points.size - 1) {
                        val p1 = stroke.points[i]
                        val p2 = stroke.points[i + 1]
                        val hue = (i * 12f) % 360f
                        val segmentColor = Color.hsv(hue, 0.9f, 1f)

                        drawLine(
                            color = segmentColor,
                            start = p1,
                            end = p2,
                            strokeWidth = stroke.strokeWidth,
                            cap = StrokeCap.Round
                        )
                    }
                } else {
                    val drawColor = if (stroke.isEraser) backgroundColor else stroke.color
                    val path = Path().apply {
                        moveTo(stroke.points[0].x, stroke.points[0].y)
                        for (i in 1 until stroke.points.size) {
                            val prev = stroke.points[i - 1]
                            val curr = stroke.points[i]
                            val midX = (prev.x + curr.x) / 2f
                            val midY = (prev.y + curr.y) / 2f
                            quadraticTo(prev.x, prev.y, midX, midY)
                        }
                        lineTo(stroke.points.last().x, stroke.points.last().y)
                    }

                    drawPath(
                        path = path,
                        color = drawColor,
                        style = Stroke(
                            width = stroke.strokeWidth,
                            cap = StrokeCap.Round,
                            join = StrokeJoin.Round
                        )
                    )
                }
            }
        }
    }
}
