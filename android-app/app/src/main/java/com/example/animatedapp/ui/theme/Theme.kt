package com.example.animatedapp.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// Luxury Palette
val Obsidian = Color(0xFF0A0A0A)
val DeepCharcoal = Color(0xFF1A1A1A)
val GoldPremium = Color(0xFFD4AF37)
val GoldLight = Color(0xFFF9E29C)
val GlassDark = Color(0xAA121212)
val TextGold = Color(0xFFE5C100)
val TextSilver = Color(0xFFB2BEC3)

private val LuxuryColorScheme = darkColorScheme(
    primary = GoldPremium,
    secondary = GoldLight,
    tertiary = Color(0xFF74B9FF),
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
