package com.example.myblogs

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.myblogs.Routes.Routes
import com.example.myblogs.Screens.LoadingOverlay
import com.example.myblogs.Screens.MainScreen
import com.example.myblogs.Screens.SignInScreen
import com.example.myblogs.Screens.SignUpScreen
import com.example.myblogs.Screens.SplashScreen
import com.example.myblogs.ui.theme.MyBlogsTheme
import com.example.myblogs.viewmodels.SignInViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyBlogsApp()
        }
    }
}

@Composable
fun MyBlogsApp() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Routes.splashScreen,
        modifier = Modifier.fillMaxSize()
    ) {
        composable(Routes.splashScreen) { SplashScreen(navController) }
        composable(Routes.signInScreen) {
            SignInScreen(navController, viewModel = hiltViewModel())
        }
        composable(Routes.signUpScreen) {
            SignUpScreen(navController, viewModel = hiltViewModel())
        }
        composable(Routes.mainScreen) {
            MainScreen()
        }
    }
}

