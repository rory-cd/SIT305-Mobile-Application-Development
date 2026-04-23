package com.rorycd.learningassistant.ui.quiz

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rorycd.learningassistant.data.Question
import com.rorycd.learningassistant.data.Quiz
import com.rorycd.learningassistant.data.QuizResult
import com.rorycd.learningassistant.data.QuizRepository
import com.rorycd.learningassistant.data.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Date

/**
 * View model for [QuizScreen]. Manages UI
 */
class QuizViewModel(
    savedStateHandle: SavedStateHandle,
    private val quizRepo: QuizRepository,
    private val userRepo: UserRepository
) : ViewModel() {

    private var allQuestions = emptyList<Question>()
    private val correctQuestions = mutableListOf<String>()
    private val incorrectQuestions = mutableListOf<String>()
    private var quiz: Quiz? = null

    private val _uiState = MutableStateFlow(QuizUiState())
    val uiState: StateFlow<QuizUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            val quizId: Int = checkNotNull(savedStateHandle["quizId"])
            quiz = quizRepo.getQuizById(quizId)
            if (quiz != null)
                allQuestions = quizRepo.getQuizQuestions(quiz!!.id)

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
            correctQuestions += _uiState.value.question
        } else {
            incorrectQuestions += _uiState.value.question
        }
        if (_uiState.value.questionNumber < _uiState.value.totalQuestions - 1) loadNextQuestion()
    }

    fun submitResult() {
        viewModelScope.launch {
            val userId = userRepo.getCurrentUser()?.id
            if (userId != null) {
                val quizResult = QuizResult (
                    userId = userId,
                    quizId = _uiState.value.quizId,
                    correctQuestions = correctQuestions,
                    incorrectQuestions = incorrectQuestions,
                )
                // Record completion time to mark as complete
                val updatedQuiz = Quiz(
                    quiz!!.id,
                    quiz!!.userId,
                    quiz!!.topic,
                    lastCompleted = Date()
                )
//                quizRepo.completeQuiz(updatedQuiz, quizResult)
            }
        }
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
