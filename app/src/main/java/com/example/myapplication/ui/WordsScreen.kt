package com.example.myapplication.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.automirrored.rounded.VolumeUp
import androidx.compose.material.icons.rounded.AutoAwesome
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.audio.MelodySoundEngine
import com.example.myapplication.model.AgeGroup
import com.example.myapplication.model.WordItem
import com.example.myapplication.model.getWordsForAge
import com.example.myapplication.ui.components.ConfettiEffect

@Composable
fun WordsScreen(
    ageGroup: AgeGroup,
    onBackToHome: () -> Unit,
    onSpeak: (String) -> Unit,
    onSpeakPhonic: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val words = remember(ageGroup) { getWordsForAge(ageGroup) }
    var currentWordIndex by remember { mutableIntStateOf(0) }
    val currentWord = words[currentWordIndex]

    var activeLetterHighlight by remember { mutableIntStateOf(-1) }
    var celebrationTrigger by remember { mutableLongStateOf(0L) }

    LaunchedEffect(currentWordIndex) {
        activeLetterHighlight = -1
        MelodySoundEngine.playAlphabetChime(currentWord.word.first())
        onSpeak(currentWord.word)
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF9FBFF))
            .padding(14.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Header: Back button + Title + Age group badge
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(
                        onClick = {
                            MelodySoundEngine.playBubblePop()
                            onBackToHome()
                        },
                        modifier = Modifier
                            .size(42.dp)
                            .background(Color.White, CircleShape)
                            .border(1.5.dp, Color(0xFFE0E0E0), CircleShape)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                            contentDescription = "Back",
                            tint = Color(0xFF37474F)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "Phonics Words",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color(0xFF263238)
                        )
                        Text(
                            text = "Tap each letter to hear its sound!",
                            fontSize = 12.sp,
                            color = Color(0xFF78909C)
                        )
                    }
                }

                // Age badge
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(ageGroup.primaryColor.copy(alpha = 0.15f))
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = "${ageGroup.icon} ${ageGroup.title}",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = ageGroup.primaryColor
                    )
                }
            }

            // Word Selection Carousel
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                itemsIndexed(words) { index, item ->
                    val isSelected = index == currentWordIndex
                    Card(
                        modifier = Modifier
                            .clickable {
                                currentWordIndex = index
                            },
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (isSelected) item.color else Color.White
                        ),
                        elevation = CardDefaults.cardElevation(defaultElevation = if (isSelected) 6.dp else 2.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text(text = item.emoji, fontSize = 20.sp)
                            Text(
                                text = item.word,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isSelected) Color.White else Color(0xFF37474F)
                            )
                        }
                    }
                }
            }

            // Central Featured Word Hero Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(26.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Big Emoji Illustration with circular backdrop
                    Box(
                        modifier = Modifier
                            .size(110.dp)
                            .background(
                                Brush.radialGradient(
                                    listOf(
                                        currentWord.color.copy(alpha = 0.25f),
                                        currentWord.color.copy(alpha = 0.05f),
                                        Color.Transparent
                                    )
                                ),
                                CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = currentWord.emoji,
                            fontSize = 64.sp
                        )
                    }

                    // Interactive Phonics Letters
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        currentWord.word.forEachIndexed { idx, char ->
                            val isHighlighted = activeLetterHighlight == idx
                            val scale by animateFloatAsState(
                                targetValue = if (isHighlighted) 1.22f else 1.0f,
                                animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
                                label = "letter_bounce"
                            )

                            Box(
                                modifier = Modifier
                                    .size(56.dp)
                                    .scale(scale)
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(
                                        if (isHighlighted) currentWord.color else currentWord.color.copy(alpha = 0.12f)
                                    )
                                    .border(
                                        width = if (isHighlighted) 2.5.dp else 1.dp,
                                        color = currentWord.color,
                                        shape = RoundedCornerShape(16.dp)
                                    )
                                    .clickable {
                                        activeLetterHighlight = idx
                                        MelodySoundEngine.playAlphabetChime(char)
                                        val sound = currentWord.phonicsList.getOrElse(idx) { "$char" }
                                        onSpeakPhonic(sound)
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "$char",
                                    fontSize = 28.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = if (isHighlighted) Color.White else currentWord.color
                                )
                            }
                        }
                    }

                    // Phonic Breakdown Guide text
                    Text(
                        text = currentWord.phonicsList.joinToString("  •  ") { "\"$it\"" },
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF546E7A)
                    )

                    // Spoken sentence description
                    Text(
                        text = currentWord.explanation,
                        fontSize = 14.sp,
                        color = Color(0xFF37474F),
                        fontWeight = FontWeight.Medium
                    )

                    // Action Buttons (Play whole word / Celebrate)
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        ElevatedButton(
                            onClick = {
                                MelodySoundEngine.playAlphabetChime(currentWord.word.first())
                                onSpeak(currentWord.explanation)
                            },
                            shape = RoundedCornerShape(18.dp),
                            colors = ButtonDefaults.elevatedButtonColors(
                                containerColor = currentWord.color,
                                contentColor = Color.White
                            ),
                            contentPadding = androidx.compose.foundation.layout.PaddingValues(
                                horizontal = 18.dp,
                                vertical = 12.dp
                            )
                        ) {
                            Icon(Icons.AutoMirrored.Rounded.VolumeUp, contentDescription = "Listen")
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Hear Word",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        ElevatedButton(
                            onClick = {
                                celebrationTrigger = System.currentTimeMillis()
                                MelodySoundEngine.playCelebrationChime()
                                onSpeak("Great job! You spelled ${currentWord.word}!")
                            },
                            shape = RoundedCornerShape(18.dp),
                            colors = ButtonDefaults.elevatedButtonColors(
                                containerColor = Color(0xFFFFB300),
                                contentColor = Color.White
                            ),
                            contentPadding = androidx.compose.foundation.layout.PaddingValues(
                                horizontal = 18.dp,
                                vertical = 12.dp
                            )
                        ) {
                            Icon(Icons.Rounded.Star, contentDescription = "Star")
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "I Spelled It!",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            // Quick Next / Prev
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                ElevatedButton(
                    onClick = {
                        if (currentWordIndex > 0) currentWordIndex--
                    },
                    enabled = currentWordIndex > 0,
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.elevatedButtonColors(containerColor = Color(0xFF607D8B), contentColor = Color.White)
                ) {
                    Text("Previous Word", fontWeight = FontWeight.Bold)
                }

                ElevatedButton(
                    onClick = {
                        if (currentWordIndex < words.size - 1) currentWordIndex++
                    },
                    enabled = currentWordIndex < words.size - 1,
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.elevatedButtonColors(containerColor = Color(0xFF4CAF50), contentColor = Color.White)
                ) {
                    Text("Next Word", fontWeight = FontWeight.Bold)
                }
            }
        }

        ConfettiEffect(trigger = celebrationTrigger)
    }
}
