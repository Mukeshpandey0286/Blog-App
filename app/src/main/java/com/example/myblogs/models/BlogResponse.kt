package com.example.myblogs.models

data class BlogResponse(
    val __v: Int,
    val _id: String,
    val body: String,
    val comments: List<String>,
    val createdBy: String,
    val title: String,
    val createdAt: String,
    val updatedAt: String,
    val coverImage: String,
)
