package com.example.myapplication.model

import androidx.compose.ui.graphics.Color

/**
 * Kid Age Groups with tailored learning paths.
 */
enum class AgeGroup(
    val title: String,
    val ageRange: String,
    val subtitle: String,
    val icon: String,
    val primaryColor: Color,
    val gradientColors: List<Color>
) {
    TODDLER(
        title = "Toddlers",
        ageRange = "1 - 3 yrs",
        subtitle = "Big buttons, gentle sounds & cheerful shapes",
        icon = "👶",
        primaryColor = Color(0xFFFF8A65),
        gradientColors = listOf(Color(0xFFFFCC80), Color(0xFFFF8A65))
    ),
    PRESCHOOL(
        title = "Preschoolers",
        ageRange = "3 - 5 yrs",
        subtitle = "Phonics tracing, first words & animal sounds",
        icon = "🧒",
        primaryColor = Color(0xFF26A69A),
        gradientColors = listOf(Color(0xFF80CBC4), Color(0xFF00897B))
    ),
    KINDERGARTEN(
        title = "Early Learners",
        ageRange = "5 - 7+ yrs",
        subtitle = "Spelling station, wild safari, birds & math fun",
        icon = "👧",
        primaryColor = Color(0xFFAB47BC),
        gradientColors = listOf(Color(0xFFCE93D8), Color(0xFF7B1FA2))
    )
}

/**
 * Category Types available across the app.
 */
enum class CategoryType {
    ALPHABETS,
    WORDS,
    ANIMALS,
    BIRDS,
    NUMBERS,
    COLORS_SHAPES,
    MATH_FUN
}

/**
 * Learning Window Tile displayed on the Landing Page.
 */
data class CategoryTile(
    val type: CategoryType,
    val title: String,
    val subtitle: String,
    val emoji: String,
    val cardColor: Color,
    val badge: String
)

/**
 * Provides dynamically tailored learning tiles based on selected age group.
 */
fun getCategoriesForAge(ageGroup: AgeGroup): List<CategoryTile> {
    return when (ageGroup) {
        AgeGroup.TODDLER -> listOf(
            CategoryTile(
                type = CategoryType.ALPHABETS,
                title = "Alphabets & Phonics",
                subtitle = "Touch & hear letter magic",
                emoji = "🔤",
                cardColor = Color(0xFFE53935),
                badge = "A - Z Tracing"
            ),
            CategoryTile(
                type = CategoryType.ANIMALS,
                title = "Cute Animals",
                subtitle = "Soft cuddly sounds & friends",
                emoji = "🐶",
                cardColor = Color(0xFFFB8C00),
                badge = "Puppy & Friends"
            ),
            CategoryTile(
                type = CategoryType.BIRDS,
                title = "Chirpy Birds",
                subtitle = "Sweet baby bird chirps",
                emoji = "🐦",
                cardColor = Color(0xFF00ACC1),
                badge = "Sweet Chirps"
            ),
            CategoryTile(
                type = CategoryType.NUMBERS,
                title = "Numbers 1 - 5",
                subtitle = "Count bright stars & bubbles",
                emoji = "🔢",
                cardColor = Color(0xFF43A047),
                badge = "First Count"
            ),
            CategoryTile(
                type = CategoryType.WORDS,
                title = "Baby Words",
                subtitle = "Mama, Dada, Ball & Cat",
                emoji = "🎈",
                cardColor = Color(0xFF8E24AA),
                badge = "First Words"
            )
        )
        AgeGroup.PRESCHOOL -> listOf(
            CategoryTile(
                type = CategoryType.ALPHABETS,
                title = "Alphabet Scratch Pad",
                subtitle = "Trace letters with soft phonics",
                emoji = "🔤",
                cardColor = Color(0xFFE53935),
                badge = "A - Z & Tracing"
            ),
            CategoryTile(
                type = CategoryType.WORDS,
                title = "Phonics Words",
                subtitle = "Spell & blend 3-letter words",
                emoji = "📖",
                cardColor = Color(0xFF3949AB),
                badge = "C-A-T Cat!"
            ),
            CategoryTile(
                type = CategoryType.ANIMALS,
                title = "Animal Kingdom",
                subtitle = "Roars, trumpets & friendly calls",
                emoji = "🦁",
                cardColor = Color(0xFFFB8C00),
                badge = "Wild & Farm"
            ),
            CategoryTile(
                type = CategoryType.BIRDS,
                title = "Singing Birds",
                subtitle = "Sweet melodies & bright feathers",
                emoji = "🦜",
                cardColor = Color(0xFF00897B),
                badge = "Melodious Calls"
            ),
            CategoryTile(
                type = CategoryType.NUMBERS,
                title = "Numbers 1 - 10",
                subtitle = "Count, hear & trace 1 to 10",
                emoji = "🔢",
                cardColor = Color(0xFF5E35B1),
                badge = "Counting Fun"
            )
        )
        AgeGroup.KINDERGARTEN -> listOf(
            CategoryTile(
                type = CategoryType.ALPHABETS,
                title = "Alphabet Mastery",
                subtitle = "Upper & lower case letter drawing",
                emoji = "🔤",
                cardColor = Color(0xFFE53935),
                badge = "Full A - Z Pad"
            ),
            CategoryTile(
                type = CategoryType.WORDS,
                title = "Word Builder",
                subtitle = "Phonics spelling & sight words",
                emoji = "📚",
                cardColor = Color(0xFF1E88E5),
                badge = "Spelling Studio"
            ),
            CategoryTile(
                type = CategoryType.ANIMALS,
                title = "Safari Wildlife",
                subtitle = "Discover facts, habitats & sounds",
                emoji = "🦒",
                cardColor = Color(0xFFF57C00),
                badge = "Animal Facts"
            ),
            CategoryTile(
                type = CategoryType.BIRDS,
                title = "Birds of the World",
                subtitle = "Graceful flight & bird songs",
                emoji = "🦅",
                cardColor = Color(0xFF00ACC1),
                badge = "Aviary Wonders"
            ),
            CategoryTile(
                type = CategoryType.NUMBERS,
                title = "Numbers & Math Fun",
                subtitle = "Count to 20 & joyful additions",
                emoji = "➕",
                cardColor = Color(0xFF43A047),
                badge = "1 - 20 & Add"
            )
        )
    }
}

