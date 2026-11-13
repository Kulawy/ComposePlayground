package com.jakger.ghcrange_mobile.ui.components

import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.jakger.feature_ghcrange_mobile.R

@Composable
fun AnimatedHeart(modifier: Modifier = Modifier) {
    val infiniteTransition = rememberInfiniteTransition(label = "HeartbeatTransition")
    val scale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = 1200
                1.0f at 0
                1.333f at 140 using FastOutSlowInEasing
                1.12f at 260 using FastOutLinearInEasing
                1.23f at 400 using FastOutSlowInEasing
                1.0f at 600 using LinearOutSlowInEasing
                1.0f at 1200
            },
            repeatMode = RepeatMode.Restart
        ),
        label = "HeartbeatScale"
    )

    Icon(
        painter = painterResource(R.drawable.ic_ecg_heart_48),
        contentDescription = "Animated Heart",
        tint = Color.White,
        modifier = modifier
            .size(48.dp)
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
                transformOrigin = TransformOrigin.Center
            }
    )
}

@Composable
fun StepsTrailingIcon(modifier: Modifier = Modifier){
    Icon(
        painter = painterResource(R.drawable.ic_footprint_48),
        contentDescription = "Footprint image",
        tint = Color.White,
        modifier = modifier
    )
}

@Composable
fun FireTrailingIcon(modifier: Modifier = Modifier){
    Icon(
        painter = painterResource(R.drawable.ic_fire_48),
        contentDescription = "Footprint image",
        tint = Color.White,
        modifier = modifier
    )
}