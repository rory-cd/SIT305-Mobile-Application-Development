package com.rorycd.learningassistant.network

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

/**
 * Retrofit API endpoint definitions
 */
public interface QuizApiService {
    @GET("getQuiz")
    suspend fun getQuiz(
        @Query("topic") topic: String
    ): QuizResponse

    @POST("getFeedback")
    suspend fun getFeedback(
        @Body request: FeedbackRequest
    ): FeedbackResponse

    @POST("getExtension")
    suspend fun getExtension(
        @Body request: FeedbackRequest
    ): FeedbackResponse
}
