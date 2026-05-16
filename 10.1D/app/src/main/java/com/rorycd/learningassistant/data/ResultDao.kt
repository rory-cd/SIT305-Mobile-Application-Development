package com.rorycd.learningassistant.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object for Room [QuizResult] and [Answer] entities
 */
@Dao
interface ResultDao {
    @Insert
    suspend fun insertResult(quizResult: QuizResult) : Long

    @Insert
    suspend fun insertAnswers(answers: List<Answer>)

    @Update
    suspend fun updateResult(quizResult: QuizResult)

    @Query("DELETE FROM results WHERE quizId = :quizId AND userId = :userId")
    suspend fun deleteResultsForQuiz(quizId: Int, userId: Int)

    @Transaction
    @Query("SELECT * FROM results WHERE userId = :userId ORDER BY completeDate DESC")
    fun getResultsForUser(userId: Int): Flow<List<QuizResultWithAnswers>>

    @Transaction
    @Query("SELECT * FROM results WHERE quizId = :quizId AND userId = :userId LIMIT 1")
    suspend fun getResultForQuiz(quizId: Int, userId: Int): QuizResultWithAnswers?
}
