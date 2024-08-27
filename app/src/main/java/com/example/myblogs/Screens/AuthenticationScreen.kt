package com.example.myblogs.Screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.myblogs.R
import com.example.myblogs.Routes.Routes
import com.example.myblogs.viewmodels.SignInFormState
import com.example.myblogs.viewmodels.SignInViewModel
import com.example.myblogs.viewmodels.SignUpFormState
import com.example.myblogs.viewmodels.SignUpViewModel


@Composable
fun SignUpScreen(
    navController: NavController,
    viewModel: SignUpViewModel = hiltViewModel()
) {
    val gradientColors = listOf(
        Color(0xFFEB95E0),
        Color(0xFFEB91B1),
        Color(0xFFA1C4F0),
        Color(0xFFE9AEE3),
    )

    val formState by viewModel.formState.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.linearGradient(
                    colors = gradientColors,
                    start = Offset(0f, 0f),
                    end = Offset(1000f, 1000f)
                )
            )
    ) {
        Column(modifier = Modifier.padding(top = 30.dp)) {
            TopBar(
                navController,
                textForInfo = "Hello there!\nCreate an account below",
                textForAccountDetail = "Already have an Account!",
                btnText = "Login",
                route = Routes.signInScreen,
                isLoading = isLoading
            )
            Spacer(modifier = Modifier.height(30.dp))
            BottomViewForSignUp(
                navController,
                isLoading,
                formState,
                viewModel::onUsernameChanged,
                viewModel::onEmailChanged,
                viewModel::onPasswordChanged,
                { viewModel.signUpUser(navController) }
            )
        }

        if (isLoading) {
            LoadingOverlay(animRes = R.raw.loading_dots)
        }
    }
}

@Composable
fun SignInScreen(
    navController: NavController,
    viewModel: SignInViewModel = hiltViewModel()
) {
    val gradientColors = listOf(
        Color(0xFFEB95E0),
        Color(0xFFEB91B1),
        Color(0xFFA1C4F0),
        Color(0xFFE9AEE3),
    )

    val formState by viewModel.formState.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.linearGradient(
                    colors = gradientColors,
                    start = Offset(0f, 0f),
                    end = Offset(1000f, 1000f)
                )
            )
            .clickable(enabled = !isLoading) {}
    ) {
        Column(modifier = Modifier.padding(top = 30.dp)) {
            TopBar(
                navController,
                textForInfo = "Welcome Back!\nLet's login now..",
                textForAccountDetail = "Don't have an account\nCreate a new Account!",
                btnText = "Register",
                route = Routes.signUpScreen,
                isLoading = isLoading
            )
            Spacer(modifier = Modifier.padding(top = 30.dp))
            BottomViewForSignIn(
                navController,
                isLoading,
                formState,
                viewModel::onEmailChanged,
                viewModel::onPasswordChanged,
                { viewModel.signInUser(navController) }
            )
        }

        if (isLoading) {
            LoadingOverlay(animRes = R.raw.loading_dots)
        }
    }
}

