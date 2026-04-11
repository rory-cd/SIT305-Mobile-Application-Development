package com.rorycd.eventplanner.ui.eventlist

data class EventUiModel(
    val id: Int,
    val title: String,
    val location: String?,
    val category: String?,
    val dateFormatted: String,
    val timeFormatted: String,
)
