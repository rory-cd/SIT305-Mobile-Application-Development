package com.rorycd.chatbot.utils

import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

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

/**
 * Formats a timestamp relative to today
 */
fun formatTimestampRelative(millis: Long): String {
    val instant = Instant.ofEpochMilli(millis).atZone(ZoneId.systemDefault())
    val date = instant.toLocalDate()
    val time = instant.format(DateTimeFormatter.ofPattern("h:mm a"))
    val today = ZonedDateTime.now().toLocalDate()

    val daysBetween = ChronoUnit.DAYS.between(date, today)

    return when (daysBetween) {
        // Today
        0L -> time
        // Yesterday
        1L -> "Yesterday at $time"
        // In the past week
        in 2..7 -> "$daysBetween days ago"
        // Longer
        else -> date.format(DateTimeFormatter.ofPattern("E d MMMM"))
    }
}
