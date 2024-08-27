package com.example.myblogs.bottomNavigation.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.myblogs.bottomNavigation.nav.BottomNavItem
import com.example.myblogs.bottomNavigation.ui.tabs.CreateBlogScreen
import com.example.myblogs.bottomNavigation.ui.tabs.HomeScreen
import com.example.myblogs.bottomNavigation.ui.tabs.ProfileScreen
//import com.example.myblogs.bottomNavigation.ui.tabs.UserBlogScreen
import com.example.myblogs.bottomNavigation.ui.tabs.blogDetails


@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun MainEntryPoint() {

    val navController = rememberNavController()
    Scaffold(bottomBar = { MainBottomNavigation(navController = navController) }) { paddingVal ->
        MainNavigation(navHostController = navController, modifier = Modifier.padding(paddingVal))
    }
}


@Composable
fun MainNavigation(navHostController: NavHostController, modifier: Modifier) {
    NavHost(
        navController = navHostController,
        startDestination = BottomNavItem.Home.route,
        modifier
    ) {
        composable(BottomNavItem.Home.route) {
            HomeScreen(navHostController)
        }
        composable(BottomNavItem.Create.route) {
            CreateBlogScreen(navHostController = navHostController)
        }
//        composable(BottomNavItem.UserBlog.route) {
//            UserBlogScreen()
//        }
        composable(BottomNavItem.Profile.route) {
            ProfileScreen(navHostController)
        }
        composable("detail/{blogId}") { backStackEntry ->
            val blogId = backStackEntry.arguments?.getString("blogId") ?: ""
                blogDetails(blogId, navHostController = navHostController)

        }

    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainBottomNavigation(navController: NavHostController) {
    val items = listOf(
        BottomNavItem.Home,
        BottomNavItem.Create,
//        BottomNavItem.UserBlog,
        BottomNavItem.Profile
    )
    NavigationBar(containerColor = Color.White) {
        val navStack by navController.currentBackStackEntryAsState()
        val currentRoute = navStack?.destination?.route

        items.forEach { item ->
            NavigationBarItem(
                icon = {
                    BadgedBox(badge = {
                        if (currentRoute == item.route) {
                            Surface(
                                modifier = Modifier.padding(4.dp),
                                shape = CircleShape,
                                color = Color.Red
                            ) {
                                Text(
                                    text = "12",  // Example badge count
                                    style = TextStyle(fontSize = 6.sp),
                                    modifier = Modifier.padding(4.dp),
                                    color = Color.White,
                                    fontFamily = FontFamily.Serif,
                                    fontStyle = FontStyle.Normal,
                                    fontWeight = FontWeight.Thin
                                )
                            }
                        }
                    }) {
                        Icon(
                            imageVector = item.icon,
                            contentDescription = null,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                },
                label = {
                    Text(
                        item.label, color = Color.Black,
                        fontFamily = FontFamily.Serif,
                        fontStyle = FontStyle.Normal,
                        fontWeight = FontWeight.Thin,
                        fontSize = 10.sp
                    )
                },
                selected = currentRoute == item.route,
                onClick = {
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.startDestinationRoute ?: item.route) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                alwaysShowLabel = true,
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color.Red,
                    unselectedIconColor = Color.Black,
                    selectedTextColor = Color.Red,
                    unselectedTextColor = Color.Black
                )
            )
        }
    }
}