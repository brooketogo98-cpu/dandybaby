package com.example.animatedapp.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// Bespoke Luxury Palette
val Obsidian = Color(0xFF050505)
val DeepCharcoal = Color(0xFF0F0F0F)
val GoldPremium = Color(0xFFC5A059) // More muted, sophisticated gold
val GoldAccent = Color(0xFFE8D5B5)
val GlassDark = Color(0xCC0A0A0A)
val TextGold = Color(0xFFD4AF37)
val TextSilver = Color(0xFF8E8E93)

private val LuxuryColorScheme = darkColorScheme(
    primary = GoldPremium,
    secondary = GoldAccent,
    tertiary = Color(0xFF2C3E50),
    background = Obsidian,
    surface = DeepCharcoal,
    onPrimary = Obsidian,
    onSecondary = Obsidian,
    onBackground = TextSilver,
    onSurface = TextGold
)

@Composable
fun AnimatedAppTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = LuxuryColorScheme,
        content = content
    )
}
