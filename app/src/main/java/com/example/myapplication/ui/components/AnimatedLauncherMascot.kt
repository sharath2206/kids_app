package com.example.myapplication.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.myapplication.audio.MelodySoundEngine
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

/**
 * Animated Launcher Mascot based on the app's cheerful star launcher icon.
 * Features idle bobbing, orbital twinkling sparkles, floating musical notes,
 * and an energetic squash-and-stretch bounce on tap with melodious chimes.
 */
@Composable
fun AnimatedLauncherMascot(
    modifier: Modifier = Modifier,
    size: Dp = 100.dp,
    onMascotTap: (() -> Unit)? = null
) {
    val coroutineScope = rememberCoroutineScope()
    val bounceScale = remember { Animatable(1f) }
    val bounceRotation = remember { Animatable(0f) }
    var isWinking by remember { mutableStateOf(false) }

    // Periodic automatic winking
    LaunchedEffect(Unit) {
        while (true) {
            delay(3200L)
            isWinking = true
            delay(280L)
            isWinking = false
        }
    }

    // Infinite gentle floating & breathing transition
    val infiniteTransition = rememberInfiniteTransition(label = "mascot_idle")
    val floatOffset by infiniteTransition.animateFloat(
        initialValue = -5f,
        targetValue = 6f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "floating"
    )

    val breathingScale by infiniteTransition.animateFloat(
        initialValue = 0.97f,
        targetValue = 1.03f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1500, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "breathing"
    )

    val sparkleAngle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 6000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "sparkle_orbit"
    )

    val musicalNoteOffset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = -16f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "notes_float"
    )

    Box(
        modifier = modifier
            .size(size)
            .offset(y = floatOffset.dp)
            .scale(breathingScale * bounceScale.value)
            .rotate(bounceRotation.value)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                coroutineScope.launch {
                    MelodySoundEngine.playCelebrationChime()
                    // Squash and stretch bounce animation
                    bounceScale.animateTo(0.82f, tween(100))
                    launch {
                        bounceRotation.animateTo(12f, tween(120))
                        bounceRotation.animateTo(-10f, tween(140))
                        bounceRotation.animateTo(0f, spring())
                    }
                    bounceScale.animateTo(1.18f, spring(dampingRatio = 0.45f, stiffness = 400f))
                    bounceScale.animateTo(1.0f, spring())
                }
                onMascotTap?.invoke()
            },
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.size(size)) {
            val width = this.size.width
            val height = this.size.height
            val center = Offset(width / 2f, height / 2f)

            // 1. Soft glowing aura
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        Color(0x60FFE082),
                        Color(0x20FFD54F),
                        Color.Transparent
                    ),
                    center = center,
                    radius = width * 0.48f
                ),
                radius = width * 0.48f,
                center = center
            )

            // 2. Cute smiling star body
            drawStarMascot(
                center = center,
                radius = width * 0.38f,
                innerRadius = width * 0.20f,
                isWinking = isWinking
            )

            // 3. Orbiting twinkling sparkles
            drawOrbitingSparkles(
                center = center,
                radius = width * 0.43f,
                angle = sparkleAngle
            )

            // 4. Floating musical notes
            drawFloatingMusicalNotes(
                center = center,
                width = width,
                offsetY = musicalNoteOffset
            )
        }
    }
}

/**
 * Draws the 5-point mascot star with cute eyes, smile, blush cheeks, and pencil wand.
 */
