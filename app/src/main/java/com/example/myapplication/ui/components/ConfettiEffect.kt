package com.example.myapplication.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.rotate
import kotlin.random.Random

private data class Particle(
    val initialX: Float,
    val initialY: Float,
    val speedX: Float,
    val speedY: Float,
    val color: Color,
    val size: Float,
    val rotationSpeed: Float
)

@Composable
fun ConfettiEffect(
    trigger: Long,
    modifier: Modifier = Modifier
) {
    if (trigger <= 0L) return

    val progress = remember(trigger) { Animatable(0f) }

    val particles = remember(trigger) {
        val colors = listOf(
            Color(0xFFFF1744), Color(0xFFFF9100), Color(0xFFFFEA00),
            Color(0xFF00E676), Color(0xFF00B0FF), Color(0xFFD500F9),
            Color(0xFFFF4081)
        )
        List(45) {
            Particle(
                initialX = Random.nextFloat(),
                initialY = Random.nextFloat() * 0.3f,
                speedX = (Random.nextFloat() - 0.5f) * 600f,
                speedY = Random.nextFloat() * 1200f + 400f,
                color = colors[Random.nextInt(colors.size)],
                size = Random.nextFloat() * 14f + 12f,
                rotationSpeed = (Random.nextFloat() - 0.5f) * 720f
            )
        }
    }

    LaunchedEffect(trigger) {
        progress.snapTo(0f)
        progress.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 2000, easing = LinearEasing)
        )
    }

    if (progress.value < 1f) {
        Canvas(modifier = modifier.fillMaxSize()) {
            val canvasWidth = size.width
            val canvasHeight = size.height

            particles.forEach { p ->
                val currentX = (p.initialX * canvasWidth) + (p.speedX * progress.value)
                val currentY = (p.initialY * canvasHeight) + (p.speedY * progress.value)
                val alpha = (1f - progress.value).coerceIn(0f, 1f)

                rotate(
                    degrees = p.rotationSpeed * progress.value,
                    pivot = Offset(currentX, currentY)
                ) {
                    drawRect(
                        color = p.color.copy(alpha = alpha),
                        topLeft = Offset(currentX - p.size / 2, currentY - p.size / 2),
                        size = androidx.compose.ui.geometry.Size(p.size, p.size * 0.6f)
                    )
                }
            }
        }
    }
}
