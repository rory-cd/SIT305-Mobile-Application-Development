package com.rorycd.learningassistant.ui.results

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rorycd.learningassistant.data.QuizRepository
import com.rorycd.learningassistant.data.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * View model for [ResultsScreen]. Manages UI
 */
class ResultsViewModel(
    savedStateHandle: SavedStateHandle,
    private val quizRepo: QuizRepository,
    private val userRepo: UserRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ResultsUiState())
    val uiState: StateFlow<ResultsUiState> = _uiState.asStateFlow()

    // Set the UI values
    init {
        viewModelScope.launch {
            val quizId: Int = checkNotNull(savedStateHandle["quizId"])
            val quiz = quizRepo.getQuizById(quizId)
            val user = userRepo.getCurrentUser()
            if (user != null && quiz != null) {
                val result = quizRepo.getResultForQuiz(quizId, user.id)
                val allQuestions = quizRepo.getQuizQuestions(quiz.id)

                if (result != null) {
                    val plan = if (result.incorrectQuestions.isEmpty()) {
                        quizRepo.fetchPlanForExtension(result.correctQuestions)
                    } else {
                        quizRepo.fetchPlanForImprovement(result.incorrectQuestions)
                    }
                    _uiState.update {
                        it.copy(
                            username = user.username,
                            correctAnswers = result.correctQuestions.size,
                            questionCount = allQuestions.size,
                            feedback = plan
                        )
                    }
                }
            } else {
                // Error
            }
            // Mark as loaded
            _uiState.update { it.copy(isLoaded = true) }
        }
    }
}
