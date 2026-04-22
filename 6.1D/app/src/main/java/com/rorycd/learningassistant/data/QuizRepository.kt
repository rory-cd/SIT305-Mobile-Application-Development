package com.rorycd.learningassistant.data

import android.content.Context
import com.rorycd.learningassistant.network.QuizResponse
import com.rorycd.learningassistant.network.RetrofitInstance

class QuizRepository(private val context: Context) {
    suspend fun fetchQuiz(topic: String): QuizResponse {
        return RetrofitInstance.quizApiService.getQuiz(topic)
    }


}
