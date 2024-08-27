package com.example.myblogs.bottomNavigation.ui.tabs

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.myblogs.R
import com.example.myblogs.Routes.Routes
import com.example.myblogs.Screens.LottieAnimationExample
import com.example.myblogs.bottomNavigation.nav.BottomNavItem
import com.example.myblogs.models.UserProfile
import com.example.myblogs.models.userNameRequest
import com.example.myblogs.viewmodels.ProfileViewModel

@Composable
fun ProfileScreen(
    navHostController: NavHostController,
    profileViewModel: ProfileViewModel = hiltViewModel()
) {
    val profileData by profileViewModel.profileData.collectAsState()

    Box {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(5.dp)
        ) {
            IconButton(
                onClick = { navHostController.navigate(BottomNavItem.Home.route) },
                modifier = Modifier.padding(start = 6.dp, top = 12.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = null,
                    modifier = Modifier.size(28.dp),
                    tint = Color.Black
                )
            }
            profileData?.let { profile ->
                ProfileSection(profile)
                ProfileDetail(profile, handleProfileUpdate(profile, profileViewModel), profileViewModel, navHostController)
            } ?: run {
                Box(Modifier.fillMaxSize()) {
                    LottieAnimationExample(
                        animationResId = R.raw.loading_text,
                        modifier = Modifier.fillMaxSize(.2f).align(Alignment.Center).padding(3.dp),
                    )
                }
            }
        }
    }
}

private fun handleProfileUpdate(
    profile: UserProfile,
    profileViewModel: ProfileViewModel
): (UserProfile) -> Unit {
    return { updatedProfile ->
        val userNameRequest = userNameRequest(
            name = updatedProfile.name
        )
        profileViewModel.updateProfileData(userNameRequest)

    }
}

@Composable
fun ProfileSection(profile: UserProfile) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(2.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            modifier = Modifier.size(170.dp),
            shape = RoundedCornerShape(100.dp),
            elevation = CardDefaults.outlinedCardElevation()
        ) {
            Image(
                painter = painterResource(id = R.drawable.user_profile),
                contentDescription = null,
                contentScale = ContentScale.Crop
            )
        }
        Text(
            text = profile.name,
            fontSize = 20.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color.DarkGray,
            fontFamily = FontFamily.Monospace
        )
        Text(
            text = profile.role,
            fontSize = 16.sp,
            fontWeight = FontWeight.Light,
            color = Color.Gray,
            fontFamily = FontFamily.Serif
        )
    }
}

@Composable
fun ProfileDetail(profile: UserProfile, onSaveProfile: (UserProfile) -> Unit, profileViewModel: ProfileViewModel, navHostController: NavHostController) {
    var isEnable by remember { mutableStateOf(false) }
    var userName by remember { mutableStateOf(profile.name) }


    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(3.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "Edit Profile",
                fontSize = 18.sp,
                fontFamily = FontFamily.Serif,
                color = Color.Gray,
                fontWeight = FontWeight.Light,
                fontStyle = FontStyle.Normal
            )
            Spacer(modifier = Modifier.width(12.dp))
            Button(
                onClick = {
                    if (isEnable) {
                        val updatedProfile = profile.copy(name = userName, _id = profile._id)
                        onSaveProfile(updatedProfile)
                    }
                    isEnable = !isEnable
                }, // Toggle the isEnable state
                colors = ButtonDefaults.buttonColors(Color.Transparent),
                border = BorderStroke(.5.dp, Color.Gray),
                shape = RectangleShape,
                modifier = Modifier
                    .padding(5.dp)
            ) {
                Icon(
                    imageVector = if (isEnable) Icons.Default.Send else Icons.Default.Edit,
                    contentDescription = "Edit",
                    tint = Color.DarkGray
                )
                Text(text = if (isEnable) "Save" else "Edit", color = Color.DarkGray)
            }
        }

        Spacer(modifier = Modifier.height(15.dp))
        ProfileTextView(
            textDetail = "Your Name",
            value = userName,
            isEnable,
            onValueChange = { userName = it })
        Spacer(modifier = Modifier.height(5.dp))
        ProfileTextView(
            textDetail = "Your Email",
            value = profile.email,
            isEnable = false,
            onValueChange = {})
        Spacer(modifier = Modifier.height(5.dp))
        ProfileTextView(
            textDetail = "Account Type",
            value = profile.role,
            isEnable = false,
            onValueChange = {})
        Spacer(modifier = Modifier.height(15.dp))
        Button(
            onClick = { profileViewModel.logoutProfile{
                navHostController.navigate(Routes.signInScreen) {
                    // Clear back stack to prevent returning to profile screen
                    popUpTo(Routes.signInScreen) { inclusive = true }
                }}},
            colors = ButtonDefaults.buttonColors(Color.LightGray),
            shape = RectangleShape,
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .height(55.dp)
        ) {
            Text(
                text = "Logout",
                color = Color.Black,
                fontFamily = FontFamily.Serif,
                fontStyle = FontStyle.Normal,
                letterSpacing = .8.sp,
                textAlign = TextAlign.Center,
                fontSize = 20.sp
            )
        }
    }
}

@Composable
fun ProfileTextView(
    textDetail: String,
    value: String,
    isEnable: Boolean,
    onValueChange: (String) -> Unit
) {

    Box(modifier = Modifier.fillMaxWidth()) {
        Column {
            Text(
                text = textDetail,
                modifier = Modifier.padding(start = 6.dp),
                color = Color.Black,
                fontSize = 18.sp,
                fontFamily = FontFamily.Serif,
                fontWeight = FontWeight.Bold,
                fontStyle = FontStyle.Normal,
                letterSpacing = 1.sp
            )
//            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = value,
                onValueChange = onValueChange,
                enabled = isEnable,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(6.dp),
                singleLine = true,
                textStyle = TextStyle(
                    fontSize = 18.sp,
                    fontFamily = FontFamily.Serif,
                    fontWeight = FontWeight.Thin,
                    color = if (isEnable) Color.Black else Color.DarkGray,
                    textAlign = TextAlign.Justify
                ),
            )
        }
    }
}

@Composable
@Preview
fun ProfileScreenPreview() {
    ProfileScreen(navHostController = NavHostController(LocalContext.current))
}