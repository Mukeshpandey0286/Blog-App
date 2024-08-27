package com.example.myblogs.Screens

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.myblogs.R
import com.example.myblogs.viewmodels.SignInViewModel
import kotlinx.coroutines.delay


@Composable
fun SplashScreen(navController: NavController) {
    // Get the view model
    val signInViewModel: SignInViewModel = hiltViewModel()

    // Animation state
    val animation = remember { Animatable(0f) }

    // Start the animation and handle navigation after animation completes
    LaunchedEffect(Unit) {
        // Animate the logo
        animation.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 2000, easing = FastOutSlowInEasing)
        )

        // Check token and navigate accordingly
        signInViewModel.checkTokenAndNavigate(navController)
    }

    // UI for the splash screen
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        LottieAnimationExample(
        animationResId = R.raw.blog_load, modifier = Modifier
            .fillMaxSize()
    )
    }
}
