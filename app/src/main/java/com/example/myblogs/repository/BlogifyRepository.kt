package com.example.myblogs.repository

import com.example.myblogs.api.BlogifyApi
import com.example.myblogs.models.BlogRequest
import com.example.myblogs.models.BlogResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import javax.inject.Inject


class BlogifyRepository @Inject constructor(private val blogifyApi: BlogifyApi) {

    suspend fun getAllBlogs(token: String): Response<List<BlogResponse>>{
        return blogifyApi.getAllBlogs(token)
    }

    suspend fun getBlogDetails(blogId: String): BlogResponse{
        return blogifyApi.getBlogDetails(blogId)
    }

    suspend fun createBlog(
        token: String,
        title: RequestBody,
        body: RequestBody,
        coverImage: MultipartBody.Part?
    ): Response<BlogResponse> {
        return blogifyApi.createBlog(token, title, body, coverImage)
    }

}