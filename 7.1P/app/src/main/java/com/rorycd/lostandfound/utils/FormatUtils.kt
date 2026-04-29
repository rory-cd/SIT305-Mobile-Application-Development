package com.rorycd.lostandfound.utils

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
