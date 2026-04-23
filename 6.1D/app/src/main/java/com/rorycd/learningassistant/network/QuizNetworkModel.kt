package com.rorycd.learningassistant.network

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// Model classes to define the shape of data received from the network
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

@Serializable
data class FeedbackRequest(val questions: String)

@Serializable
data class FeedbackResponse(val feedback: String)