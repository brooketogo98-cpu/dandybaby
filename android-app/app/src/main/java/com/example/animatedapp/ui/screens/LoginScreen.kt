package com.example.animatedapp.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.*
import kotlin.math.roundToInt

@Composable
fun LoginScreen() {
    var animationState by remember { mutableStateOf(AnimationState.WALKING) }
    
    // Lottie composition for the character
    // Note: In a real app, you'd place 'character.json' in res/raw
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(
        com.example.animatedapp.R.raw.character_animation
    ))
    
    val progress by animateLottieCompositionAsState(
        composition = composition,
        iterations = if (animationState == AnimationState.WALKING) LottieConstants.IterateForever else 1
    )

    // Animation for the character's horizontal position
    val characterXOffset by animateDpAsState(
        targetValue = when (animationState) {
            AnimationState.WALKING -> 0.dp
            AnimationState.WAVING -> 150.dp
            AnimationState.DRAGGING -> 300.dp
        },
        animationSpec = tween(durationMillis = 2000),
        label = "CharacterX"
    )

    // Animation for the Login Card's position (dragged in)
    val loginCardOffset by animateDpAsState(
        targetValue = if (animationState == AnimationState.DRAGGING) 0.dp else 500.dp,
        animationSpec = tween(durationMillis = 1500, easing = FastOutSlowInEasing),
        label = "LoginCard"
    )

    Box(modifier = Modifier.fillMaxSize().background(Color.White)) {
        
        // The Login Card (The thing being "dragged" in)
        Card(
            modifier = Modifier
                .align(Alignment.Center)
                .offset { IntOffset(0, loginCardOffset.value.roundToInt()) }
                .padding(24.dp)
                .fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            elevation = CardDefaults.cardElevation(8.dp)
        ) {
            Column(
                modifier = Modifier.padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Welcome Back", style = MaterialTheme.typography.headlineMedium)
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(value = "", onValueChange = {}, label = { Text("Email") })
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(value = "", onValueChange = {}, label = { Text("Password") })
                Spacer(modifier = Modifier.height(24.dp))
                Button(onClick = {}, modifier = Modifier.fillMaxWidth()) {
                    Text("Login")
                }
            }
        }

        // The Character
        LottieAnimation(
            composition = composition,
            progress = { progress },
            modifier = Modifier
                .size(200.dp)
                .align(Alignment.BottomStart)
                .offset(x = characterXOffset)
        )
    }

    // Simple state machine for the sequence
    LaunchedEffect(Unit) {
        kotlinx.coroutines.delay(3000)
        animationState = AnimationState.WAVING
        kotlinx.coroutines.delay(2000)
        animationState = AnimationState.DRAGGING
    }
}

enum class AnimationState {
    WALKING, WAVING, DRAGGING
}
