package com.example.animatedapp.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.animatedapp.ui.theme.GoldPremium
import com.example.animatedapp.ui.theme.Obsidian
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(onTimeout: () -> Unit) {
    val scale = remember { Animatable(0.8f) }
    val alpha = remember { Animatable(0f) }
    
    LaunchedEffect(Unit) {
        // Elegant fade and scale
        alpha.animateTo(1f, tween(1500, easing = EaseInOutQuart))
        scale.animateTo(1f, spring(dampingRatio = Spring.DampingRatioLowBouncy, stiffness = Spring.StiffnessVeryLow))
        delay(1000)
        alpha.animateTo(0f, tween(1000))
        onTimeout()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Obsidian),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            // Luxury Logo Placeholder
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .scale(scale.value)
                    .alpha(alpha.value)
                    .background(
                        Brush.radialGradient(
                            colors = listOf(GoldPremium.copy(alpha = 0.3f), Color.Transparent)
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text("S", fontSize = 72.sp, color = GoldPremium, fontWeight = FontWeight.ExtraBold, fontFamily = FontFamily.Serif)
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            Text(
                text = "SEEKING",
                modifier = Modifier.alpha(alpha.value),
                style = MaterialTheme.typography.displaySmall.copy(
                    color = GoldPremium,
                    fontWeight = FontWeight.Light,
                    letterSpacing = 12.sp,
                    fontFamily = FontFamily.Serif
                )
            )
        }
    }
}
