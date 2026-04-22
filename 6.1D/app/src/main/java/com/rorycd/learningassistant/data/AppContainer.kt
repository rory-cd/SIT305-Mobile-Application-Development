package com.rorycd.learningassistant.data

import android.content.Context

/**
 * App container for dependency injection of [UserRepository] and [QuizRepository]
 */
class AppContainer(private val context: Context) {
    val userRepo: UserRepository by lazy {
        UserRepository(AppDatabase.getDatabase(context).userDao(), context)
    }
    val quizRepo: QuizRepository by lazy {
        QuizRepository(context)
    }
}
