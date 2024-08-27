package com.example.myblogs.utils

import java.text.SimpleDateFormat
import java.util.*

fun parseDate(dateString: String): String {
    return try {
        // Define the correct date pattern for the given date string
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        inputFormat.timeZone = TimeZone.getTimeZone("UTC") // Ensure the timezone is UTC

        // Parse the date
        val date = inputFormat.parse(dateString)

        // Format the date to your desired output format
        val outputFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
        outputFormat.format(date ?: Date()) // Fallback to current date if parsing fails
    } catch (e: Exception) {
        "Invalid date" // Handle any parsing errors
    }
}
