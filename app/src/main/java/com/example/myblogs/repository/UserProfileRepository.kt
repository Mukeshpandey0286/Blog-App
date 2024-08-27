package com.example.myblogs.repository

import com.example.myblogs.api.BlogifyApi
import com.example.myblogs.models.UserProfile
import com.example.myblogs.models.userNameRequest
import retrofit2.Response
import javax.inject.Inject

class UserProfileRepository @Inject constructor(private val blogifyApi: BlogifyApi){

    suspend fun getUserProfile(token: String): Response<UserProfile> {
        return blogifyApi.getUserProfile(token)
    }

    suspend fun updateUserProfile(token: String, updatedProfile: userNameRequest): Response<UserProfile> {
        return blogifyApi.updateUserProfile(token, updatedProfile)
    }
}