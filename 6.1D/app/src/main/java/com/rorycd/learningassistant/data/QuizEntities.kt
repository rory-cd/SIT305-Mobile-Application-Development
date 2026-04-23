package com.rorycd.learningassistant.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
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
    val score: Int,
    val lastCompleted: Date? = null
)
