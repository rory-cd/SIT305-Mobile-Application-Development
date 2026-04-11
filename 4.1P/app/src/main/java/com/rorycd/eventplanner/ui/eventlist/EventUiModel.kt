package com.rorycd.eventplanner.ui.eventlist

/**
 * Model for representing an Event in UI
 */
data class EventUiModel(
    val id: Int,
    val title: String,
    val location: String?,
    val category: String?,
    val dateFormatted: String,
    val timeFormatted: String,
)
