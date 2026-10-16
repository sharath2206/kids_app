package com.example.myapplication.model

import androidx.compose.ui.graphics.Color

data class AlphabetItem(
    val letter: Char,
    val word: String,
    val emoji: String,
    val color: Color,
    val phonicSound: String,
    val phonicDisplay: String,
    val phonicExplanation: String
)

val AlphabetList = listOf(
    AlphabetItem('A', "Apple", "🍎", Color(0xFFE53935), "Ah", "/æ/", "A says ah... ah... Apple"),
    AlphabetItem('B', "Ball", "⚽", Color(0xFF1E88E5), "Buh", "/b/", "B says buh... buh... Ball"),
    AlphabetItem('C', "Cat", "🐱", Color(0xFFFB8C00), "Kuh", "/k/", "C says kuh... kuh... Cat"),
    AlphabetItem('D', "Dog", "🐶", Color(0xFF43A047), "Duh", "/d/", "D says duh... duh... Dog"),
    AlphabetItem('E', "Elephant", "🐘", Color(0xFF8E24AA), "Eh", "/e/", "E says eh... eh... Elephant"),
    AlphabetItem('F', "Fish", "🐟", Color(0xFF00ACC1), "Fff", "/f/", "F says fff... fff... Fish"),
    AlphabetItem('G', "Grapes", "🍇", Color(0xFF7CB342), "Guh", "/g/", "G says guh... guh... Grapes"),
    AlphabetItem('H', "Hat", "🎩", Color(0xFFD81B60), "Huh", "/h/", "H says huh... huh... Hat"),
    AlphabetItem('I', "Igloo", "🍦", Color(0xFFF4511E), "Ih", "/ɪ/", "I says ih... ih... Igloo"),
    AlphabetItem('J', "Juice", "🧃", Color(0xFFFFB300), "Juh", "/dʒ/", "J says juh... juh... Juice"),
    AlphabetItem('K', "Kite", "🪁", Color(0xFF3949AB), "Kuh", "/k/", "K says kuh... kuh... Kite"),
    AlphabetItem('L', "Lion", "🦁", Color(0xFFF57C00), "Lll", "/l/", "L says lll... lll... Lion"),
    AlphabetItem('M', "Monkey", "🐵", Color(0xFF6D4C41), "Mmm", "/m/", "M says mmm... mmm... Monkey"),
    AlphabetItem('N', "Nest", "🪺", Color(0xFF00897B), "Nnn", "/n/", "N says nnn... nnn... Nest"),
    AlphabetItem('O', "Orange", "🍊", Color(0xFFFF9800), "Aw", "/ɒ/", "O says aw... aw... Orange"),
    AlphabetItem('P', "Penguin", "🐧", Color(0xFF1E88E5), "Puh", "/p/", "P says puh... puh... Penguin"),
    AlphabetItem('Q', "Queen", "👑", Color(0xFF9C27B0), "Kwuh", "/kw/", "Q says kwuh... kwuh... Queen"),
    AlphabetItem('R', "Rainbow", "🌈", Color(0xFFE91E63), "Rrr", "/r/", "R says rrr... rrr... Rainbow"),
    AlphabetItem('S', "Sun", "☀️", Color(0xFFFFC107), "Sss", "/s/", "S says sss... sss... Sun"),
    AlphabetItem('T', "Tiger", "🐯", Color(0xFFFF5722), "Tuh", "/t/", "T says tuh... tuh... Tiger"),
    AlphabetItem('U', "Umbrella", "☂️", Color(0xFF5E35B1), "Uh", "/ʌ/", "U says uh... uh... Umbrella"),
    AlphabetItem('V', "Violin", "🎻", Color(0xFF8D6E63), "Vvv", "/v/", "V says vvv... vvv... Violin"),
    AlphabetItem('W', "Whale", "🐋", Color(0xFF0288D1), "Wuh", "/w/", "W says wuh... wuh... Whale"),
    AlphabetItem('X', "Xylophone", "🎼", Color(0xFF009688), "Ks", "/ks/", "X says ks... ks... Xylophone"),
    AlphabetItem('Y', "Yacht", "⛵", Color(0xFF3F51B5), "Yuh", "/j/", "Y says yuh... yuh... Yacht"),
    AlphabetItem('Z', "Zebra", "🦓", Color(0xFF424242), "Zzz", "/z/", "Z says zzz... zzz... Zebra")
)
