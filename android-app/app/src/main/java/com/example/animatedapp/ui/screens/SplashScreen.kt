package com.example.animatedapp.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.animatedapp.ui.theme.GoldPremium
import com.example.animatedapp.ui.theme.Obsidian
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(onTimeout: () -> Unit) {
    val scale = remember { Animatable(0.7f) }
    val alpha = remember { Animatable(0f) }
    val rotation = remember { Animatable(0f) }
    
    val infiniteTransition = rememberInfiniteTransition(label = "shimmer")
    val shimmerOffset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(3000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "shimmerOffset"
    )

    LaunchedEffect(Unit) {
        // Sophisticated entry
        alpha.animateTo(1f, tween(2000, easing = EaseInOutCubic))
        scale.animateTo(1f, spring(dampingRatio = Spring.DampingRatioLowBouncy, stiffness = Spring.StiffnessVeryLow))
        rotation.animateTo(360f, tween(3000, easing = EaseOutQuart))
        delay(1500)
        alpha.animateTo(0f, tween(1200, easing = EaseInCubic))
        onTimeout()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Obsidian),
        contentAlignment = Alignment.Center
    ) {
        // Bespoke Background Canvas
        Canvas(modifier = Modifier.fillMaxSize()) {
            val center = Offset(size.width / 2, size.height / 2)
            rotate(rotation.value) {
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(GoldPremium.copy(alpha = 0.05f), Color.Transparent),
                        center = center,
                        radius = size.minDimension / 1.5f
                    ),
                    radius = size.minDimension / 1.5f,
                    center = center
                )
            }
        }

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            // Signature Logo
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .scale(scale.value)
                    .alpha(alpha.value),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "S",
                    fontSize = 84.sp,
                    color = GoldPremium,
                    fontWeight = FontWeight.Thin,
                    fontFamily = FontFamily.Serif,
                    modifier = Modifier.offset(y = (-4).dp)
                )
                
                // Shimmering Border
                Canvas(modifier = Modifier.size(100.dp)) {
                    drawCircle(
                        color = GoldPremium,
                        radius = size.minDimension / 2,
                        style = androidx.compose.ui.graphics.drawscope.Stroke(width = 1f)
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(40.dp))
            
            Text(
                text = "SEEKING",
                modifier = Modifier.alpha(alpha.value),
                style = MaterialTheme.typography.displaySmall.copy(
                    color = GoldPremium,
                    fontWeight = FontWeight.ExtraLight,
                    letterSpacing = 16.sp,
                    fontFamily = FontFamily.Serif
                )
            )
            
            Text(
                text = "EST. 2006",
                modifier = Modifier.alpha(alpha.value * 0.5f).padding(top = 8.dp),
                style = MaterialTheme.typography.labelSmall.copy(
                    color = GoldPremium,
                    letterSpacing = 4.sp,
                    fontFamily = FontFamily.Monospace
                )
            )
        }
    }
}