/**
 * Words data model for Phonics spelling station.
 */
data class WordItem(
    val word: String,
    val phonicsList: List<String>,
    val emoji: String,
    val explanation: String,
    val color: Color
)

fun getWordsForAge(ageGroup: AgeGroup): List<WordItem> {
    return when (ageGroup) {
        AgeGroup.TODDLER -> listOf(
            WordItem("BALL", listOf("Buh", "Aw", "Lll"), "⚽", "B-A-L-L says Ball! Bouncy Ball!", Color(0xFF1E88E5)),
            WordItem("CAT", listOf("Kuh", "Ah", "Tuh"), "🐱", "C-A-T says Cat! Cute kitty cat!", Color(0xFFFB8C00)),
            WordItem("SUN", listOf("Sss", "Uh", "Nnn"), "☀️", "S-U-N says Sun! Bright sunny day!", Color(0xFFFFB300)),
            WordItem("DOG", listOf("Duh", "Aw", "Guh"), "🐶", "D-O-G says Dog! Friendly doggy!", Color(0xFF43A047)),
            WordItem("BED", listOf("Buh", "Eh", "Duh"), "🛏️", "B-E-D says Bed! Soft sleepy bed!", Color(0xFF8E24AA)),
            WordItem("HAT", listOf("Huh", "Ah", "Tuh"), "🎩", "H-A-T says Hat! Magic party hat!", Color(0xFFD81B60))
        )
        AgeGroup.PRESCHOOL -> listOf(
            WordItem("CAT", listOf("Kuh", "Ah", "Tuh"), "🐱", "C-A-T spells Cat. Cat says meow!", Color(0xFFFB8C00)),
            WordItem("FISH", listOf("Fff", "Ih", "Shh"), "🐟", "F-I-S-H spells Fish. Swimming in the sea!", Color(0xFF00ACC1)),
            WordItem("STAR", listOf("Sss", "Tuh", "Ah", "Rrr"), "⭐", "S-T-A-R spells Star. Twinkling in the sky!", Color(0xFFFFB300)),
            WordItem("PIG", listOf("Puh", "Ih", "Guh"), "🐷", "P-I-G spells Pig. Oink oink pig!", Color(0xFFE91E63)),
            WordItem("DUCK", listOf("Duh", "Uh", "Kuh"), "🦆", "D-U-C-K spells Duck. Quack quack duck!", Color(0xFF43A047)),
            WordItem("MOON", listOf("Mmm", "Ooo", "Nnn"), "🌙", "M-O-O-N spells Moon. Glowing peaceful moon!", Color(0xFF3F51B5)),
            WordItem("TREE", listOf("Tuh", "Rrr", "Eee"), "🌳", "T-R-E-E spells Tree. Tall green tree!", Color(0xFF2E7D32))
        )
        AgeGroup.KINDERGARTEN -> listOf(
            WordItem("TRAIN", listOf("Tuh", "Rrr", "Ay", "Nnn"), "🚂", "T-R-A-I-N spells Train. Choo choo train!", Color(0xFFE53935)),
            WordItem("ROCKET", listOf("Rrr", "Aw", "Kuh", "Eh", "Tuh"), "🚀", "R-O-C-K-E-T spells Rocket. Zoom to the stars!", Color(0xFF7B1FA2)),
            WordItem("FLOWER", listOf("Fff", "Lll", "Ow", "Er"), "🌸", "F-L-O-W-E-R spells Flower. Sweet blooming petals!", Color(0xFFE91E63)),
            WordItem("MONKEY", listOf("Mmm", "Uh", "Nnn", "Kuh", "Eee"), "🐵", "M-O-N-K-E-Y spells Monkey. Swinging from branches!", Color(0xFF6D4C41)),
            WordItem("RAINBOW", listOf("Rrr", "Ay", "Nnn", "Buh", "Oh"), "🌈", "R-A-I-N-B-O-W spells Rainbow. Beautiful colors in the sky!", Color(0xFF0288D1)),
            WordItem("SMILE", listOf("Sss", "Mmm", "Eye", "Lll"), "😊", "S-M-I-L-E spells Smile. A happy cheerful smile!", Color(0xFFFFB300))
        )
    }
}

