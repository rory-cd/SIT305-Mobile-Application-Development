package com.rorycd.learningassistant.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rorycd.learningassistant.data.QuizRepository
import com.rorycd.learningassistant.data.UserRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * View model for [HomeScreen]
 */
class HomeViewModel(
    private val userRepo: UserRepository,
    private val quizRepo: QuizRepository
) : ViewModel() {

    val currentUser = userRepo.getCurrentUserFlow()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = null
        )

    val quizzes = quizRepo.getIncompleteQuizzesStream(userRepo.currentUserId())
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    // Generate new quizzes if required
    init {
        viewModelScope.launch {
            val user = userRepo.getCurrentUserFlow().filterNotNull().first()
            quizRepo.refillQuizzesForUser(user)
        }
    }

    fun logOut() {
        userRepo.logOut()
    }
}
