package com.example.myblogs.utils

import android.content.Context
import com.example.myblogs.utils.Constants.PREFS_TOKEN_FILES
import com.example.myblogs.utils.Constants.USER_TOKEN
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class TokenManager @Inject constructor(@ApplicationContext context: Context) {

    private val prefs = context.getSharedPreferences(PREFS_TOKEN_FILES, Context.MODE_PRIVATE)

    fun saveToken(token: String){
        val editor = prefs.edit()
        editor.putString(USER_TOKEN, token)
        editor.apply()
    }

    fun getToken() : String? {
        return prefs.getString(USER_TOKEN, null)
    }

    fun deleteToken() {
        val editor = prefs.edit()
        editor.remove(USER_TOKEN)
        editor.apply()
    }

    fun isTokenAvailable(): Boolean {
        return getToken() != null
    }

    fun isTokenExpired(): Boolean {
        val token = getToken()
        return token == null || isTokenExpired(token)
    }


}