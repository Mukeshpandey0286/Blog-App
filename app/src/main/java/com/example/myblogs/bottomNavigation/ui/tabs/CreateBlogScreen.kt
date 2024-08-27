package com.example.myblogs.bottomNavigation.ui.tabs

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.myblogs.R
import com.example.myblogs.Screens.LottieAnimationExample
import com.example.myblogs.viewmodels.CreateBlogViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateBlogScreen(navHostController: NavHostController, viewModel: CreateBlogViewModel = hiltViewModel()) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Create Blog",
                        fontSize = 18.sp,
                        color = Color.Black,
                        fontFamily = FontFamily.Serif
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navHostController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        },
        modifier = Modifier
            .background(Color.White)
            .padding(6.dp)
    ) { paddingValues ->
        val isSuccess by viewModel.isSuccess.collectAsState()
        var title by remember { mutableStateOf("") }
        var description by remember { mutableStateOf("") }
        var imageUri by remember { mutableStateOf<Uri?>(null) }

        if (isSuccess) {
            title = ""
            description = ""
            imageUri = null
            viewModel.resetSuccessStatus()
        }
Box(modifier = Modifier.padding(paddingValues)) {
    CreateBlogContent(
        title = title,
        description = description,
        imageUri = imageUri,
        onTitleChange = { title = it },
        onDescriptionChange = { description = it },
        onImageUriChange = { imageUri = it },
        viewModel = viewModel
    )
}
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateBlogContent(
    title: String,
    description: String,
    imageUri: Uri?,
    onTitleChange: (String) -> Unit,
    onDescriptionChange: (String) -> Unit,
    onImageUriChange: (Uri?) -> Unit,
    viewModel: CreateBlogViewModel
) {
    val isLoading by viewModel.isLoading.collectAsState()
    val context = LocalContext.current

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        onImageUriChange(uri)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(6.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (imageUri == null) {
            Text(
                text = "Add Image",
                fontWeight = FontWeight.Medium,
                fontFamily = FontFamily.Serif,
                color = Color.Gray
            )
            Card(
                modifier = Modifier.size(200.dp),
                colors = CardDefaults.cardColors(Color.Transparent),
                shape = RectangleShape,
                onClick = { imagePickerLauncher.launch("image/*") }
            ) {
                Image(
                    painter = painterResource(id = R.drawable.select),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Fit
                )
            }
        } else {
            Image(
                painter = rememberAsyncImagePainter(imageUri),
                contentDescription = "Selected Image",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp)
                    .padding(top = 16.dp),
                contentScale = ContentScale.Inside
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Column {
            Text(
                text = "Add Blog Title:-",
                fontWeight = FontWeight.Medium,
                fontFamily = FontFamily.Serif,
                color = Color.DarkGray
            )
            OutlinedTextField(
                value = title,
                onValueChange = onTitleChange,
                label = {
                    Text(
                        text = "Blog Title",
                        fontWeight = FontWeight.Medium,
                        fontFamily = FontFamily.Serif
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Next
                )
            )
            Spacer(modifier = Modifier.height(18.dp))

            Text(
                text = "Add Blog Description:-",
                fontWeight = FontWeight.Medium,
                fontFamily = FontFamily.Serif,
                color = Color.DarkGray
            )
            OutlinedTextField(
                value = description,
                onValueChange = onDescriptionChange,
                label = {
                    Text(
                        text = "Blog Description",
                        fontWeight = FontWeight.Medium,
                        fontFamily = FontFamily.Serif
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Done, keyboardType = KeyboardType.Text
                ),
                maxLines = 15
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                if (title.isNotEmpty() && description.isNotEmpty() && imageUri != null) {
                    viewModel.createBlog(context, title, description, imageUri)
                } else {
                    Toast.makeText(context, "Please fill all fields", Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = 4.dp)
                .fillMaxWidth()
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(Color.Gray),
            shape = RectangleShape
        ) {
            if (isLoading) {
                LottieAnimationExample(
                    animationResId = R.raw.loading_dots,
                    modifier = Modifier.weight(.08f)
                )
            } else {
                Text(
                    text = "Add Blog",
                    fontWeight = FontWeight.Medium,
                    fontFamily = FontFamily.Serif,
                    fontSize = 20.sp
                )
            }
        }
    }
}




@Preview
@Composable
fun createBlogShow() {
    CreateBlogScreen(NavHostController(LocalContext.current))
}