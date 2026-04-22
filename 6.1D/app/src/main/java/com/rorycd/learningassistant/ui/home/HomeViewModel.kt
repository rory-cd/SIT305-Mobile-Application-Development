package com.rorycd.learningassistant.ui.home

import androidx.lifecycle.ViewModel
import com.rorycd.learningassistant.data.QuizRepository
import com.rorycd.learningassistant.data.UserRepository

/**
 * View model for [HomeScreen]. Manages UI
 */
class HomeViewModel(
    private val userRepo: UserRepository,
    private val quizRepo: QuizRepository
) : ViewModel() {

}
