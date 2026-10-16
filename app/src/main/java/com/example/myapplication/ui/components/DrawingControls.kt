package com.example.myapplication.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.Undo
import androidx.compose.material.icons.rounded.AutoAwesome
import androidx.compose.material.icons.rounded.CleaningServices
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Visibility
import androidx.compose.material.icons.rounded.VisibilityOff
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.model.CanvasTheme

val KidColors = listOf(
    Color(0xFFE53935), // Red
    Color(0xFFFF9800), // Orange
    Color(0xFFFFD600), // Sunshine Yellow
    Color(0xFF43A047), // Green
    Color(0xFF00ACC1), // Cyan / Sky
    Color(0xFF1E88E5), // Blue
    Color(0xFF8E24AA), // Purple
    Color(0xFFFF4081), // Pink
    Color(0xFF6D4C41), // Brown
    Color(0xFF212121)  // Dark
)

@Composable
fun DrawingControls(
    selectedColor: Color,
    onColorSelected: (Color) -> Unit,
    isRainbowMode: Boolean,
    onRainbowToggle: () -> Unit,
    isEraserMode: Boolean,
    onEraserToggle: () -> Unit,
    strokeWidth: Float,
    onStrokeWidthSelected: (Float) -> Unit,
    canUndo: Boolean,
    onUndo: () -> Unit,
    onClear: () -> Unit,
    showLetterGuide: Boolean,
    onToggleLetterGuide: () -> Unit,
    canvasTheme: CanvasTheme,
    onThemeSelected: (CanvasTheme) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF7F7FD)),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Row 1: Action bar (Guide toggle, Brush sizes, Undo, Clear)
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Brush sizes (Thin, Medium, Thick)
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    val sizes = listOf(14f to "Thin", 28f to "Med", 46f to "Chubby")
                    sizes.forEach { (width, label) ->
                        val isSelected = strokeWidth == width && !isEraserMode
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = if (isSelected) Color(0xFFFF9800) else Color(0xFFECEFF1),
                            modifier = Modifier
                                .clickable {
                                    onStrokeWidthSelected(width)
                                }
                        ) {
                            Box(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Box(
                                        modifier = Modifier
                                            .size((width / 3).dp.coerceIn(6.dp, 16.dp))
                                            .clip(CircleShape)
                                            .background(if (isSelected) Color.White else Color.DarkGray)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = label,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isSelected) Color.White else Color.DarkGray
                                    )
                                }
                            }
                        }
                    }
                }

                // Guide Toggle & Actions (Undo, Clear)
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    // Guide toggle
                    IconButton(
                        onClick = onToggleLetterGuide,
                        modifier = Modifier
                            .size(38.dp)
                            .background(
                                if (showLetterGuide) Color(0xFFE1BEE7) else Color(0xFFEEEEEE),
                                CircleShape
                            )
                    ) {
                        Icon(
                            imageVector = if (showLetterGuide) Icons.Rounded.Visibility else Icons.Rounded.VisibilityOff,
                            contentDescription = "Toggle letter guide",
                            tint = if (showLetterGuide) Color(0xFF6A1B9A) else Color.Gray,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    // Undo
                    IconButton(
                        onClick = onUndo,
                        enabled = canUndo,
                        modifier = Modifier
                            .size(38.dp)
                            .background(
                                if (canUndo) Color(0xFFFFE0B2) else Color(0xFFEEEEEE),
                                CircleShape
                            )
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Rounded.Undo,
                            contentDescription = "Undo",
                            tint = if (canUndo) Color(0xFFE65100) else Color.LightGray,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    // Clear
                    IconButton(
                        onClick = onClear,
                        modifier = Modifier
                            .size(38.dp)
                            .background(Color(0xFFFFCDD2), CircleShape)
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Delete,
                            contentDescription = "Clear",
                            tint = Color(0xFFC62828),
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }

            // Row 2: Color Palette + Rainbow Magic + Eraser
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Rainbow Magic Brush Button
                val rainbowBrush = Brush.sweepGradient(
                    listOf(
                        Color.Red, Color.Yellow, Color.Green,
                        Color.Cyan, Color.Blue, Color.Magenta, Color.Red
                    )
                )
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .clip(CircleShape)
                        .background(rainbowBrush)
                        .border(
                            width = if (isRainbowMode) 3.5.dp else 1.dp,
                            color = if (isRainbowMode) Color.White else Color.Transparent,
                            shape = CircleShape
                        )
                        .clickable { onRainbowToggle() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Rounded.AutoAwesome,
                        contentDescription = "Rainbow Brush",
                        tint = Color.White,
                        modifier = Modifier.size(22.dp)
                    )
                }

                // Regular Colors
                KidColors.forEach { color ->
                    val isSelected = (!isRainbowMode && !isEraserMode && selectedColor == color)
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(color)
                            .border(
                                width = if (isSelected) 3.5.dp else 1.dp,
                                color = if (isSelected) Color(0xFF212121) else Color(0x33000000),
                                shape = CircleShape
                            )
                            .clickable { onColorSelected(color) }
                    )
                }

                // Eraser Tool
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .clip(CircleShape)
                        .background(if (isEraserMode) Color(0xFFFF80AB) else Color(0xFFFCE4EC))
                        .border(
                            width = if (isEraserMode) 3.dp else 1.dp,
                            color = if (isEraserMode) Color(0xFFC2185B) else Color(0xFFF8BBD0),
                            shape = CircleShape
                        )
                        .clickable { onEraserToggle() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Rounded.CleaningServices,
                        contentDescription = "Eraser",
                        tint = if (isEraserMode) Color.White else Color(0xFFC2185B),
                        modifier = Modifier.size(22.dp)
                    )
                }
            }

            // Row 3: Board / Pad Themes (Paper, Magic Scratch, Chalkboard)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                val themes = listOf(
                    CanvasTheme.PAPER_TRACING to "📄 Paper",
                    CanvasTheme.MAGIC_SCRATCH to "✨ Magic Scratch",
                    CanvasTheme.CHALKBOARD to "🟩 Chalkboard"
                )

                themes.forEach { (theme, label) ->
                    val isCurrentTheme = canvasTheme == theme
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = if (isCurrentTheme) Color(0xFF3F51B5) else Color.Transparent,
                        modifier = Modifier
                            .padding(horizontal = 4.dp)
                            .clickable { onThemeSelected(theme) }
                    ) {
                        Text(
                            text = label,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                            fontSize = 12.sp,
                            fontWeight = if (isCurrentTheme) FontWeight.Bold else FontWeight.Medium,
                            color = if (isCurrentTheme) Color.White else Color.Gray
                        )
                    }
                }
            }
        }
    }
}
