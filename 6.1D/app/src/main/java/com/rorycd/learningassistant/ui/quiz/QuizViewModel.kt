package com.rorycd.learningassistant.ui.quiz

import androidx.compose.runtime.MutableState
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rorycd.learningassistant.data.Question
import com.rorycd.learningassistant.data.QuizRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * View model for [QuizScreen]. Manages UI
 */
class QuizViewModel(
    savedStateHandle: SavedStateHandle,
    private val quizRepo: QuizRepository
) : ViewModel() {

    private var allQuestions = emptyList<Question>()

    private val _uiState = MutableStateFlow(QuizUiState())
    val uiState: StateFlow<QuizUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            val quizId: Int = checkNotNull(savedStateHandle["quizId"])
            val quiz = quizRepo.getQuizById(quizId)
            if (quiz != null)
                allQuestions = quizRepo.getQuizQuestions(quiz.id)

            // Set initial ui state values
            _uiState.update {
                it.copy(
                    quizId = quizId,
                    totalQuestions = allQuestions.size
                )
            }
            loadNextQuestion()
        }
    }

    fun submitAnswer(answer: Int) {
        val answerAsString = (65 + answer).toChar().toString()
        val questionIdx = _uiState.value.questionNumber
        val answerCorrect = answerAsString == allQuestions[questionIdx].answer
        if (answerCorrect) {
            val newCorrectAnswers = _uiState.value.correctAnswers + 1
            _uiState.update { it.copy(correctAnswers = newCorrectAnswers) }
        }
        loadNextQuestion()
    }

    private fun loadNextQuestion() {
        _uiState.update {
            val nextQuestion = it.questionNumber + 1
            it.copy(
                question = allQuestions[nextQuestion].title,
                options = allQuestions[nextQuestion].options,
                questionNumber = nextQuestion
            )
        }
    }
}
