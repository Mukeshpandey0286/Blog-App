package com.example.myblogs.utils

fun isTokenExpired(token: String): Boolean {
    try {
        // Split the token into its three parts
        val parts = token.split(".")
        if (parts.size != 3) {
            return true // Invalid token format
        }

        // Decode the payload (the second part) from Base64
        val payload = String(android.util.Base64.decode(parts[1], android.util.Base64.URL_SAFE))

        // Parse the payload as a JSON object
        val jsonObject = org.json.JSONObject(payload)

        // Get the expiration time from the payload
        val exp = jsonObject.getLong("exp")

        // Convert the expiration time to milliseconds
        val expirationTime = exp * 1000

        // Get the current time in milliseconds
        val currentTime = System.currentTimeMillis()

        // Check if the token has expired
        return currentTime > expirationTime
    } catch (e: Exception) {
        // Handle errors (e.g., JSON parsing errors)
        e.printStackTrace()
        return true // Assume expired if there's an error
    }
}
