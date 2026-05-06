package com.rorycd.chatbot.ui.home

/**
 * Represents the current state of the [HomeScreen] UI
 */
data class HomeUiState(
    val error: String = "",
    val loading: Boolean = false
)
