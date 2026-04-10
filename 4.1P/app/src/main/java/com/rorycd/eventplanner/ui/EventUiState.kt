package com.rorycd.eventplanner.ui

data class EventUiState(
    val currentTitle: String = "",
    val currentLocation: String = "",
    val currentCategory: String = "",
    val currentDate: Long = System.currentTimeMillis(),
    val isValid: Boolean = false
)
