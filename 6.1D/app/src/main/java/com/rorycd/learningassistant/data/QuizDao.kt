package com.rorycd.learningassistant.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object for Room [Quiz] and [Question] entities
 */
@Dao
interface QuizDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertQuiz(quiz: Quiz)

    @Update
    suspend fun insertQuestions(questions: List<Question>)

    @Delete
    suspend fun deleteQuiz(quiz: Quiz)

    @Query("SELECT * FROM quizzes WHERE lastCompleted = NULL")
    suspend fun getIncompleteQuizzes(): List<Quiz>

    @Query("SELECT * FROM quizzes WHERE id = :id LIMIT 1")
    fun getQuizById(id: Int): Flow<Quiz?>
}
