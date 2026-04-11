package com.rorycd.eventplanner.utils

import java.time.Instant
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

fun formatDate(millis: Long): String {
    val formatter = DateTimeFormatter.ofPattern("E d MMMM")
    return Instant.ofEpochMilli(millis)
        .atZone(ZoneId.systemDefault())
        .format(formatter)
}

fun formatTime(millis: Long): String {
    val formatter = DateTimeFormatter.ofPattern("h:mm a")
    return Instant.ofEpochMilli(millis)
        .atZone(ZoneId.systemDefault())
        .format(formatter)
}

fun formatMinutes(mins: Int): String {
    val hour = mins / 60
    val minute = mins % 60
    val localTime = LocalTime.of(hour, minute)
    return localTime.format(DateTimeFormatter.ofPattern("h:mm a"))
}
