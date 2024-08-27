package com.example.myblogs.models

data class BlogRequest(
    val title: String,
    val body: String,
    val coverImage: String?=null,
)