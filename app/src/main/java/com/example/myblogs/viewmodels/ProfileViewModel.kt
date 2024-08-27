package com.example.myblogs.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myblogs.models.UserProfile
import com.example.myblogs.models.userNameRequest
import com.example.myblogs.repository.UserProfileRepository
import com.example.myblogs.utils.TokenManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val repository: UserProfileRepository,
    private val tokenManager: TokenManager
) : ViewModel() {

    private val _profileData = MutableStateFlow<UserProfile?>(null)
    val profileData: StateFlow<UserProfile?> = _profileData

    init {
        fetchProfileData()
    }

    private fun fetchProfileData() {
        viewModelScope.launch {
            val token = tokenManager.getToken()
            if (token != null) {
                try {
                    val response = repository.getUserProfile("Bearer $token") // Ensure "Bearer " prefix is included
                    if (response.isSuccessful && response.body() != null) {
                        _profileData.value = response.body()
                    } else {
                        // Handle unsuccessful response or empty body
                        Log.e("ProfileViewModel", "Failed to fetch profile data: ${response.errorBody()?.string()}")
                    }
                } catch (e: Exception) {
                    Log.e("ProfileViewModel", "Error fetching profile data", e)
                }
            } else {
                Log.e("ProfileViewModel", "No token found")
            }
        }
    }


    fun updateProfileData(newData: userNameRequest) {
        viewModelScope.launch {
            val token = tokenManager.getToken()
            if (token != null) {
                val response = repository.updateUserProfile(token, newData)
                if (response.isSuccessful) {
                    _profileData.value = response.body()
                }
            }
        }
    }

    fun logoutProfile(onLoggedOut: () -> Unit) {
        viewModelScope.launch {
            try {
                tokenManager.deleteToken()
                // Optionally clear other user data or preferences
                onLoggedOut() // Callback to handle post-logout actions
            } catch (e: Exception) {
                Log.e("ProfileViewModel", "Error logging out", e)
            }
        }
    }

}
