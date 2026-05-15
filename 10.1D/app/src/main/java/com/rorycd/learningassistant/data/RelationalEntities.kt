package com.rorycd.learningassistant.data

import androidx.room.Embedded
import androidx.room.Relation

/**
 * Relation entity, joining questions to answers
 */
data class AnswerWithQuestion(
    @Embedded val answer: Answer,
    @Relation(
        parentColumn = "questionId",
        entityColumn = "id"
    )
    val question: Question
)

/**
 * Relation entity, allowing more convenient queries
 */
data class QuizResultWithAnswers(
    @Embedded val result: QuizResult,

    @Relation(
        parentColumn = "quizId",
        entityColumn = "id"
    )
    val quiz: Quiz,

    @Relation(
        entity = Answer::class,
        parentColumn = "id",
        entityColumn = "resultId"
    )
    val answers: List<AnswerWithQuestion>
)
