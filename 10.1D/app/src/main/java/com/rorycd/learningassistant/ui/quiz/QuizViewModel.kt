package com.rorycd.learningassistant.ui.quiz

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rorycd.learningassistant.data.Answer
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
 * View model for [QuizScreen]
 */
class QuizViewModel(
    savedStateHandle: SavedStateHandle,
    private val quizRepo: QuizRepository,
    private val userRepo: UserRepository
) : ViewModel() {

    private var _allQuestions = emptyList<Question>()
    private val _answers = mutableMapOf<Int, String>()
    private var _quiz: Quiz? = null

    // UI state
    private val _uiState = MutableStateFlow(QuizUiState())
    val uiState: StateFlow<QuizUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            // Get quiz id from route args
            val quizId: Int = checkNotNull(savedStateHandle["quizId"])

            _quiz = quizRepo.getQuizById(quizId)

            if (_quiz == null) {
                _uiState.update { it.copy(toastMessage = "Something went wrong - results could not be fetched.") }
                return@launch
            }

            _allQuestions = quizRepo.getQuizQuestions(_quiz!!.id)

            // Set initial ui state values
            _uiState.update {
                it.copy(
                    quizId = quizId,
                    totalQuestions = _allQuestions.size
                )
            }
            loadNextQuestion()
        }
    }

    fun submitAnswer(answer: Int) {
        val answerAsString = (65 + answer).toChar().toString()
        val questionIdx = _uiState.value.questionNumber
        val question = _allQuestions[questionIdx]

        // Record answer
        _answers[question.id] = answerAsString
        if (question.answer == answerAsString) {
            _uiState.update { it.copy(currentScore = it.currentScore + 1) }
        }

        if (_uiState.value.questionNumber < _uiState.value.totalQuestions - 1) loadNextQuestion()
    }

    fun submitResult() {
        viewModelScope.launch {
            val userId = userRepo.getCurrentUser()?.id
            if (userId != null) {
                val completeDate = Date()

                // Create result
                val quizResult = QuizResult (
                    userId = userId,
                    quizId = _uiState.value.quizId,
                    score = _uiState.value.currentScore,
                    maxScore = _allQuestions.size,
                    completeDate = completeDate
                )
                // Map answers to entities
                val answerEntities = _answers.map { (qId, selectedAnswer) ->
                    val question = _allQuestions.find { it.id == qId }
                        ?: throw IllegalStateException("Question with ID $qId not found")

                    Answer(
                        resultId = 0,
                        questionId = qId,
                        selectedAnswer = selectedAnswer,
                        isCorrect = question.answer == selectedAnswer
                    )
                }
                // Record completion time to mark as complete
                val updatedQuiz = Quiz(
                    _quiz!!.id,
                    _quiz!!.userId,
                    _quiz!!.topic,
                    lastCompleted = completeDate
                )
                quizRepo.completeQuiz(updatedQuiz, quizResult, answerEntities)
            }
        }
    }

    private fun loadNextQuestion() {
        _uiState.update {
            val nextQuestion = it.questionNumber + 1
            it.copy(
                question = _allQuestions[nextQuestion].title,
                options = _allQuestions[nextQuestion].options,
                questionNumber = nextQuestion
            )
        }
    }
    fun clearToast() {
        _uiState.update { it.copy(toastMessage = null) }
    }
}
