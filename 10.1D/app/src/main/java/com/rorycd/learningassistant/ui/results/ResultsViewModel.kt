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

            // User or quiz not found - show error
            if (user == null || quiz == null) {
                _uiState.update { it.copy(toastMessage = "Something went wrong - results could not be fetched.") }
                return@launch
            }

            // Get result
            val resultWithAnswers = quizRepo.getResultForQuiz(quizId, user.id)
            if (resultWithAnswers == null) {
                _uiState.update { it.copy(toastMessage = "Something went wrong - results could not be fetched.") }
                return@launch
            }

            // Get correct and incorrect answers with associated questions
            val (correctAnswers, incorrectAnswers) = resultWithAnswers.answers.partition {
                it.answer.isCorrect
            }

            val correctQuestions = correctAnswers.map { it.question.title }
            val incorrectQuestions = incorrectAnswers.map { it.question.title }

            // Get plan based on results
            val plan = if (incorrectQuestions.isEmpty()) {
                quizRepo.fetchPlanForExtension(correctQuestions)
            } else {
                quizRepo.fetchPlanForImprovement(incorrectQuestions)
            }

            // Update UI state
            _uiState.update {
                it.copy(
                    username = user.username,
                    correctAnswers = resultWithAnswers.result.score,
                    questionCount = resultWithAnswers.result.maxScore,
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
