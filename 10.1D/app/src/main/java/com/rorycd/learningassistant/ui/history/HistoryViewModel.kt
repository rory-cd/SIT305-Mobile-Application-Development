package com.rorycd.learningassistant.ui.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rorycd.learningassistant.data.QuizRepository
import com.rorycd.learningassistant.data.UserRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map

/**
 * View model for [HistoryScreen]
 */
class HistoryViewModel(
    private val userRepo: UserRepository,
    private val quizRepo: QuizRepository
) : ViewModel() {

    // User
    val currentUser = userRepo.getCurrentUserFlow()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), initialValue = null)

    // Results
    @OptIn(ExperimentalCoroutinesApi::class)
    val results = currentUser.flatMapLatest { user ->

        if (user == null) {
            flowOf(emptyList())
        } else {
            quizRepo.getResultsForUser(user.id)
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())
}
