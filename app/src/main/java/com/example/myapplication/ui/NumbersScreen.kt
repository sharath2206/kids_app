package com.example.myapplication.ui

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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.audio.MelodySoundEngine
import com.example.myapplication.model.AgeGroup
import com.example.myapplication.model.getNumbersForAge
import com.example.myapplication.ui.components.ConfettiEffect

@Composable
fun NumbersScreen(
    ageGroup: AgeGroup,
    onBackToHome: () -> Unit,
    onSpeak: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val numbers = remember(ageGroup) { getNumbersForAge(ageGroup) }
    var currentNumberIndex by remember { mutableIntStateOf(0) }
    val currentNumber = numbers[currentNumberIndex]

    var tappedCount by remember { mutableIntStateOf(0) }
    var celebrationTrigger by remember { mutableLongStateOf(0L) }

    LaunchedEffect(currentNumberIndex) {
        tappedCount = 0
        MelodySoundEngine.playNumberChime(currentNumber.value)
        onSpeak("Number ${currentNumber.value}. ${currentNumber.word}!")
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFFBF8FD))
            .padding(14.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Header
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
                            text = "Melodic Numbers",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color(0xFF6A1B9A)
                        )
                        Text(
                            text = "Count each item with musical chimes!",
                            fontSize = 12.sp,
                            color = Color(0xFF78909C)
                        )
                    }
                }

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

            // Number Selector Row
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                itemsIndexed(numbers) { index, item ->
                    val isSelected = index == currentNumberIndex
                    Box(
                        modifier = Modifier
                            .size(if (isSelected) 50.dp else 42.dp)
                            .clip(RoundedCornerShape(14.dp))
                            .background(if (isSelected) item.color else Color.White)
                            .border(
                                width = if (isSelected) 2.5.dp else 1.dp,
                                color = if (isSelected) item.color else Color(0xFFE0E0E0),
                                shape = RoundedCornerShape(14.dp)
                            )
                            .clickable {
                                currentNumberIndex = index
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "${item.value}",
                            fontSize = if (isSelected) 22.sp else 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isSelected) Color.White else Color(0xFF455A64)
                        )
                    }
                }
            }

            // Featured Hero Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(26.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 5.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Big Number Badge & Word
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(76.dp)
                                .clip(RoundedCornerShape(20.dp))
                                .background(currentNumber.color),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "${currentNumber.value}",
                                fontSize = 42.sp,
                                fontWeight = FontWeight.Black,
                                color = Color.White
                            )
                        }

                        Column {
                            Text(
                                text = currentNumber.word,
                                fontSize = 28.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = currentNumber.color
                            )
                            Text(
                                text = "Count the ${currentNumber.emoji} items below!",
                                fontSize = 14.sp,
                                color = Color(0xFF546E7A)
                            )
                        }
                    }

                    // Interactive Count Bubbles
                    Text(
                        text = "Tapped: $tappedCount / ${currentNumber.value}",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (tappedCount == currentNumber.value) Color(0xFF2E7D32) else Color(0xFF455A64)
                    )

                    // Grid / Flow of Emojis to Tap and Count
                    val itemsPerRow = 5
                    val rows = (currentNumber.value + itemsPerRow - 1) / itemsPerRow

                    Column(
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        for (r in 0 until rows) {
                            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                                for (c in 0 until itemsPerRow) {
                                    val itemIdx = r * itemsPerRow + c
                                    if (itemIdx < currentNumber.value) {
                                        val isTapped = itemIdx < tappedCount
                                        val scale by animateFloatAsState(
                                            targetValue = if (isTapped) 1.15f else 1.0f,
                                            animationSpec = spring(),
                                            label = "emoji_scale"
                                        )

                                        Box(
                                            modifier = Modifier
                                                .size(52.dp)
                                                .scale(scale)
                                                .clip(RoundedCornerShape(16.dp))
                                                .background(
                                                    if (isTapped) currentNumber.color.copy(alpha = 0.22f) else Color(0xFFF5F5F5)
                                                )
                                                .border(
                                                    width = if (isTapped) 2.dp else 1.dp,
                                                    color = if (isTapped) currentNumber.color else Color(0xFFE0E0E0),
                                                    shape = RoundedCornerShape(16.dp)
                                                )
                                                .clickable {
                                                    tappedCount = (itemIdx + 1)
                                                    MelodySoundEngine.playNumberChime(itemIdx + 1)
                                                    onSpeak("${itemIdx + 1}")
                                                    if (tappedCount == currentNumber.value) {
                                                        celebrationTrigger = System.currentTimeMillis()
                                                        MelodySoundEngine.playCelebrationChime()
                                                        onSpeak("Hooray! You counted all ${currentNumber.value} ${currentNumber.word}!")
                                                    }
                                                },
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(text = currentNumber.emoji, fontSize = 28.sp)
                                        }
                                    }
                                }
                            }
                        }
                    }

                    // Audio Pronunciation & Celebrate Button
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        ElevatedButton(
                            onClick = {
                                MelodySoundEngine.playNumberChime(currentNumber.value)
                                onSpeak("${currentNumber.value}... ${currentNumber.word}!")
                            },
                            shape = RoundedCornerShape(18.dp),
                            colors = ButtonDefaults.elevatedButtonColors(
                                containerColor = currentNumber.color,
                                contentColor = Color.White
                            )
                        ) {
                            Icon(Icons.AutoMirrored.Rounded.VolumeUp, contentDescription = "Hear")
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Hear Number", fontWeight = FontWeight.Bold)
                        }

                        ElevatedButton(
                            onClick = {
                                celebrationTrigger = System.currentTimeMillis()
                                MelodySoundEngine.playCelebrationChime()
                                onSpeak("Great job counting ${currentNumber.value}!")
                            },
                            shape = RoundedCornerShape(18.dp),
                            colors = ButtonDefaults.elevatedButtonColors(
                                containerColor = Color(0xFFFFB300),
                                contentColor = Color.White
                            )
                        ) {
                            Icon(Icons.Rounded.Star, contentDescription = "Star")
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Good Job!", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            // Next / Previous Navigation
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                ElevatedButton(
                    onClick = {
                        if (currentNumberIndex > 0) currentNumberIndex--
                    },
                    enabled = currentNumberIndex > 0,
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.elevatedButtonColors(containerColor = Color(0xFF78909C), contentColor = Color.White)
                ) {
                    Text("Previous", fontWeight = FontWeight.Bold)
                }

                ElevatedButton(
                    onClick = {
                        if (currentNumberIndex < numbers.size - 1) currentNumberIndex++
                    },
                    enabled = currentNumberIndex < numbers.size - 1,
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.elevatedButtonColors(containerColor = Color(0xFF4CAF50), contentColor = Color.White)
                ) {
                    Text("Next", fontWeight = FontWeight.Bold)
                }
            }
        }

        ConfettiEffect(trigger = celebrationTrigger)
    }
}
