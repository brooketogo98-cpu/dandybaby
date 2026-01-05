package com.example.animatedapp.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
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
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
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

    // Character horizontal movement
    val characterXOffset by animateDpAsState(
        targetValue = when (animationState) {
            AnimationState.WALKING -> (-150).dp
            AnimationState.WAVING -> 40.dp
            AnimationState.DRAGGING -> 280.dp
        },
        animationSpec = tween(durationMillis = 3000, easing = LinearOutSlowInEasing),
        label = "CharacterX"
    )

    // Login Card vertical movement (dragged in)
    val loginCardOffset by animateDpAsState(
        targetValue = if (animationState == AnimationState.DRAGGING) 0.dp else 1000.dp,
        animationSpec = tween(durationMillis = 2000, easing = FastOutSlowInEasing),
        label = "LoginCard"
    )

    Box(modifier = Modifier.fillMaxSize().background(Obsidian)) {
        
        // Luxury Background Gradient
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Obsidian, DeepCharcoal)
                    )
                )
        )

        // Decorative Gold Glow
        Box(
            modifier = Modifier
                .size(400.dp)
                .align(Alignment.TopEnd)
                .offset(x = 100.dp, y = (-100).dp)
                .background(GoldPremium.copy(alpha = 0.05f), RoundedCornerShape(200.dp))
                .blur(100.dp)
        )

        // The Luxury Login Card
        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .offset { IntOffset(0, loginCardOffset.value.roundToInt()) }
                .padding(24.dp)
                .shadow(30.dp, RoundedCornerShape(40.dp), ambientColor = GoldPremium, spotColor = GoldPremium)
                .clip(RoundedCornerShape(40.dp))
                .background(GlassDark)
                .border(1.dp, GoldPremium.copy(alpha = 0.3f), RoundedCornerShape(40.dp))
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Welcome to Seeking",
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Serif,
                    color = GoldPremium,
                    letterSpacing = 2.sp
                )
            )
            Text(
                text = "Elevate your connections",
                style = MaterialTheme.typography.bodyMedium,
                color = TextSilver.copy(alpha = 0.7f)
            )
            
            Spacer(modifier = Modifier.height(48.dp))
            
            LuxuryTextField(
                label = "Email Address",
                icon = Icons.Default.Email
            )
            
            Spacer(modifier = Modifier.height(20.dp))
            
            LuxuryTextField(
                label = "Password",
                icon = Icons.Default.Lock,
                isPassword = true
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Text(
                text = "Forgot Password?",
                modifier = Modifier.align(Alignment.End),
                style = MaterialTheme.typography.labelLarge,
                color = GoldLight
            )
            
            Spacer(modifier = Modifier.height(48.dp))
            
            Button(
                onClick = {},
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp),
                shape = RoundedCornerShape(20.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = GoldPremium,
                    contentColor = Obsidian
                ),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 8.dp)
            ) {
                Text("SIGN IN", fontSize = 18.sp, fontWeight = FontWeight.Bold, letterSpacing = 4.sp)
            }
            
            Spacer(modifier = Modifier.height(32.dp))
            
            Row {
                Text("New to Seeking? ", color = TextSilver)
                Text("Join Now", color = GoldLight, fontWeight = FontWeight.Bold)
            }
        }

        // The Character (Lottie)
        LottieAnimation(
            composition = composition,
            progress = { progress },
            modifier = Modifier
                .size(300.dp)
                .align(Alignment.BottomStart)
                .offset(x = characterXOffset, y = 20.dp)
        )
    }

    LaunchedEffect(Unit) {
        kotlinx.coroutines.delay(1500)
        animationState = AnimationState.WAVING
        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
        
        kotlinx.coroutines.delay(3000)
        animationState = AnimationState.DRAGGING
        haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LuxuryTextField(
    label: String,
    icon: ImageVector,
    isPassword: Boolean = false
) {
    var value by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    OutlinedTextField(
        value = value,
        onValueChange = { value = it },
        label = { Text(label, color = TextSilver.copy(alpha = 0.5f)) },
        leadingIcon = { Icon(icon, contentDescription = null, tint = GoldPremium) },
        trailingIcon = {
            if (isPassword) {
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(
                        imageVector = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                        contentDescription = null,
                        tint = GoldPremium.copy(alpha = 0.5f)
                    )
                }
            }
        },
        visualTransformation = if (isPassword && !passwordVisible) PasswordVisualTransformation() else VisualTransformation.None,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = GoldPremium,
            unfocusedBorderColor = GoldPremium.copy(alpha = 0.2f),
            focusedLabelColor = GoldPremium,
            cursorColor = GoldPremium,
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.White
        )
    )
}
