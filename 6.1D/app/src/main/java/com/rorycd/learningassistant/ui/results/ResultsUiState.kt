package com.rorycd.learningassistant.ui.results

/**
 * Represents the current state of the [ResultsScreen] UI
 */
data class ResultsUiState (
    val username: String = "",
    val correctAnswers: Int = 0,
    val questionCount: Int = 0,
    val feedback: String = "",
    val isLoaded: Boolean = false
)
