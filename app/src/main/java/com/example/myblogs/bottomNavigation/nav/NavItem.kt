package com.example.myblogs.bottomNavigation.nav

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavItem(val icon: ImageVector, val label: String, val route: String) {

    object Home : BottomNavItem(icon = Icons.Default.Home, label = "Home", route = "home")
    object Create : BottomNavItem(icon = Icons.Default.Add, label = "Create Blog", route = "create_blog")
//    object UserBlog : BottomNavItem(icon = Icons.Default.List, label = "User Blogs", route = "user_blog")
    object Profile :
        BottomNavItem(icon = Icons.Default.AccountCircle, label = "Profile", route = "profile")

    object Warning :
        BottomNavItem(icon = Icons.Default.Warning, label = "Warning", route = "warning")

}