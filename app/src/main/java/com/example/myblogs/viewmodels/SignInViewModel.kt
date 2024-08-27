package com.example.myblogs.viewmodels

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.myblogs.Routes.Routes
import com.example.myblogs.api.BlogifyApi
import com.example.myblogs.models.LoginUser
import com.example.myblogs.models.User
import com.example.myblogs.utils.TokenManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.HttpException
import javax.inject.Inject

data class SignInFormState(
    val email: String = "",
    val password: String = "",
    val isValid: Boolean = false,
    val errorMessage: String? = null
)

@HiltViewModel
class SignInViewModel @Inject constructor(private val api: BlogifyApi,  private val tokenManager: TokenManager):ViewModel() {
    private val _formState = MutableStateFlow(SignInFormState())
    val formState: StateFlow<SignInFormState> = _formState
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading


    fun onEmailChanged(email: String) {
        _formState.value = _formState.value.copy(email = email)
        validateForm()
    }

    fun onPasswordChanged(password: String) {
        _formState.value = _formState.value.copy(password = password)
        validateForm()
    }

    private fun validateForm() {
        val currentState = _formState.value
        val isEmailValid = android.util.Patterns.EMAIL_ADDRESS.matcher(currentState.email).matches()
        val isPasswordValid = currentState.password.length >= 6

        val isValid = isEmailValid && isPasswordValid

        _formState.value = currentState.copy(
            isValid = isValid,
            errorMessage = when {
               !isEmailValid -> "Invalid email format"
                !isPasswordValid -> "Password must be at least 6 characters"
                else -> null
            }
        )
    }

    fun checkTokenAndNavigate(navController: NavController) {

        viewModelScope.launch {

            _isLoading.value = true

            if (tokenManager.isTokenAvailable()) {
                // Token is available, navigate to the main screen
                navController.navigate(Routes.mainScreen) {
                    popUpTo(0) { inclusive = true }
                }
            } else {
                // Token is not available, navigate to the login screen
                navController.navigate(Routes.signInScreen) {
                    popUpTo(0) { inclusive = true }
                }
            }
        }
    }


    fun signInUser(navController: NavController) {
        val currentState = _formState.value
        if (currentState.isValid) {
            _formState.value = currentState.copy(errorMessage = null) // Clear previous error
            viewModelScope.launch {
                try {
                    _isLoading.value = true
                    val user = LoginUser(
                        email = currentState.email,
                        password = currentState.password
                    )
                    // Log the user data being sent
                    println("Sending login request with: $user")

                    val response = api.loginUser(user)
                    // Log the response code and message
                    println("Response code: ${response.code()}, message: ${response.message()}")

                    if (response.isSuccessful ) {
                        val token = response.body()?.token

                        if (token != null) {
                            // Save the token using TokenManager
                            tokenManager.saveToken(token)
                        }
                        // Show loading animation for 2 seconds before navigating
                        Handler(Looper.getMainLooper()).postDelayed({
                            println("Toekn:  ${token}")
                            _isLoading.value = false
                            navController.navigate(Routes.mainScreen) {
                                popUpTo(0) { inclusive = true }
                            }
                        }, 2000)
                    } else {
                        // Handle sign-up error, display an error message
                        _isLoading.value = false
                        // Extract error message from the response
                        val errorBody = response.errorBody()?.string()
                        val errorMessage = errorBody?.let {
                            try {
                                val jsonObject = JSONObject(it)
                                jsonObject.getString("msg") // or the appropriate key where your API returns the error message
                            } catch (e: Exception) {
                                "Sign-up failed: Unexpected error"
                            }
                        } ?: "Sign-up failed: ${response.message()}"

                        _formState.value = currentState.copy(errorMessage = errorMessage)
                        println("Sign-in failed: $errorMessage")
                    }
                } catch (e: HttpException) {
                    _isLoading.value = false
                    _formState.value = currentState.copy(
                        errorMessage = "An error occurred: ${e.response()?.errorBody()?.string() ?: e.localizedMessage}"
                    )
                    println("HttpException occurred: ${e.localizedMessage}")
                } catch (e: Exception) {
                    // Handle sign-up error, hide loading and display an error message
                    _isLoading.value = false
                    // Handle network or other errors, display an error message
                    _formState.value = currentState.copy(
                        errorMessage = "An error occurred: ${e.localizedMessage}"
                    )
                    println("HttpException occurred: ${e.localizedMessage}")
                }
            }
        }
    }


}