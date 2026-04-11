package com.rorycd.eventplanner.ui.newevent

import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId

data class NewEventUiState(
    val currentTitle: String = "",
    val currentLocation: String = "",
    val currentCategory: String = "",
    val currentDate: Long = LocalDate.now()
        .atStartOfDay(ZoneId.systemDefault())
        .toInstant()
        .toEpochMilli(),
    val currentTimeMins: Int = LocalTime.now()
        .plusHours(1)
        .withMinute(0)
        .let { it.hour * 60 },
    val isValid: Boolean = false
)
