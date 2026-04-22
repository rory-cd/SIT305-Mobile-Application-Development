package com.rorycd.learningassistant.network

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class QuizResponse(
    val quiz: List<QuizItem>
)

@Serializable
data class QuizItem(
    val question: String,
    val options: List<String>,
    @SerialName("correct_answer")
    val correctAnswer: String
)
