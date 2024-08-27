@file:Suppress("DEPRECATION")

package com.example.myblogs.bottomNavigation.ui.tabs

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import com.example.myblogs.R
import com.example.myblogs.Screens.LottieAnimationExample
import com.example.myblogs.bottomNavigation.nav.BottomNavItem
import com.example.myblogs.models.BlogResponse
import com.example.myblogs.utils.parseDate
import com.example.myblogs.viewmodels.BlogDetailsViewModel
import com.example.myblogs.viewmodels.BlogsViewModel
import com.example.myblogs.viewmodels.CreateBlogViewModel
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun HomeScreen(
    navHostController: NavHostController,
    blogsViewModel: BlogsViewModel = hiltViewModel()
) {
    val blogData by blogsViewModel.blogData.collectAsState()
    val isRefreshing = remember { mutableStateOf(false) }

    SwipeRefresh(
        state = rememberSwipeRefreshState(isRefreshing.value),
        onRefresh = {
            isRefreshing.value = true
            blogsViewModel.fetchAllBlogs()
            isRefreshing.value = false
        },
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(4.dp)
                .background(Color.White)
        ) {

            Column {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 4.dp, end = 7.dp, top = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {

                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(
                            painter = painterResource(id = R.drawable.menu),
                            contentDescription = null,
                            modifier = Modifier.size(35.dp)
                        )
                    }

                    Card(
                        modifier = Modifier.size(35.dp),
                        shape = RectangleShape,
                        elevation = CardDefaults.cardElevation(2.dp)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.blog_logo),
                            contentDescription = null,
                            contentScale = ContentScale.Crop
                        )
                    }
                }
//                if (blogData.isNullOrEmpty()) {
//                    Column(
//                        Modifier
//                            .fillMaxSize()
//                            .padding(8.dp),
//                        horizontalAlignment = Alignment.CenterHorizontally
//                    ) {
//                        LottieAnimationExample(
//                            animationResId = R.raw.loading_dots,
//                            modifier = Modifier.size(60.dp)
//                        )
//                        Text(text = "No data to show!! sorry first add some blog....")
//                    }
//                } else {

                    blogData?.let { blogs ->
                        AllBlogCards(blogs) { blog ->
                            navHostController.navigate("detail/${blog._id}") // Navigate to details screen
                        }
                    } ?: run {
                        Box(Modifier.fillMaxSize()) {
                            LottieAnimationExample(
                                animationResId = R.raw.loading_dots,
                                modifier = Modifier
                                    .fillMaxSize(.2f)
                                    .align(Alignment.Center)
                                    .padding(3.dp),
                            )
                        }

                }
            }
        }
    }
}

@Composable
fun AllBlogCards(blogs: List<BlogResponse>, onBlogClick: (BlogResponse) -> Unit) {

    LazyColumn(modifier = Modifier.padding(4.dp)) {
        items(blogs, key = { blog -> blog._id }) { blog ->
            blogCard(blog, onClick = onBlogClick)
        }
    }
}

@Composable
fun blogCard(blog: BlogResponse, onClick: (BlogResponse) -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(6.dp)
            .clickable { onClick(blog) }
    ) {
        Row {
            Card(
                modifier = Modifier.size(90.dp),
                shape = RoundedCornerShape(35.dp),
                elevation = CardDefaults.cardElevation(1.dp)
            ) {
                Image(
                   painter = rememberAsyncImagePainter(blog.coverImage),
                    contentDescription = null,
                    contentScale = ContentScale.Crop
                )
            }
            Column(
                modifier = Modifier
                    .weight(0.69f)
                    .padding(start = 8.dp, top = 4.dp)
            ) {
                Text(
                    text = blog.title,
                    fontSize = 21.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    fontFamily = FontFamily.Serif,
                    maxLines = 2,
                    overflow =  androidx.compose.ui.text.style.TextOverflow.Ellipsis
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = parseDate(blog.createdAt),
                        fontSize = 14.sp,
                        color = Color.Gray,
                        fontWeight = FontWeight.Light,
                        fontFamily = FontFamily.Serif
                    )
                    Text(
                        text = " | 8 min read",
                        fontSize = 15.sp,
                        color = Color.Gray,
                        fontWeight = FontWeight.Light,
                        fontFamily = FontFamily.Serif
                    )
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun blogDetails(blogId: String, viewModel: BlogDetailsViewModel = hiltViewModel(), navHostController: NavHostController) {
    viewModel.fetchBlogDetails(blogId)
    val blog by viewModel.blogDetails.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Blog Details", fontSize = 18.sp, color = Color.Black, fontFamily = FontFamily.Serif) },
                navigationIcon = {
                    IconButton(onClick = { navHostController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }, modifier = Modifier.background(Color.White)
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(6.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            blog?.let {

                Column(modifier = Modifier.fillMaxSize()) {

                    Image(
                        painter = rememberAsyncImagePainter(it.coverImage),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(400.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    Column(
                        modifier = Modifier
                            .verticalScroll(rememberScrollState()),
                    ) {
                        Text(
                            text = "Title :-",
                            fontSize = 14.sp,
                            color = Color.Gray,
                            fontWeight = FontWeight.Light,
                            fontFamily = FontFamily.Serif,
                        )

                        Text(
                            text = it.title,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Serif,
                            color = Color.Black
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Blog About :-",
                            fontSize = 14.sp,
                            color = Color.Gray,
                            fontWeight = FontWeight.Light,
                            fontFamily = FontFamily.Serif
                        )
                        Text(
                            text = it.body,
                            fontSize = 16.sp,
                            fontFamily = FontFamily.Serif,
                            fontWeight = FontWeight.Bold,
                            color = Color.DarkGray
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text(
                                    text = "Created at :-",
                                    fontSize = 14.sp,
                                    color = Color.Gray,
                                    fontWeight = FontWeight.Light,
                                    fontFamily = FontFamily.Serif
                                )
                                Text(
                                    text = parseDate(it.createdAt),
                                    fontSize = 14.sp,
                                    color = Color.Gray,
                                    fontWeight = FontWeight.Light,
                                    fontFamily = FontFamily.Serif
                                )
                            }

                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = " | 8 min read",
                                fontSize = 14.sp,
                                fontFamily = FontFamily.Serif,
                                color = Color.Gray,
                                fontWeight = FontWeight.Light
                            )
                        }
                    }
                }
            } ?: run {
                Text("Loading...")
            }
        }
    }
}
