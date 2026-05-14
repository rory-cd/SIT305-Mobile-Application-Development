package com.rorycd.learningassistant.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

/**
 * Data Access Object for Room [QuizResult] entities
 */
@Dao
interface ResultDao {
    @Insert
    suspend fun insertResult(quizResult: QuizResult)

    @Update
    suspend fun updateResult(quizResult: QuizResult)

    @Query("DELETE FROM results WHERE quizId = :quizId AND userId = :userId")
    suspend fun deleteResultsForQuiz(quizId: Int, userId: Int)

    @Query("SELECT * FROM results WHERE quizId = :quizId AND userId = :userId LIMIT 1")
    suspend fun getResultForQuiz(quizId: Int, userId: Int): QuizResult?
}
