package com.rorycd.chatbot.utils

import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

/**
 * Formats date in milliseconds as human-readable (e.g. Mon 5 April)
 */
fun formatDateAsString(millis: Long): String {
    val formatter = DateTimeFormatter.ofPattern("E d MMMM")
    return Instant.ofEpochMilli(millis)
        .atZone(ZoneId.systemDefault())
        .format(formatter)
}

/**
 * Formats time in milliseconds as human-readable (e.g. 3:15 pm)
 */
fun formatTimeAsString(millis: Long): String {
    val formatter = DateTimeFormatter.ofPattern("h:mm a")
    return Instant.ofEpochMilli(millis)
        .atZone(ZoneId.systemDefault())
        .format(formatter)
}
