package com.example.myapplication.ui

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.automirrored.rounded.VolumeUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.audio.MelodySoundEngine
import com.example.myapplication.model.AgeGroup
import com.example.myapplication.model.getBirdsForAge
import com.example.myapplication.ui.components.ConfettiEffect

@Composable
fun BirdsScreen(
    ageGroup: AgeGroup,
    onBackToHome: () -> Unit,
    onSpeak: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val birds = remember(ageGroup) { getBirdsForAge(ageGroup) }
    var selectedIndex by remember { mutableIntStateOf(0) }
    var confettiTrigger by remember { mutableLongStateOf(0L) }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF0FDF4))
            .padding(14.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
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
                            text = "Melodious Birds",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color(0xFF00796B)
                        )
                        Text(
                            text = "Listen to sweet chirps & bird songs!",
                            fontSize = 12.sp,
                            color = Color(0xFF546E7A)
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

            // Featured Bird Card
            val activeBird = birds[selectedIndex]
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = activeBird.color.copy(alpha = 0.14f)),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(14.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(text = activeBird.emoji, fontSize = 54.sp)
                        Column {
                            Text(
                                text = activeBird.name,
                                fontSize = 22.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = activeBird.color
                            )
                            Text(
                                text = "Song: \"${activeBird.chirpText}\"",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF37474F)
                            )
                            Text(
                                text = activeBird.funFact,
                                fontSize = 13.sp,
                                color = Color(0xFF546E7A)
                            )
                        }
                    }

                    IconButton(
                        onClick = {
                            MelodySoundEngine.playSweetBirdChirp()
                            onSpeak("${activeBird.name}! ${activeBird.chirpText}! ${activeBird.funFact}")
                        },
                        modifier = Modifier
                            .size(46.dp)
                            .background(activeBird.color, CircleShape)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Rounded.VolumeUp,
                            contentDescription = "Sing",
                            tint = Color.White
                        )
                    }
                }
            }

            // Grid of Birds
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentPadding = PaddingValues(bottom = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                itemsIndexed(birds) { index, bird ->
                    val isSelected = selectedIndex == index
                    val scale by animateFloatAsState(
                        targetValue = if (isSelected) 1.04f else 1.0f,
                        animationSpec = spring(),
                        label = "bird_scale"
                    )

                    Card(
                        modifier = Modifier
                            .scale(scale)
                            .fillMaxWidth()
                            .clickable {
                                selectedIndex = index
                                MelodySoundEngine.playSweetBirdChirp()
                                onSpeak("${bird.name}! ${bird.chirpText}!")
                            },
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (isSelected) Color.White else Color(0xFFF9FDF9)
                        ),
                        border = androidx.compose.foundation.BorderStroke(
                            width = if (isSelected) 2.5.dp else 1.dp,
                            color = if (isSelected) bird.color else Color(0xFFE2EFE2)
                        ),
                        elevation = CardDefaults.cardElevation(defaultElevation = if (isSelected) 6.dp else 2.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(text = bird.emoji, fontSize = 46.sp)
                            Text(
                                text = bird.name,
                                fontSize = 17.sp,
                                fontWeight = FontWeight.Bold,
                                color = bird.color,
                                textAlign = TextAlign.Center
                            )
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(bird.color.copy(alpha = 0.12f))
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = "♪ ${bird.chirpText}",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = bird.color
                                )
                            }
                        }
                    }
                }
            }
        }

        ConfettiEffect(trigger = confettiTrigger)
    }
}
