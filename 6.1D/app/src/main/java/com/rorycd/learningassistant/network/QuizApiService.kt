package com.rorycd.learningassistant.network

import retrofit2.http.GET
import retrofit2.http.Query

public interface QuizApiService {
    @GET("getQuiz")
    suspend fun getQuiz(
        @Query("topic") topic: String
    ): QuizResponse
}
