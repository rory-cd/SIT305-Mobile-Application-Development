package com.rorycd.learningassistant.data

import androidx.room.RoomDatabase
import androidx.room.withTransaction
import com.rorycd.learningassistant.network.FeedbackRequest
import com.rorycd.learningassistant.network.QuizResponse
import com.rorycd.learningassistant.network.RetrofitInstance
import com.rorycd.learningassistant.network.toRoomEntities
import kotlinx.coroutines.flow.Flow

const val MAX_QUIZZES = 3

/**
 * Repository for managing [Quiz], [Question], and [QuizResult] data
 */
class QuizRepository(
    private val database: RoomDatabase,
    private val quizDao: QuizDao,
    private val resultDao: ResultDao
) {

    // Fetches a quiz from the API (Llama)
    suspend fun fetchQuiz(topic: String): QuizResponse {
        val result = RetrofitInstance.quizApiService.getQuiz(topic)
        return result
    }

    // Fetches an improvement plan from the API (Llama)
    suspend fun fetchPlanForImprovement(questions: List<String>): String {
        val questionsString = questions.toString()
        val request = FeedbackRequest(questions = questionsString)
        val response = RetrofitInstance.quizApiService.getFeedback(request)
        return response.feedback
    }

    // Fetches an extension plan from the API (Llama)
    suspend fun fetchPlanForExtension(questions: List<String>): String {
        val questionsString = questions.toString()
        val request = FeedbackRequest(questions = questionsString)
        val response = RetrofitInstance.quizApiService.getExtension(request)
        return response.feedback
    }

    // Generates user-assigned quizzes if the user has less than the defined number
    suspend fun refillQuizzesForUser(user: User) {
        if (user.interests.isNullOrEmpty()) return

        val incompleteQuizzes = quizDao.getIncompleteQuizzes(user.id)

        // Check how many quizzes are required
        val required = MAX_QUIZZES - incompleteQuizzes.size

        repeat (required) {
            val topic = user.interests.random()
            // Generate a quiz with AI
            val quizResponse = fetchQuiz(topic)
            // Convert to room format
            val (quiz, questions) = quizResponse.toRoomEntities(user.id, topic)
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

    suspend fun getResultForQuiz(quizId: Int, userId: Int): QuizResult? {
        return resultDao.getResultForQuiz(quizId, userId)
    }

    // Database transaction to make calls to two DAOs
    suspend fun completeQuiz(quiz: Quiz, result: QuizResult) {
        database.withTransaction {
            quizDao.updateQuiz(quiz)
            resultDao.insertResult(result)
        }
    }
}