@Composable
fun BottomViewForSignUp(
    navController: NavController,
    isLoading: Boolean,
    formState: SignUpFormState,
    onUsernameChanged: (String) -> Unit,
    onEmailChanged: (String) -> Unit,
    onPasswordChanged: (String) -> Unit,
    onSignUpClicked: () -> Unit
) {
    Column(Modifier.padding(10.dp)) {
        Spacer(modifier = Modifier.padding(top = 16.dp))
        TextInput(
            icon = Icons.Default.Person,
            label = "Username",
            value = formState.username,
            onValueChange = onUsernameChanged,
            enabled = !isLoading
        )
        Spacer(modifier = Modifier.padding(top = 16.dp))
        TextInput(
            icon = Icons.Default.Email,
            label = "Email",
            value = formState.email,
            onValueChange = onEmailChanged,
            enabled = !isLoading
        )
        Spacer(modifier = Modifier.padding(top = 16.dp))
        TextInput(
            icon = Icons.Default.Lock,
            label = "Password",
            value = formState.password,
            onValueChange = onPasswordChanged,
            enabled = !isLoading
        )
        Spacer(modifier = Modifier.padding(top = 38.dp))
        Button(
            onClick = onSignUpClicked,
            modifier = Modifier.fillMaxWidth(),
            enabled = formState.isValid && !isLoading,
            colors = ButtonDefaults.buttonColors(Color.Gray)
        ) {
            Text(
                "SIGN UP",
                Modifier.padding(vertical = 8.dp),
                color = Color.Black,
                fontSize = 18.sp,
                fontFamily = FontFamily.Serif,
                fontStyle = FontStyle.Normal,
                fontWeight = FontWeight.Bold
            )
        }

        formState.errorMessage?.let { error ->
            Spacer(modifier = Modifier.padding(top = 8.dp))
            Text(
                text = error,
                color = Color.Red,
                fontSize = 15.sp,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                fontFamily = FontFamily.Serif,
                fontStyle = FontStyle.Normal,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
fun BottomViewForSignIn(
    navController: NavController,
    isLoading: Boolean,
    formState: SignInFormState,
    onEmailChanged: (String) -> Unit,
    onPasswordChanged: (String) -> Unit,
    onSignUpClicked: () -> Unit
) {
    Column(Modifier.padding(10.dp)) {
        Spacer(modifier = Modifier.padding(top = 16.dp))
        TextInput(
            icon = Icons.Default.Email,
            label = "Email",
            value = formState.email,
            onValueChange = onEmailChanged,
            enabled = !isLoading
        )
        Spacer(modifier = Modifier.padding(top = 16.dp))
        TextInput(
            icon = Icons.Default.Lock,
            label = "Password",
            value = formState.password,
            onValueChange = onPasswordChanged,
            enabled = !isLoading
        )
        Spacer(modifier = Modifier.padding(top = 38.dp))
        Button(
            onClick = onSignUpClicked,
            modifier = Modifier.fillMaxWidth(),
            enabled = formState.isValid && !isLoading,
            colors = ButtonDefaults.buttonColors(Color.Gray)
        ) {
            Text(
                "SIGN IN",
                Modifier.padding(vertical = 8.dp),
                color = Color.Black,
                fontSize = 18.sp,
                fontFamily = FontFamily.Serif,
                fontStyle = FontStyle.Normal,
                fontWeight = FontWeight.Bold
            )
        }

        formState.errorMessage?.let { error ->
            Spacer(modifier = Modifier.padding(top = 8.dp))
            Text(
                text = error,
                color = Color.Red,
                fontSize = 15.sp,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                fontFamily = FontFamily.Serif,
                fontStyle = FontStyle.Normal,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
fun TopBar(
    navController: NavController,
    textForInfo: String,
    textForAccountDetail: String,
    btnText: String,
    route: String,
    isLoading: Boolean
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 10.dp, top = 8.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier.fillMaxWidth(),
            ) {
                LottieAnimationExample(
                    animationResId = R.raw.hii, modifier = Modifier
                        .fillMaxWidth(.3f)
                        .height(150.dp)
                )
                Spacer(modifier = Modifier.height(14.dp))
                Text(
                    text = textForInfo,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 26.sp,
                    color = Color.Black,
                    textAlign = TextAlign.Start,
                    fontFamily = FontFamily.Serif,
                    fontStyle = FontStyle.Normal,
                    modifier = Modifier.padding(top = 12.dp)

                )

                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = textForAccountDetail,
                        fontWeight = FontWeight.Light,
                        fontSize = 20.sp,
                        color = Color.Gray,
                        fontFamily = FontFamily.Serif,
                        fontStyle = FontStyle.Normal
                    )
                    Button(modifier = Modifier
                        .fillMaxWidth(.8f)
                        .border(
                            ButtonDefaults.outlinedButtonBorder,
                            shape = ButtonDefaults.shape
                        )
                        .height(45.dp),
                        enabled = !isLoading,
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Unspecified),
                        onClick = {
                            navController.navigate(route) {
                                // Clear the back stack to avoid loops
                                popUpTo(0) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }) {
                        Text(
                            text = btnText,
                            color = Color.Black,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Thin,
                            fontFamily = FontFamily.Serif,
                            fontStyle = FontStyle.Normal
                        )
                    }
                }
            }
        }

    }
}

@Composable
fun TextInput(
    icon: ImageVector,
    label: String,
    value: String,
    enabled: Boolean = true,
    onValueChange: (String) -> Unit
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier
            .fillMaxWidth(),
        leadingIcon = { Icon(imageVector = icon, contentDescription = null) },
        label = { Text(text = label) },
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White,
            disabledContainerColor = Color.White,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
        ),
        singleLine = true,
        enabled = enabled
    )
}

@Composable
fun LottieAnimationExample(animationResId: Int, modifier: Modifier) {
    // Load the animation from the raw resource folder
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(animationResId))
    var isPlaying by remember { mutableStateOf(true) }

    // Lottie animation
    LottieAnimation(
        composition = composition,
        iterations = LottieConstants.IterateForever,
        isPlaying = isPlaying,
        modifier = modifier
    )
}

@Composable
fun LoadingOverlay(animRes: Int) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.5f)), // Semi-transparent background
        contentAlignment = Alignment.Center
    ) {
        LottieAnimationExample(
            animationResId = animRes, modifier = Modifier
                .fillMaxWidth(.3f)
                .height(150.dp)
        ) // Use your Lottie animation here
    }
}