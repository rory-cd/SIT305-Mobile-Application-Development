package com.rorycd.eventplanner.ui

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class EventUiState(
    val currentTitle: String = "",
    val currentLocation: String = "",
    val currentCategory: String = "",
    val currentDate: String = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date()),
    val isValid: Boolean = false
)
