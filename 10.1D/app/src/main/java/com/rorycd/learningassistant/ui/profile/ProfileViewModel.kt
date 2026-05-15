package com.rorycd.learningassistant.ui.profile

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
 * View model for [ProfileScreen]
 */
class ProfileViewModel(
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

    // Average score
    val averageScore = results.map { results ->

        if (results.isEmpty()) return@map 0

        val totalScorePercentages = results.sumOf {
            (it.result.score.toDouble() / it.result.maxScore) * 100
        }
        (totalScorePercentages / results.size).toInt()

    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), 0)

    // Scores by topic
    val topicScores = results.map { results ->
        if (results.isEmpty()) return@map emptyMap()

        // Group by topic ("Kotlin": QuizResultWithAnswers, QuizResultWithAnswers)
        val groupedResults = results.groupBy { it.quiz.topic }

        // Get scores for each attempt ("Kotlin": 53%, 68%)
        val groupedScores = groupedResults.mapValues { (_, attempts) ->
            attempts.map { (it.result.score.toDouble() / it.result.maxScore) * 100 }
        }
        // Get average score for each topic ("Kotlin": 58%)
        groupedScores.mapValues { (_, scores) -> scores.average().toInt() }

    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyMap())

    // Top 3 scoring topics
    val topTopics = topicScores.map { topicScores ->
        if (topicScores.isEmpty()) return@map emptyList()

        val sortedList = topicScores.toList().sortedByDescending { it.second }
        sortedList.take(3)

    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    // Bottom 3 scoring topics
    val bottomTopics = topicScores.map { topicScores ->
        if (topicScores.isEmpty()) return@map emptyList()

        val sortedList = topicScores.toList().sortedBy { it.second }
        sortedList.take(3)

    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    fun logOut() {
        userRepo.logOut()
    }
}
