package com.rorycd.learningassistant.ui.quiz

/**
 * Represents the current state of the [QuizScreen] UI
 */
data class QuizUiState(
    val quizId: Int = -1,
    val question: String = "",
    val options: List<String> = emptyList(),
    val currentScore: Int = 0,
    val questionNumber: Int = -1,
    val totalQuestions: Int = -1
)