/**
 * Animal data model.
 */
data class AnimalItem(
    val name: String,
    val soundText: String,
    val emoji: String,
    val funFact: String,
    val color: Color
)

fun getAnimalsForAge(ageGroup: AgeGroup): List<AnimalItem> {
    return when (ageGroup) {
        AgeGroup.TODDLER -> listOf(
            AnimalItem("Puppy", "Woof! Woof!", "🐶", "Puppies love to wag their tails!", Color(0xFFFB8C00)),
            AnimalItem("Kitty", "Meow! Meow!", "🐱", "Kittens love warm gentle hugs!", Color(0xFFFF7043)),
            AnimalItem("Cow", "Moo! Moo!", "🐮", "Cows give yummy nutritious milk!", Color(0xFF5D4037)),
            AnimalItem("Duckling", "Quack! Quack!", "🦆", "Ducks love to paddle in water!", Color(0xFF43A047)),
            AnimalItem("Sheep", "Baa! Baa!", "🐑", "Sheep have super soft wool!", Color(0xFF546E7A)),
            AnimalItem("Piggy", "Oink! Oink!", "🐷", "Piggies love playful mud splashes!", Color(0xFFEC407A))
        )
        AgeGroup.PRESCHOOL -> listOf(
            AnimalItem("Lion", "Roaaar!", "🦁", "The lion is known as king of the jungle!", Color(0xFFFFA000)),
            AnimalItem("Elephant", "Pawoo!", "🐘", "Elephants have long trunks to drink water!", Color(0xFF78909C)),
            AnimalItem("Monkey", "Ooh-ooh aah-aah!", "🐵", "Monkeys love sweet bananas and climbing!", Color(0xFF8D6E63)),
            AnimalItem("Dolphin", "Click-whistle!", "🐬", "Dolphins are smart and love jumping waves!", Color(0xFF00ACC1)),
            AnimalItem("Bear", "Grrr-hum!", "🐻", "Bears love sweet golden honey!", Color(0xFF6D4C41)),
            AnimalItem("Zebra", "Neigh-whinny!", "🦓", "Every zebra has unique black and white stripes!", Color(0xFF424242))
        )
        AgeGroup.KINDERGARTEN -> listOf(
            AnimalItem("Giraffe", "Gentle hum", "🦒", "Giraffes are the tallest animals on Earth!", Color(0xFFFFA000)),
            AnimalItem("Cheetah", "Purr-chirp!", "🐆", "Cheetahs can sprint faster than race cars!", Color(0xFFFFB300)),
            AnimalItem("Kangaroo", "Chortle-click", "🦘", "Kangaroos have a cozy pouch for their joey!", Color(0xFFD84315)),
            AnimalItem("Panda", "Bleat-chirp", "🐼", "Pandas can spend 12 hours a day eating bamboo!", Color(0xFF37474F)),
            AnimalItem("Tiger", "Chuff-roar!", "🐯", "Tigers are great swimmers and have striped fur!", Color(0xFFFF5722)),
            AnimalItem("Koala", "Squeak-snore", "🐨", "Koalas love to sleep peacefully in eucalyptus trees!", Color(0xFF78909C))
        )
    }
}

/**
 * Bird data model.
 */
