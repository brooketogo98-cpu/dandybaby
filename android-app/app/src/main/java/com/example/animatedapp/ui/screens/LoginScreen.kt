package com.example.animatedapp.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
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

    val characterXOffset by animateDpAsState(
        targetValue = when (animationState) {
            AnimationState.WALKING -> (-100).dp
            AnimationState.WAVING -> 50.dp
            AnimationState.DRAGGING -> 250.dp
        },
        animationSpec = tween(durationMillis = 2500, easing = LinearOutSlowInEasing),
        label = "CharacterX"
    )

    val loginCardOffset by animateDpAsState(
        targetValue = if (animationState == AnimationState.DRAGGING) 0.dp else 800.dp,
        animationSpec = tween(durationMillis = 1800, easing = FastOutSlowInEasing),
        label = "LoginCard"
    )

    val backgroundBrush = Brush.verticalGradient(
        colors = listOf(BackgroundGradientStart, BackgroundGradientEnd)
    )

    Box(modifier = Modifier.fillMaxSize().background(backgroundBrush)) {
        
        // Decorative background elements
        Box(
            modifier = Modifier
                .size(300.dp)
                .offset(x = (-100).dp, y = (-50).dp)
                .background(PrimaryPurple.copy(alpha = 0.1f), RoundedCornerShape(150.dp))
                .blur(50.dp)
        )

        // The Login Card (Glassmorphism effect)
        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .offset { IntOffset(0, loginCardOffset.value.roundToInt()) }
                .padding(24.dp)
                .shadow(20.dp, RoundedCornerShape(32.dp))
                .clip(RoundedCornerShape(32.dp))
                .background(GlassWhite)
                .border(1.dp, Color.White.copy(alpha = 0.5f), RoundedCornerShape(32.dp))
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Welcome Back",
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp,
                    color = TextPrimary
                )
            )
            Text(
                text = "Sign in to continue your journey",
                style = MaterialTheme.typography.bodyMedium,
                color = TextPrimary.copy(alpha = 0.6f)
            )
            
            Spacer(modifier = Modifier.height(40.dp))
            
            CustomTextField(
                label = "Email Address",
                icon = Icons.Default.Email,
                keyboardType = KeyboardType.Email
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            CustomTextField(
                label = "Password",
                icon = Icons.Default.Lock,
                isPassword = true
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Text(
                text = "Forgot Password?",
                modifier = Modifier.align(Alignment.End),
                style = MaterialTheme.typography.labelLarge,
                color = PrimaryPurple
            )
            
            Spacer(modifier = Modifier.height(40.dp))
            
            Button(
                onClick = {},
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryPurple)
            ) {
                Text("Sign In", fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            Row {
                Text("Don't have an account? ", color = TextPrimary.copy(alpha = 0.6f))
                Text("Sign Up", color = SecondaryPink, fontWeight = FontWeight.Bold)
            }
        }

        // The Character (Lottie)
        LottieAnimation(
            composition = composition,
            progress = { progress },
            modifier = Modifier
                .size(250.dp)
                .align(Alignment.BottomStart)
                .offset(x = characterXOffset, y = (-20).dp)
        )
    }

    LaunchedEffect(Unit) {
        kotlinx.coroutines.delay(1000)
        animationState = AnimationState.WAVING
        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
        
        kotlinx.coroutines.delay(2500)
        animationState = AnimationState.DRAGGING
        haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomTextField(
    label: String,
    icon: ImageVector,
    isPassword: Boolean = false,
    keyboardType: KeyboardType = KeyboardType.Text
) {
    var value by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    OutlinedTextField(
        value = value,
        onValueChange = { value = it },
        label = { Text(label) },
        leadingIcon = { Icon(icon, contentDescription = null, tint = PrimaryPurple) },
        trailingIcon = {
            if (isPassword) {
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(
                        imageVector = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                        contentDescription = null
                    )
                }
            }
        },
        visualTransformation = if (isPassword && !passwordVisible) PasswordVisualTransformation() else VisualTransformation.None,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = PrimaryPurple,
            unfocusedBorderColor = Color.LightGray.copy(alpha = 0.5f),
            focusedLabelColor = PrimaryPurple
        )
    )
}

enum class AnimationState {
    WALKING, WAVING, DRAGGING
}
