package com.rorycd.learningassistant.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object for Room [Quiz] and [Question] entities
 */
@Dao
interface QuizDao {
    @Transaction
    suspend fun insertFullQuiz(quiz: Quiz, questions: List<Question>) {
        val id = insertQuizOnly(quiz)
        val questionsWithId = questions.map { it.copy(quizId = id.toInt()) }
        insertQuestions(questionsWithId)
    }

    @Update
    suspend fun updateQuiz(quiz: Quiz)

    @Insert
    suspend fun insertQuizOnly(quiz: Quiz) : Long

    @Insert
    suspend fun insertQuestions(questions: List<Question>)

    @Delete
    suspend fun deleteQuiz(quiz: Quiz)

    @Query("SELECT * FROM quizzes WHERE userId = :userId AND lastCompleted IS NULL")
    suspend fun getIncompleteQuizzes(userId: Int): List<Quiz>

    @Query("SELECT * FROM questions WHERE quizId = :quizId")
    suspend fun getQuestionsForQuiz(quizId: Int): List<Question>

    @Query("SELECT * FROM quizzes WHERE userId = :userId AND lastCompleted IS NULL")
    fun getIncompleteQuizFlow(userId: Int): Flow<List<Quiz>>

    @Query("SELECT * FROM quizzes WHERE id = :id LIMIT 1")
    suspend fun getQuizById(id: Int): Quiz?
}
