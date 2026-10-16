package com.example.myapplication.ui.components

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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.automirrored.rounded.ArrowForward
import androidx.compose.material.icons.automirrored.rounded.VolumeUp
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.model.AlphabetItem
import com.example.myapplication.model.AlphabetList
import com.example.myapplication.model.LetterCaseMode

@Composable
fun TopAlphabetHeader(
    currentAlphabet: AlphabetItem,
    letterCaseMode: LetterCaseMode,
    onCaseModeToggle: () -> Unit,
    onSpeakClicked: () -> Unit,
    onCelebrateClicked: () -> Unit,
    onBackToHome: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = currentAlphabet.color.copy(alpha = 0.12f))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Letter + Word + Emoji + Speaker
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Home button if provided
                if (onBackToHome != null) {
                    IconButton(
                        onClick = onBackToHome,
                        modifier = Modifier
                            .size(36.dp)
                            .background(Color.White, CircleShape)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                            contentDescription = "Back to Home",
                            tint = Color(0xFF37474F),
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }

                // Emoji Icon
                Text(
                    text = currentAlphabet.emoji,
                    fontSize = 30.sp
                )

                Column {
                    Text(
                        text = "${currentAlphabet.letter} is for ${currentAlphabet.word}",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = currentAlphabet.color
                    )
                    Text(
                        text = "Sound: \"${currentAlphabet.phonicSound}\" ${currentAlphabet.phonicDisplay}",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF455A64)
                    )
                }

                // Speaker button
                IconButton(
                    onClick = onSpeakClicked,
                    modifier = Modifier
                        .size(38.dp)
                        .background(currentAlphabet.color, CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Rounded.VolumeUp,
                        contentDescription = "Pronounce Letter",
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            // Case mode toggle + Good Job celebration
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                // Case Toggle (A / a / Aa)
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = Color.White,
                    modifier = Modifier
                        .border(1.5.dp, currentAlphabet.color, RoundedCornerShape(12.dp))
                        .clickable { onCaseModeToggle() }
                ) {
                    Text(
                        text = when (letterCaseMode) {
                            LetterCaseMode.UPPERCASE -> "A"
                            LetterCaseMode.LOWERCASE -> "a"
                            LetterCaseMode.BOTH -> "Aa"
                        },
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = currentAlphabet.color
                    )
                }

                // Good Job celebration button
                ElevatedButton(
                    onClick = onCelebrateClicked,
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.elevatedButtonColors(
                        containerColor = Color(0xFFFFB300),
                        contentColor = Color.White
                    ),
                    contentPadding = androidx.compose.foundation.layout.PaddingValues(
                        horizontal = 10.dp,
                        vertical = 6.dp
                    )
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Star,
                        contentDescription = "Celebrate",
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(3.dp))
                    Text(
                        text = "Done!",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
fun AlphabetSelectorBar(
    currentIndex: Int,
    onAlphabetSelected: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val listState = rememberLazyListState()

    LaunchedEffect(currentIndex) {
        listState.animateScrollToItem(
            (currentIndex - 2).coerceAtLeast(0)
        )
    }

    LazyRow(
        state = listState,
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        itemsIndexed(AlphabetList) { index, item ->
            val isSelected = index == currentIndex
            Box(
                modifier = Modifier
                    .size(if (isSelected) 46.dp else 40.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(
                        if (isSelected) item.color else Color(0xFFF0F0F5)
                    )
                    .border(
                        width = if (isSelected) 2.dp else 0.dp,
                        color = if (isSelected) Color.White else Color.Transparent,
                        shape = RoundedCornerShape(14.dp)
                    )
                    .clickable { onAlphabetSelected(index) },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "${item.letter}",
                    fontSize = if (isSelected) 20.sp else 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isSelected) Color.White else Color(0xFF455A64)
                )
            }
        }
    }
}

@Composable
fun NavigationFooter(
    onPrevious: () -> Unit,
    onNext: () -> Unit,
    hasPrevious: Boolean,
    hasNext: Boolean,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        ElevatedButton(
            onClick = onPrevious,
            enabled = hasPrevious,
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.elevatedButtonColors(
                containerColor = Color(0xFF673AB7),
                contentColor = Color.White
            )
        ) {
            Icon(Icons.AutoMirrored.Rounded.ArrowBack, contentDescription = "Previous")
            Spacer(modifier = Modifier.width(6.dp))
            Text("Previous", fontSize = 14.sp, fontWeight = FontWeight.Bold)
        }

        ElevatedButton(
            onClick = onNext,
            enabled = hasNext,
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.elevatedButtonColors(
                containerColor = Color(0xFF4CAF50),
                contentColor = Color.White
            )
        ) {
            Text("Next", fontSize = 14.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.width(6.dp))
            Icon(Icons.AutoMirrored.Rounded.ArrowForward, contentDescription = "Next")
        }
    }
}
