package com.rorycd.learningassistant.data

import android.content.Context
import android.util.Log
import com.rorycd.learningassistant.network.QuizResponse
import com.rorycd.learningassistant.network.RetrofitInstance
import com.rorycd.learningassistant.network.toRoomEntities
import kotlinx.coroutines.flow.Flow

class QuizRepository(private val quizDao: QuizDao, private val context: Context) {
    suspend fun fetchQuiz(topic: String): QuizResponse {
        val result = RetrofitInstance.quizApiService.getQuiz(topic)
        Log.e("QUIZ", result.quiz.toString())
        return result
    }

    suspend fun refillQuizzesForUser(user: User) {
        if (user.interests.isNullOrEmpty()) return

        val incompleteQuizzes = quizDao.getIncompleteQuizzes(user.id)
        val required = 1 - incompleteQuizzes.size

        repeat (required) {
            val topic = user.interests.random()
            // Generate a quiz with AI
            val quizResponse = fetchQuiz(topic)
            // Convert to room format
            val (quiz, questions) = quizResponse.toRoomEntities(user.id, topic)
            Log.e("QUIZ", questions.toString())
            // Add to database
            quizDao.insertFullQuiz(quiz, questions)
        }
    }

    suspend fun getQuizById(id: Int): Quiz? {
        return quizDao.getQuizById(id)
    }

    suspend fun getQuizQuestions(quizId: Int): List<Question> {
        return quizDao.getQuestionsForQuiz(quizId)
    }

    fun getIncompleteQuizzesStream(userId: Int): Flow<List<Quiz?>> {
        return quizDao.getIncompleteQuizFlow(userId)
    }
}
