package com.example.myblogs.models

data class User(
    var name:String,
    var email:String,
    var password:String
)

data class LoginUser(
    var email:String,
    var password:String
)

data class LoginResponse(
    val msg: String,
    val token: String
)

data class userNameRequest(
    val _id: String? = null,
    val name: String
)

data class UserProfile(
    val _id: String,
    val name: String,
    val email: String,
    val role: String
)

