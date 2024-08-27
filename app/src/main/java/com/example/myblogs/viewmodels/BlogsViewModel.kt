package com.example.myblogs.viewmodels

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Base64
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myblogs.models.BlogRequest
import com.example.myblogs.models.BlogResponse
import com.example.myblogs.models.UserProfile
import com.example.myblogs.repository.BlogifyRepository
import com.example.myblogs.utils.TokenManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.InputStream
import java.net.SocketTimeoutException
import javax.inject.Inject

@HiltViewModel
class BlogsViewModel @Inject constructor(private val repository: BlogifyRepository, private val tokenManager: TokenManager) : ViewModel() {

    private val _blogData = MutableStateFlow<List<BlogResponse>?>(null)
    val blogData: StateFlow<List<BlogResponse>?> = _blogData

    init {
        fetchAllBlogs()
    }

    fun fetchAllBlogs() {
        viewModelScope.launch {
            val token = tokenManager.getToken()
            if (token != null) {
                try {
                    val response = repository.getAllBlogs("Bearer $token") // Ensure "Bearer " prefix is included
                    if (response.isSuccessful && response.body() != null) {
                        _blogData.value = response.body()
                    } else {
                        // Handle unsuccessful response or empty body
                        Log.e(
                            "BlogsViewModel",
                            "Failed to fetch profile data: ${response.errorBody()?.string()}"
                        )
                    }
                } catch (e: SocketTimeoutException) {
                    Log.e("BlogsViewModel", "Network timeout", e)
                }
                catch (e: Exception) {
                    Log.e("BlogsViewModel", "Error fetching profile data", e)
                }
            } else {
                Log.e("BlogsViewModel", "No token found")
            }
        }

    }
}


@HiltViewModel
class BlogDetailsViewModel @Inject constructor(private val blogRepository: BlogifyRepository) : ViewModel() {

    private val _blogDetails = MutableStateFlow<BlogResponse?>(null)
    val blogDetails: StateFlow<BlogResponse?> = _blogDetails

    fun fetchBlogDetails(blogId: String) {
        viewModelScope.launch {
            _blogDetails.value = blogRepository.getBlogDetails(blogId)
        }
    }
}

@HiltViewModel
class CreateBlogViewModel @Inject constructor(
    private val blogRepository: BlogifyRepository,
    private val tokenManager: TokenManager
) : ViewModel() {

    private val _blogData = MutableStateFlow<BlogResponse?>(null)
    val blogData: StateFlow<BlogResponse?> = _blogData

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _isSuccess = MutableStateFlow(false)
    val isSuccess = _isSuccess.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    fun createBlog(context: Context, title: String, body: String, coverImageUri: Uri) {
        viewModelScope.launch {
            _isLoading.value = true
            val token = tokenManager.getToken()
            if (token != null) {
                try {
                    // Convert title and body to RequestBody
                    val titleRequestBody = title.toRequestBody("text/plain".toMediaTypeOrNull())
                    val bodyRequestBody = body.toRequestBody("text/plain".toMediaTypeOrNull())

                    // Get InputStream from URI
                    val inputStream = context.contentResolver.openInputStream(coverImageUri)
                    val fileBytes = inputStream?.readBytes()

                    // Create RequestBody for the image
                    val imageRequestBody = fileBytes?.let {
                        RequestBody.create("image/jpeg".toMediaTypeOrNull(), it)
                    }

                    // Create MultipartBody.Part for the image
                    val coverImagePart = imageRequestBody?.let {
                        MultipartBody.Part.createFormData("coverImage", "image.jpg", it)
                    }


                    val response = blogRepository.createBlog(
                        "Bearer $token",
                        titleRequestBody,
                        bodyRequestBody,
                        coverImagePart
                    )
                    if (response.isSuccessful) {
                        _blogData.value = response.body()
                        _isSuccess.value = true
                        Toast.makeText(context,"Blog created successfully", Toast.LENGTH_SHORT).show()
                        Log.d("CreateBlogViewModel", "Blog created successfully: ${response.body()}")
                    } else {
                        Log.d("CreateBlogViewModel", "data was ${response.body()}")
                        Log.e("CreateBlogViewModel", "Failed to create blog: ${response.errorBody()?.string()} ${response.message()} ${response.code()}")
                        _errorMessage.value = "Failed to create blog: ${response.message()} ${response.code()}"
                    }
                } catch (e: Exception) {
                    Log.e("CreateBlogViewModel", "Failed to create blog:  ${e.message} ")
                    _errorMessage.value = "Error creating blog: ${e.message}"
                }
            } else {
                _errorMessage.value = "Token not found"
            }
            _isLoading.value = false
        }
    }
    fun resetErrorMessage() {
        _errorMessage.value = null
    }
    fun resetSuccessStatus() {
        _isSuccess.value = false
    }
}

