package com.rorycd.eventplanner.ui.editevent

data class EditEventUiState(
    val currentTitle: String,
    val currentLocation: String,
    val currentCategory: String,
    val currentDate: Long,
    val currentTimeMins: Int,
    val isValid: Boolean
)
