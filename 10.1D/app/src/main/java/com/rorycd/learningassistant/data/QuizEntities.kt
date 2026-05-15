package com.rorycd.learningassistant.data

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.Relation
import java.util.Date

/**
 * Room entity, models data for a question
 */
@Entity(
    tableName = "questions",
    foreignKeys = [
        ForeignKey(
            entity = Quiz::class,
            parentColumns = ["id"],
            childColumns = ["quizId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["quizId"])]
)
data class Question (
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val quizId: Int,
    val title: String,
    val options: List<String>,
    val answer: String
)

/**
 * Room entity, models data for a quiz
 */
@Entity(
    tableName = "quizzes",
    foreignKeys = [
        ForeignKey(
            entity = User::class,
            parentColumns = ["id"],
            childColumns = ["userId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["userId"])]
)
data class Quiz (
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val userId: Int,
    val topic: String,
    val lastCompleted: Date? = null
)

/**
 * Room entity, models data for quiz results
 */
@Entity(
    tableName = "results",
    foreignKeys = [
        ForeignKey(
            entity = User::class,
            parentColumns = ["id"],
            childColumns = ["userId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Quiz::class,
            parentColumns = ["id"],
            childColumns = ["quizId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["quizId"]), Index(value = ["userId"])]
)
data class QuizResult (
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val userId: Int,
    val quizId: Int,
    val score: Int,
    val maxScore: Int,
    val completeDate: Date
)

/**
 * Room entity, models data for a user's quiz answers
 */
@Entity(
    tableName = "answers",
    foreignKeys = [
        ForeignKey(
            entity = QuizResult::class,
            parentColumns = ["id"],
            childColumns = ["resultId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Question::class,
            parentColumns = ["id"],
            childColumns = ["questionId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["resultId"]), Index(value = ["questionId"])]
)
data class Answer (
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val resultId: Int,
    val questionId: Int,
    val selectedAnswer: String,
    val isCorrect: Boolean
)
