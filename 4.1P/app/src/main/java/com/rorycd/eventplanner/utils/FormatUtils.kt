package com.rorycd.eventplanner.utils

import java.time.Instant
import java.time.LocalTime
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

/**
 * Formats minutes past midnight as human-readable (e.g. 3:15 pm)
 */
fun formatMinutesAsTime(mins: Int): String {
    val hour = mins / 60
    val minute = mins % 60
    val localTime = LocalTime.of(hour, minute)
    return localTime.format(DateTimeFormatter.ofPattern("h:mm a"))
}

/**
 * Takes a timestamp and returns it as the number of minutes past midnight
 */
fun getMinsPastMidnight(timestamp: Long): Int {
    val time = Instant.ofEpochMilli(timestamp)
        .atZone(ZoneId.systemDefault())
        .toLocalTime()

    return time.hour * 60 + time.minute
}

/**
 * Edits timestamp in milliseconds to midnight of the same day
 */
fun timeStampAtMidnight(timestamp: Long): Long {
    return Instant.ofEpochMilli(timestamp)
        .atZone(ZoneId.systemDefault())
        .toLocalDate()
        .atStartOfDay(ZoneId.systemDefault())
        .toInstant()
        .toEpochMilli()
}
