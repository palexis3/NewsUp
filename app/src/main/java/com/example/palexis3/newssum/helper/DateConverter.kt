package com.example.palexis3.newssum.helper

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

fun String.toDate(
    dateFormat: String = "yyyy-MM-dd'T'HH:mm:ss",
    timeZone: TimeZone = TimeZone.getTimeZone("UTC")
): Date {
    val parser = SimpleDateFormat(dateFormat, Locale.getDefault()).also {
        it.timeZone = timeZone
    }
    return parser.parse(this)
}

fun String.toDateEmptySpace(
    dateFormat: String = "yyyy-MM-dd HH:mm:ss"
) = this.toDate(dateFormat)

/**
 * Utility method that will help convert UTC dates returned from API into readable strings.
 * Reference here: https://stackoverflow.com/a/53714194/3681456
 */
fun Date.formatToReadableDate(
    dateFormat: String = "MMM dd yyyy",
    timeZone: TimeZone = TimeZone.getDefault()
): String {
    val formatter = SimpleDateFormat(dateFormat, Locale.getDefault()).also {
        it.timeZone = timeZone
    }
    return formatter.format(this)
}
