package com.example.animatedapp.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val PrimaryPurple = Color(0xFF6C63FF)
val SecondaryPink = Color(0xFFFF6584)
val BackgroundGradientStart = Color(0xFFF0F2F5)
val BackgroundGradientEnd = Color(0xFFFFFFFF)
val GlassWhite = Color(0xCCFFFFFF)
val TextPrimary = Color(0xFF2D3436)

private val LightColorScheme = lightColorScheme(
    primary = PrimaryPurple,
    secondary = SecondaryPink,
    tertiary = Color(0xFF00CEC9),
    background = BackgroundGradientStart,
    surface = GlassWhite,
    onPrimary = Color.White,
    onSecondary = Color.White,
    onBackground = TextPrimary,
    onSurface = TextPrimary
)

@Composable
fun AnimatedAppTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        content = content
    )
}
