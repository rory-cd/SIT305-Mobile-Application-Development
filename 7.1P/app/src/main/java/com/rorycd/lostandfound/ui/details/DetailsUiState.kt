package com.rorycd.lostandfound.ui.details

/**
 * Represents the UI state for the Details screen
 */
data class DetailsUiState (
    val id: Int = -1,
    val postType: String = "",
    val name: String = "",
    val imgUri: String = "",
    val phone: String = "",
    val description: String = "",
    val dateFormatted: String = "",
    val location: String = "",
    val toastMessage: String? = null
)
