package com.rorycd.learningassistant.ui

import android.app.Application
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.rorycd.learningassistant.LearningApplication
import com.rorycd.learningassistant.ui.home.HomeViewModel
import com.rorycd.learningassistant.ui.interestselect.InterestSelectViewModel
import com.rorycd.learningassistant.ui.login.LoginViewModel
import com.rorycd.learningassistant.ui.quiz.QuizViewModel
import com.rorycd.learningassistant.ui.register.RegisterViewModel

/**
 * Provides Factory to create instance of ViewModel for the entire app
 */
object AppViewModelProvider {
    val factory = viewModelFactory {
        // Initializer for LoginViewModel
        initializer {
            LoginViewModel(
                learningApplication().container.userRepo
            )
        }
        // Initializer for RegisterViewModel
        initializer {
            RegisterViewModel(
                learningApplication().container.userRepo
            )
        }
        // Initializer for InterestSelectViewModel
        initializer {
            InterestSelectViewModel(
                learningApplication().container.userRepo
            )
        }
        // Initializer for HomeViewModel
        initializer {
            HomeViewModel(
                userRepo = learningApplication().container.userRepo,
                quizRepo = learningApplication().container.quizRepo
            )
        }
        // Initializer for QuizViewModel
        initializer {
            QuizViewModel(
                createSavedStateHandle(),
                learningApplication().container.quizRepo
            )
        }
    }
}

/**
 * Extension function to queries for [Application] object and returns an instance of
 * [LearningApplication].
 */
fun CreationExtras.learningApplication(): LearningApplication =
    (this[AndroidViewModelFactory.APPLICATION_KEY] as LearningApplication)
