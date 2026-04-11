package com.rorycd.eventplanner.ui.editevent

/**
 * Represents the current state of the [EditEventScreen] UI
 */
data class EditEventUiState(
    val currentTitle: String,
    val currentLocation: String,
    val currentCategory: String,
    val currentDate: Long,
    val currentTimeMins: Int,
    val isValid: Boolean
)
