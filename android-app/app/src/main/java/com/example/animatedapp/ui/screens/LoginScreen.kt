package com.example.animatedapp.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.lottie.compose.*
import com.example.animatedapp.ui.theme.*
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

enum class AppAnimationState {
    WALKING, WAVING, DRAGGING
}

@Composable
fun LoginScreen() {
    val haptic = LocalHapticFeedback.current
    val scope = rememberCoroutineScope()
    var animationState by remember { mutableStateOf(AppAnimationState.WALKING) }
    
    // Physics-based tilt for the "Tinder" feel
    var tiltX by remember { mutableStateOf(0f) }
    var tiltY by remember { mutableStateOf(0f) }
    val animatedTiltX by animateFloatAsState(tiltX, spring(Spring.DampingRatioMediumBouncy, Spring.StiffnessLow))
    val animatedTiltY by animateFloatAsState(tiltY, spring(Spring.DampingRatioMediumBouncy, Spring.StiffnessLow))

    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(
        com.example.animatedapp.R.raw.character_animation
    ))
    
    val progress by animateLottieCompositionAsState(
        composition = composition,
        iterations = if (animationState == AppAnimationState.WALKING) LottieConstants.IterateForever else 1
    )

    // Character horizontal movement with "Angry Birds" squash/stretch logic
    val characterXOffset by animateDpAsState(
        targetValue = when (animationState) {
            AppAnimationState.WALKING -> (-200).dp
            AppAnimationState.WAVING -> 20.dp
            AppAnimationState.DRAGGING -> 320.dp
        },
        animationSpec = spring(Spring.DampingRatioLowBouncy, Spring.StiffnessVeryLow),
        label = "CharacterX"
    )

    // Physics-based Login Card movement
    val loginCardOffset = remember { Animatable(1500f) }
    val cardScale = remember { Animatable(0.9f) }
    
    LaunchedEffect(animationState) {
        if (animationState == AppAnimationState.DRAGGING) {
            launch {
                loginCardOffset.animateTo(0f, spring(0.5f, Spring.StiffnessLow))
                haptic.performHapticFeedback(HapticFeedbackType.LongPress) // "Lock" feel
            }
            launch {
                cardScale.animateTo(1f, spring(Spring.DampingRatioHighBouncy, Spring.StiffnessMedium))
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Obsidian)
            .pointerInput(Unit) {
                detectDragGestures(
                    onDrag = { change, dragAmount ->
                        tiltX = (dragAmount.x / 50f).coerceIn(-10f, 10f)
                        tiltY = (dragAmount.y / 50f).coerceIn(-10f, 10f)
                    },
                    onDragEnd = { tiltX = 0f; tiltY = 0f }
                )
            }
    ) {
        // Immersive Parallax Background
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(GoldPremium.copy(alpha = 0.05f), Color.Transparent),
                    center = Offset(size.width * 0.5f, size.height * 0.5f),
                    radius = size.minDimension
                ),
                radius = size.minDimension,
                center = Offset(size.width * 0.5f, size.height * 0.5f)
            )
        }

        // The Masterpiece Login Card
        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .offset { IntOffset(0, loginCardOffset.value.roundToInt()) }
                .graphicsLayer {
                    rotationX = -animatedTiltY
                    rotationY = animatedTiltX
                    scaleX = cardScale.value
                    scaleY = cardScale.value
                    cameraDistance = 12f * density
                }
                .padding(24.dp)
                .shadow(50.dp, RoundedCornerShape(56.dp), ambientColor = GoldPremium, spotColor = GoldPremium)
                .clip(RoundedCornerShape(56.dp))
                .background(GlassDark)
                .border(0.5.dp, GoldPremium.copy(alpha = 0.5f), RoundedCornerShape(56.dp))
                .padding(horizontal = 40.dp, vertical = 64.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "EXCEPTIONAL",
                style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = FontWeight.Black,
                    color = GoldPremium.copy(alpha = 0.4f),
                    letterSpacing = 8.sp
                )
            )
            Text(
                text = "Seeking",
                style = MaterialTheme.typography.displayMedium.copy(
                    fontWeight = FontWeight.Thin,
                    fontFamily = FontFamily.Serif,
                    color = GoldPremium,
                    letterSpacing = 2.sp
                )
            )
            
            Spacer(modifier = Modifier.height(64.dp))
            
            MasterpieceTextField(label = "IDENTITY", icon = Icons.Default.Person)
            Spacer(modifier = Modifier.height(24.dp))
            MasterpieceTextField(label = "ACCESS KEY", icon = Icons.Default.VpnKey, isPassword = true)
            
            Spacer(modifier = Modifier.height(72.dp))
            
            // Juicy Button
            Button(
                onClick = { haptic.performHapticFeedback(HapticFeedbackType.LongPress) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp)
                    .graphicsLayer { shadowElevation = 20f },
                shape = RoundedCornerShape(28.dp),
                colors = ButtonDefaults.buttonColors(containerColor = GoldPremium, contentColor = Obsidian)
            ) {
                Text("ASCEND", fontSize = 16.sp, fontWeight = FontWeight.Bold, letterSpacing = 10.sp)
            }
        }

        // The Character (Lottie) - Reactive positioning
        LottieAnimation(
            composition = composition,
            progress = { progress },
            modifier = Modifier
                .size(380.dp)
                .align(Alignment.BottomStart)
                .offset(x = characterXOffset, y = 60.dp)
                .graphicsLayer {
                    // Subtle "breathing" scale
                    val s = 1f + (progress * 0.02f)
                    scaleX = s
                    scaleY = s
                }
        )
    }

    LaunchedEffect(Unit) {
        kotlinx.coroutines.delay(1200)
        animationState = AppAnimationState.WAVING
        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
        
        kotlinx.coroutines.delay(2800)
        animationState = AppAnimationState.DRAGGING
        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MasterpieceTextField(label: String, icon: ImageVector, isPassword: Boolean = false) {
    var value by remember { mutableStateOf("") }
    OutlinedTextField(
        value = value,
        onValueChange = { value = it },
        label = { Text(label, fontSize = 10.sp, letterSpacing = 4.sp, fontWeight = FontWeight.Bold) },
        leadingIcon = { Icon(icon, null, tint = GoldPremium, modifier = Modifier.size(20.dp)) },
        visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = GoldPremium,
            unfocusedBorderColor = GoldPremium.copy(alpha = 0.15f),
            focusedLabelColor = GoldPremium,
            cursorColor = GoldPremium,
            focusedTextColor = Color.White
        )
    )
}
