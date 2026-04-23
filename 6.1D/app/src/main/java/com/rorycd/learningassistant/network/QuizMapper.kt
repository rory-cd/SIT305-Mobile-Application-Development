package com.rorycd.learningassistant.network

import com.rorycd.learningassistant.data.Question
import com.rorycd.learningassistant.data.Quiz

/**
 * Extension method for [QuizResponse], converts from network form to Room entity
 */
fun QuizResponse.toRoomEntities(userId: Int, topic: String): Pair<Quiz, List<Question>> {

    // Quiz
    val quiz = Quiz(
        userId = userId,
        topic = topic
    )

    // Questions
    val questions = this.quiz.map { question ->
        Question(
            quizId = 0,
            title = question.question,
            options = question.options,
            answer = question.correctAnswer
        )
    }
    return Pair(quiz, questions)
}
