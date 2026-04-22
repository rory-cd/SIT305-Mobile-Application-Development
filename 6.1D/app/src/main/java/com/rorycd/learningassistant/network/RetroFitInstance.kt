package com.rorycd.learningassistant.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.getValue
import kotlin.jvm.java

object RetrofitInstance {
    private const val BASE_URL = "http://192.168.0.2:5000/"

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val quizApiService: QuizApiService by lazy {
        retrofit.create(QuizApiService::class.java)
    }
}
