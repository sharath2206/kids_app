package com.example.myapplication.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.VolumeMute
import androidx.compose.material.icons.automirrored.rounded.VolumeUp
import androidx.compose.material.icons.rounded.MusicNote
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.audio.MelodySoundEngine
import com.example.myapplication.model.AgeGroup
import com.example.myapplication.model.CategoryTile
import com.example.myapplication.model.CategoryType
import com.example.myapplication.model.getCategoriesForAge
import com.example.myapplication.ui.components.AnimatedLauncherMascot

/**
 * Landing Page featuring:
 * 1. Animated Mascot Hero (Launcher icon character)
 * 2. Background soothing lullaby melody toggle
 * 3. Age Group Selector (Toddlers 1-3, Preschoolers 3-5, Early Learners 5-7+)
 * 4. Dynamic Learning Category Title Windows (Alphabets, Words, Animals, Birds, Numbers)
 */
@Composable
fun LandingScreen(
    currentAgeGroup: AgeGroup,
    onAgeGroupSelected: (AgeGroup) -> Unit,
    onCategorySelected: (CategoryType) -> Unit,
    onSpeak: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var isLullabyActive by remember { mutableStateOf(MelodySoundEngine.isLullabyPlaying) }
    val categories = remember(currentAgeGroup) { getCategoriesForAge(currentAgeGroup) }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFFF1F6FF),
                        Color(0xFFFDFBF7),
                        Color(0xFFF7F2FA)
                    )
                )
            )
            .padding(horizontal = 14.dp, vertical = 8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Top App Bar with Mascot & Lullaby Toggle
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Interactive Animated Mascot + Greeting
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    AnimatedLauncherMascot(
                        size = 72.dp,
                        onMascotTap = {
                            onSpeak("Hi little learner! Tap your age and choose an adventure!")
                        }
                    )

                    Column {
                        Text(
                            text = "Kids Wonder World",
                            fontSize = 21.sp,
                            fontWeight = FontWeight.Black,
                            color = Color(0xFF1A237E)
                        )
                        Text(
                            text = "Soft & Melodious Phonics Learning",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF5C6BC0)
                        )
                    }
                }

                // Calming Melodic Lullaby Music Toggle
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = if (isLullabyActive) Color(0xFFE8F5E9) else Color(0xFFEDE7F6),
                    modifier = Modifier
                        .clickable {
                            MelodySoundEngine.toggleLullaby { active ->
                                isLullabyActive = active
                                if (active) {
                                    onSpeak("Soft melody on")
                                }
                            }
                        }
                        .border(
                            1.5.dp,
                            if (isLullabyActive) Color(0xFF4CAF50) else Color(0xFF7E57C2),
                            RoundedCornerShape(16.dp)
                        )
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.MusicNote,
                            contentDescription = "Lullaby",
                            tint = if (isLullabyActive) Color(0xFF2E7D32) else Color(0xFF5E35B1),
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = if (isLullabyActive) "Melody On" else "Music Box",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isLullabyActive) Color(0xFF2E7D32) else Color(0xFF5E35B1)
                        )
                    }
                }
            }

            // Age Group Selection Section
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Select Kid's Age Group:",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color(0xFF37474F)
                        )
                        Text(
                            text = currentAgeGroup.ageRange,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = currentAgeGroup.primaryColor
                        )
                    }

                    // 3 Age Group Pills
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        AgeGroup.values().forEach { age ->
                            val isSelected = currentAgeGroup == age
                            val scale by animateFloatAsState(
                                targetValue = if (isSelected) 1.03f else 1.0f,
                                animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
                                label = "pill_scale"
                            )

                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .scale(scale)
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(
                                        if (isSelected) {
                                            Brush.horizontalGradient(age.gradientColors)
                                        } else {
                                            Brush.horizontalGradient(listOf(Color(0xFFF5F5F7), Color(0xFFEEEEEE)))
                                        }
                                    )
                                    .border(
                                        width = if (isSelected) 2.dp else 1.dp,
                                        color = if (isSelected) age.primaryColor else Color(0xFFE0E0E0),
                                        shape = RoundedCornerShape(16.dp)
                                    )
                                    .clickable {
                                        MelodySoundEngine.playAlphabetChime(age.title.first())
                                        onAgeGroupSelected(age)
                                        onSpeak("Selected ${age.title}, ${age.ageRange}! ${age.subtitle}")
                                    }
                                    .padding(vertical = 10.dp, horizontal = 4.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.spacedBy(2.dp)
                                ) {
                                    Text(text = age.icon, fontSize = 20.sp)
                                    Text(
                                        text = age.title,
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isSelected) Color.White else Color(0xFF455A64),
                                        textAlign = TextAlign.Center
                                    )
                                    Text(
                                        text = age.ageRange,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = if (isSelected) Color(0xFFFFF9C4) else Color(0xFF78909C)
                                    )
                                }
                            }
                        }
                    }

                    // Age focus description tag
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(currentAgeGroup.primaryColor.copy(alpha = 0.10f))
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = "✨ ${currentAgeGroup.subtitle}",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = currentAgeGroup.primaryColor
                        )
                    }
                }
            }

            // Learning Category Title Windows Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Explore Learning Windows:",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color(0xFF263238)
                )

                Text(
                    text = "${categories.size} Activities",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF78909C)
                )
            }

            // Grid of Category Title Windows
            Column(
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                categories.forEach { tile ->
                    CategoryTileWindow(
                        tile = tile,
                        onClick = {
                            MelodySoundEngine.playAlphabetChime(tile.title.first())
                            onCategorySelected(tile.type)
                        }
                    )
                }
            }

            // Encouragement Banner at bottom
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFFE8EAF6))
                    .padding(12.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(text = "🎵", fontSize = 18.sp)
                    Text(
                        text = "Gentle phonics and melodic chimes nurture early reading & confidence!",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF3949AB)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))
        }
    }
}

/**
 * Individual Learning Category Title Window.
 */
@Composable
private fun CategoryTileWindow(
    tile: CategoryTile,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(14.dp),
                modifier = Modifier.weight(1f)
            ) {
                // Category Emoji Badge with soft circular backdrop
                Box(
                    modifier = Modifier
                        .size(54.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(tile.cardColor.copy(alpha = 0.14f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = tile.emoji,
                        fontSize = 30.sp
                    )
                }

                // Title and Subtitle
                Column {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            text = tile.title,
                            fontSize = 17.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = tile.cardColor
                        )

                        // Feature Tag Badge
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(tile.cardColor.copy(alpha = 0.12f))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = tile.badge,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = tile.cardColor
                            )
                        }
                    }

                    Text(
                        text = tile.subtitle,
                        fontSize = 13.sp,
                        color = Color(0xFF546E7A)
                    )
                }
            }

            // Play/Open arrow button
            Box(
                modifier = Modifier
                    .size(38.dp)
                    .clip(CircleShape)
                    .background(tile.cardColor),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Rounded.PlayArrow,
                    contentDescription = "Open ${tile.title}",
                    tint = Color.White,
                    modifier = Modifier.size(22.dp)
                )
            }
        }
    }
}