data class BirdItem(
    val name: String,
    val chirpText: String,
    val emoji: String,
    val funFact: String,
    val color: Color
)

fun getBirdsForAge(ageGroup: AgeGroup): List<BirdItem> {
    return when (ageGroup) {
        AgeGroup.TODDLER -> listOf(
            BirdItem("Little Chick", "Cheep cheep!", "🐥", "Baby chicks are fuzzy, yellow and cuddly!", Color(0xFFFFD54F)),
            BirdItem("Duck", "Quack quack!", "🦆", "Ducks have webbed feet to swim fast!", Color(0xFF4CAF50)),
            BirdItem("Owl", "Hoot hoot!", "🦉", "Wise owls can turn their heads all around!", Color(0xFF8D6E63)),
            BirdItem("Robin", "Tweet tweet!", "🐦", "Robins have bright cheerful red tummies!", Color(0xFFE53935))
        )
        AgeGroup.PRESCHOOL -> listOf(
            BirdItem("Parrot", "Squawk! Hello!", "🦜", "Parrots can learn words and talk to you!", Color(0xFF00C853)),
            BirdItem("Peacock", "May-awe!", "🦚", "Peacocks display dazzling rainbow feathers!", Color(0xFF0091EA)),
            BirdItem("Flamingo", "Honk honk!", "🦩", "Flamingos can balance gracefully on one leg!", Color(0xFFFF4081)),
            BirdItem("Penguin", "Honk-whistle!", "🐧", "Penguins cannot fly in air, but fly underwater!", Color(0xFF37474F)),
            BirdItem("Toucan", "Gggrrrk-croak!", "🐦‍⬛", "Toucans have giant, colorful, lightweight beaks!", Color(0xFFFF9800))
        )
        AgeGroup.KINDERGARTEN -> listOf(
            BirdItem("Hummingbird", "Sweet high hum", "🌺", "Hummingbirds can fly backwards and hover like magic!", Color(0xFF00B0FF)),
            BirdItem("Bald Eagle", "Screeech!", "🦅", "Eagles have super sharp eyesight to see from miles high!", Color(0xFF795548)),
            BirdItem("Swan", "Whoop-trumpet", "🦢", "Swans are graceful birds that glide smoothly across lakes!", Color(0xFF90A4AE)),
            BirdItem("Ostrich", "Boom-boom", "🪶", "The ostrich is the biggest bird and can run super fast!", Color(0xFF616161)),
            BirdItem("Woodpecker", "Tap-tap-tap!", "🪵", "Woodpeckers tap tree trunks to find tasty insects!", Color(0xFFE53935))
        )
    }
}

/**
 * Number item data model.
 */
data class NumberItem(
    val value: Int,
    val word: String,
    val emoji: String,
    val color: Color
)

fun getNumbersForAge(ageGroup: AgeGroup): List<NumberItem> {
    val count = when (ageGroup) {
        AgeGroup.TODDLER -> 5
        AgeGroup.PRESCHOOL -> 10
        AgeGroup.KINDERGARTEN -> 20
    }

    val numberWords = listOf(
        "One", "Two", "Three", "Four", "Five",
        "Six", "Seven", "Eight", "Nine", "Ten",
        "Eleven", "Twelve", "Thirteen", "Fourteen", "Fifteen",
        "Sixteen", "Seventeen", "Eighteen", "Nineteen", "Twenty"
    )

    val emojis = listOf(
        "🍎", "🎈", "⭐", "🚗", "🐶",
        "🍓", "🦋", "🍦", "🚀", "🌈",
        "⚽", "🐥", "🍭", "🌻", "🎨",
        "🦁", "🔔", "🎸", "⛵", "👑"
    )

    val colors = listOf(
        Color(0xFFE53935), Color(0xFFFB8C00), Color(0xFFFFB300), Color(0xFF43A047), Color(0xFF00ACC1),
        Color(0xFF1E88E5), Color(0xFF5E35B1), Color(0xFF8E24AA), Color(0xFFE91E63), Color(0xFF00897B),
        Color(0xFFD81B60), Color(0xFF3949AB), Color(0xFF0097A7), Color(0xFF7CB342), Color(0xFFFF8F00),
        Color(0xFF6D4C41), Color(0xFF0288D1), Color(0xFF9C27B0), Color(0xFFF4511E), Color(0xFF2E7D32)
    )

    return (1..count).map { num ->
        val idx = num - 1
        NumberItem(
            value = num,
            word = numberWords[idx],
            emoji = emojis[idx % emojis.size],
            color = colors[idx % colors.size]
        )
    }
}