private fun DrawScope.drawStarMascot(
    center: Offset,
    radius: Float,
    innerRadius: Float,
    isWinking: Boolean
) {
    val points = 5
    val starPath = Path()
    var angle = -PI / 2.0 // start at top point
    val angleStep = PI / points

    for (i in 0 until (points * 2)) {
        val r = if (i % 2 == 0) radius else innerRadius
        val x = (center.x + r * cos(angle)).toFloat()
        val y = (center.y + r * sin(angle)).toFloat()
        if (i == 0) {
            starPath.moveTo(x, y)
        } else {
            starPath.lineTo(x, y)
        }
        angle += angleStep
    }
    starPath.close()

    // Fill star with warm sunshine gradient
    val starBrush = Brush.linearGradient(
        colors = listOf(Color(0xFFFFF176), Color(0xFFFFD54F), Color(0xFFFFB300)),
        start = Offset(center.x, center.y - radius),
        end = Offset(center.x, center.y + radius)
    )
    drawPath(path = starPath, brush = starBrush, style = Fill)

    // Vibrant border outline
    drawPath(
        path = starPath,
        color = Color(0xFFFB8C00),
        style = Stroke(width = radius * 0.07f)
    )

    // Left Eye (Big and sparkling)
    val eyeOffsetX = radius * 0.32f
    val eyeOffsetY = -radius * 0.04f
    val leftEyeCenter = Offset(center.x - eyeOffsetX, center.y + eyeOffsetY)
    val rightEyeCenter = Offset(center.x + eyeOffsetX, center.y + eyeOffsetY)
    val eyeRadius = radius * 0.13f

    drawCircle(
        color = Color(0xFF212121),
        radius = eyeRadius,
        center = leftEyeCenter
    )
    // Left eye sparkle highlight
    drawCircle(
        color = Color.White,
        radius = eyeRadius * 0.42f,
        center = Offset(leftEyeCenter.x + eyeRadius * 0.28f, leftEyeCenter.y - eyeRadius * 0.28f)
    )

    // Right Eye (Open or Winking)
    if (isWinking) {
        // Winking eye arc
        val winkPath = Path().apply {
            moveTo(rightEyeCenter.x - eyeRadius, rightEyeCenter.y)
            quadraticTo(
                rightEyeCenter.x,
                rightEyeCenter.y + eyeRadius * 1.2f,
                rightEyeCenter.x + eyeRadius,
                rightEyeCenter.y
            )
        }
        drawPath(
            path = winkPath,
            color = Color(0xFF212121),
            style = Stroke(width = eyeRadius * 0.35f)
        )
    } else {
        drawCircle(
            color = Color(0xFF212121),
            radius = eyeRadius,
            center = rightEyeCenter
        )
        // Right eye sparkle highlight
        drawCircle(
            color = Color.White,
            radius = eyeRadius * 0.42f,
            center = Offset(rightEyeCenter.x + eyeRadius * 0.28f, rightEyeCenter.y - eyeRadius * 0.28f)
        )
    }

    // Rosy Blush Cheeks
    val blushRadius = radius * 0.12f
    val blushOffsetY = radius * 0.15f
    drawCircle(
        color = Color(0x75FF4081),
        radius = blushRadius,
        center = Offset(center.x - eyeOffsetX - blushRadius * 0.4f, center.y + blushOffsetY)
    )
    drawCircle(
        color = Color(0x75FF4081),
        radius = blushRadius,
        center = Offset(center.x + eyeOffsetX + blushRadius * 0.4f, center.y + blushOffsetY)
    )

    // Sweet Smiling Mouth
    val mouthPath = Path().apply {
        moveTo(center.x - radius * 0.16f, center.y + radius * 0.18f)
        quadraticTo(
            center.x,
            center.y + radius * 0.38f,
            center.x + radius * 0.16f,
            center.y + radius * 0.18f
        )
    }
    drawPath(
        path = mouthPath,
        color = Color(0xFFD32F2F),
        style = Stroke(width = radius * 0.08f)
    )

    // Cute Magic Wand held on the side
    val wandStart = Offset(center.x + radius * 0.45f, center.y + radius * 0.25f)
    val wandEnd = Offset(center.x + radius * 0.85f, center.y - radius * 0.25f)
    drawLine(
        color = Color(0xFFE91E63),
        start = wandStart,
        end = wandEnd,
        strokeWidth = radius * 0.08f
    )
    // Magic Star Wand Tip
    drawCircle(
        color = Color(0xFFFFD54F),
        radius = radius * 0.12f,
        center = wandEnd
    )
    drawCircle(
        color = Color.White,
        radius = radius * 0.06f,
        center = wandEnd
    )
}

/**
 * Draws twinkles orbiting around the mascot.
 */
private fun DrawScope.drawOrbitingSparkles(center: Offset, radius: Float, angle: Float) {
    val rad1 = (angle * PI / 180.0).toFloat()
    val rad2 = ((angle + 120f) * PI / 180.0).toFloat()
    val rad3 = ((angle + 240f) * PI / 180.0).toFloat()

    drawFourPointStar(
        center = Offset(center.x + radius * cos(rad1), center.y + radius * sin(rad1)),
        size = radius * 0.16f,
        color = Color(0xFFFFEB3B)
    )
    drawFourPointStar(
        center = Offset(center.x + radius * 0.92f * cos(rad2), center.y + radius * 0.92f * sin(rad2)),
        size = radius * 0.14f,
        color = Color(0xFF00E5FF)
    )
    drawFourPointStar(
        center = Offset(center.x + radius * 0.88f * cos(rad3), center.y + radius * 0.88f * sin(rad3)),
        size = radius * 0.13f,
        color = Color(0xFFFF4081)
    )
}

/**
 * Draws a clean 4-point sparkle diamond.
 */
private fun DrawScope.drawFourPointStar(center: Offset, size: Float, color: Color) {
    val path = Path().apply {
        moveTo(center.x, center.y - size)
        lineTo(center.x + size * 0.35f, center.y - size * 0.35f)
        lineTo(center.x + size, center.y)
        lineTo(center.x + size * 0.35f, center.y + size * 0.35f)
        lineTo(center.x, center.y + size)
        lineTo(center.x - size * 0.35f, center.y + size * 0.35f)
        lineTo(center.x - size, center.y)
        lineTo(center.x - size * 0.35f, center.y - size * 0.35f)
        close()
    }
    drawPath(path = path, color = color, style = Fill)
}

/**
 * Draws gentle floating musical notes near the top.
 */
private fun DrawScope.drawFloatingMusicalNotes(center: Offset, width: Float, offsetY: Float) {
    val noteX = center.x - width * 0.36f
    val noteY = center.y - width * 0.28f + offsetY

    // Draw eighth note (circle + stem + flag)
    drawCircle(
        color = Color(0xFF9C27B0),
        radius = width * 0.038f,
        center = Offset(noteX, noteY)
    )
    drawLine(
        color = Color(0xFF9C27B0),
        start = Offset(noteX + width * 0.036f, noteY),
        end = Offset(noteX + width * 0.036f, noteY - width * 0.10f),
        strokeWidth = width * 0.016f
    )
    drawLine(
        color = Color(0xFF9C27B0),
        start = Offset(noteX + width * 0.036f, noteY - width * 0.10f),
        end = Offset(noteX + width * 0.075f, noteY - width * 0.07f),
        strokeWidth = width * 0.022f
    )
}
