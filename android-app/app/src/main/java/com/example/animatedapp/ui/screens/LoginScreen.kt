package com.example.animatedapp.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
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
import kotlin.math.roundToInt

enum class AnimationState {
    WALKING, WAVING, DRAGGING
}

@Composable
fun LoginScreen() {
    val haptic = LocalHapticFeedback.current
    var animationState by remember { mutableStateOf(AnimationState.WALKING) }
    
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(
        com.example.animatedapp.R.raw.character_animation
    ))
    
    val progress by animateLottieCompositionAsState(
        composition = composition,
        iterations = if (animationState == AnimationState.WALKING) LottieConstants.IterateForever else 1
    )

    // Physics-based character movement
    val characterXOffset by animateDpAsState(
        targetValue = when (animationState) {
            AnimationState.WALKING -> (-200).dp
            AnimationState.WAVING -> 20.dp
            AnimationState.DRAGGING -> 300.dp
        },
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioLowBouncy,
            stiffness = Spring.StiffnessVeryLow
        ),
        label = "CharacterX"
    )

    // Physics-based Login Card movement (The "Drag")
    val loginCardOffset = remember { Animatable(1200f) }
    
    LaunchedEffect(animationState) {
        if (animationState == AnimationState.DRAGGING) {
            loginCardOffset.animateTo(
                targetValue = 0f,
                animationSpec = spring(
                    dampingRatio = 0.6f, // Slight wobble for "weight"
                    stiffness = Spring.StiffnessLow
                )
            )
        }
    }

    Box(modifier = Modifier.fillMaxSize().background(Obsidian)) {
        
        // Bespoke Parallax Background
        Canvas(modifier = Modifier.fillMaxSize()) {
            val canvasWidth = size.width
            val canvasHeight = size.height
            
            // Subtle organic shapes
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(GoldPremium.copy(alpha = 0.03f), Color.Transparent),
                    center = Offset(canvasWidth * 0.8f, canvasHeight * 0.2f),
                    radius = 600f
                ),
                radius = 600f,
                center = Offset(canvasWidth * 0.8f, canvasHeight * 0.2f)
            )
        }

        // The Signature Login Card
        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .offset { IntOffset(0, loginCardOffset.value.roundToInt()) }
                .padding(24.dp)
                .shadow(40.dp, RoundedCornerShape(48.dp), ambientColor = GoldPremium.copy(alpha = 0.5f))
                .clip(RoundedCornerShape(48.dp))
                .background(GlassDark)
                .border(0.5.dp, GoldPremium.copy(alpha = 0.4f), RoundedCornerShape(48.dp))
                .padding(horizontal = 32.dp, vertical = 48.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Welcome",
                style = MaterialTheme.typography.displaySmall.copy(
                    fontWeight = FontWeight.ExtraLight,
                    fontFamily = FontFamily.Serif,
                    color = GoldPremium,
                    letterSpacing = 4.sp
                )
            )
            Text(
                text = "TO THE EXCEPTIONAL",
                style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = GoldPremium.copy(alpha = 0.6f),
                    letterSpacing = 6.sp
                )
            )
            
            Spacer(modifier = Modifier.height(56.dp))
            
            SignatureTextField(
                label = "EMAIL",
                icon = Icons.Default.Email
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            SignatureTextField(
                label = "PASSWORD",
                icon = Icons.Default.Lock,
                isPassword = true
            )
            
            Spacer(modifier = Modifier.height(64.dp))
            
            // Bespoke Button with Shimmer
            Button(
                onClick = {},
                modifier = Modifier
                    .fillMaxWidth()
                    .height(72.dp)
                    .border(1.dp, GoldPremium, RoundedCornerShape(24.dp)),
                shape = RoundedCornerShape(24.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent,
                    contentColor = GoldPremium
                )
            ) {
                Text(
                    "ENTER",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Light,
                    letterSpacing = 8.sp
                )
            }
            
            Spacer(modifier = Modifier.height(40.dp))
            
            Text(
                text = "FORGOT CREDENTIALS?",
                style = MaterialTheme.typography.labelSmall.copy(
                    color = TextSilver.copy(alpha = 0.5f),
                    letterSpacing = 2.sp
                )
            )
        }

        // The Character (Lottie) - Positioned to "pull" the card
        LottieAnimation(
            composition = composition,
            progress = { progress },
            modifier = Modifier
                .size(350.dp)
                .align(Alignment.BottomStart)
                .offset(x = characterXOffset, y = 40.dp)
        )
    }

    LaunchedEffect(Unit) {
        kotlinx.coroutines.delay(1000)
        animationState = AnimationState.WAVING
        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
        
        kotlinx.coroutines.delay(2500)
        animationState = AnimationState.DRAGGING
        // Heavy haptic for the "drag" start
        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignatureTextField(
    label: String,
    icon: ImageVector,
    isPassword: Boolean = false
) {
    var value by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall.copy(
                color = GoldPremium.copy(alpha = 0.5f),
                letterSpacing = 3.sp,
                fontWeight = FontWeight.Bold
            ),
            modifier = Modifier.padding(start = 8.dp, bottom = 8.dp)
        )
        OutlinedTextField(
            value = value,
            onValueChange = { value = it },
            leadingIcon = { Icon(icon, contentDescription = null, tint = GoldPremium, modifier = Modifier.size(18.dp)) },
            visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = GoldPremium,
                unfocusedBorderColor = GoldPremium.copy(alpha = 0.1f),
                cursorColor = GoldPremium,
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White
            ),
            singleLine = true
        )
    }
}
