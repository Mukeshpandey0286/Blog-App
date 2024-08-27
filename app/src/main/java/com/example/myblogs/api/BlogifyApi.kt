package com.example.myblogs.api

import com.example.myblogs.models.BlogResponse
import com.example.myblogs.models.LoginResponse
import com.example.myblogs.models.LoginUser
import com.example.myblogs.models.User
import com.example.myblogs.models.UserProfile
import com.example.myblogs.models.userNameRequest
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path


interface BlogifyApi {
    @POST("/api/user/signup")
    suspend fun createUser(@Body user: User): Response<User>

    @POST("/api/user/signin")
    suspend fun loginUser(@Body user: LoginUser): Response<LoginResponse>

    @GET("/api/user/profile")
    suspend fun getUserProfile(@Header("Authorization") token: String): Response<UserProfile>

    @PATCH("/api/user/profile-update")
    suspend fun updateUserProfile(
        @Header("Authorization") token: String,
        @Body updatedProfile: userNameRequest
    ): Response<UserProfile>

    @GET("/api/blog")
    suspend fun getAllBlogs(@Header("Authorization") token: String): Response<List<BlogResponse>>

    @GET("/api/blog/{blogId}")
    suspend fun getBlogDetails(@Path("blogId") blogId: String): BlogResponse

    @Multipart
    @POST("/api/blog/add-new")
    suspend fun createBlog(
        @Header("Authorization") token: String,
        @Part("title") title: RequestBody,
        @Part("body") body: RequestBody,
        @Part coverImage: MultipartBody.Part?
    ): Response<BlogResponse>
}