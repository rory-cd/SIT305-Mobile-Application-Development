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
 * View model for [ResultsScreen]
 */
class ResultsViewModel(
    savedStateHandle: SavedStateHandle,
    private val quizRepo: QuizRepository,
    private val userRepo: UserRepository
) : ViewModel() {

    // UI state
    private val _uiState = MutableStateFlow(ResultsUiState())
    val uiState: StateFlow<ResultsUiState> = _uiState.asStateFlow()

    // Set the UI values
    init {
        viewModelScope.launch {
            // Get quiz id from route args
            val quizId: Int = checkNotNull(savedStateHandle["quizId"])

            // Get current quiz and user
            val quiz = quizRepo.getQuizById(quizId)
            val user = userRepo.getCurrentUser()
            val questions = quizRepo.getQuizQuestions(quizId)

            // User or quiz not found - show error
            if (user == null || quiz == null) {
                _uiState.update { it.copy(toastMessage = "Something went wrong - results could not be fetched.") }
                return@launch
            }

            // Get result
            val result = quizRepo.getResultForQuiz(quizId, user.id)
            if (result == null) {
                _uiState.update { it.copy(toastMessage = "Something went wrong - results could not be fetched.") }
                return@launch
            }

            // Get plan based on results
            val plan = if (result.incorrectQuestions.isEmpty()) {
                quizRepo.fetchPlanForExtension(result.correctQuestions)
            } else {
                quizRepo.fetchPlanForImprovement(result.incorrectQuestions)
            }

            // Update UI state
            _uiState.update {
                it.copy(
                    username = user.username,
                    correctAnswers = result.correctQuestions.size,
                    questionCount = questions.size,
                    feedback = plan
                )
            }
            // Mark as loaded
            _uiState.update { it.copy(isLoaded = true) }
        }
    }

    fun clearToast() {
        _uiState.update { it.copy(toastMessage = null) }
    }
}
