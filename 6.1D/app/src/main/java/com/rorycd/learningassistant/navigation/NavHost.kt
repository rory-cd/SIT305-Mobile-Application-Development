package com.rorycd.learningassistant.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.rorycd.learningassistant.LearningApplication
import com.rorycd.learningassistant.ui.home.HomeDestination
import com.rorycd.learningassistant.ui.home.HomeScreen
import com.rorycd.learningassistant.ui.interestselect.InterestSelectScreen
import com.rorycd.learningassistant.ui.interestselect.SelectInterestsDestination
import com.rorycd.learningassistant.ui.login.LoginDestination
import com.rorycd.learningassistant.ui.register.RegisterScreen
import com.rorycd.learningassistant.ui.login.LoginScreen
import com.rorycd.learningassistant.ui.quiz.QuizDestination
import com.rorycd.learningassistant.ui.quiz.QuizScreen
import com.rorycd.learningassistant.ui.register.RegisterDestination
import com.rorycd.learningassistant.ui.results.ResultsDestination
import com.rorycd.learningassistant.ui.results.ResultsScreen

/**
 * Composable defining navigation routes for the main app content
 */
@Composable
fun LearningAssistantNavHost (
    modifier: Modifier = Modifier
) {
    val navController = rememberNavController()
    val app = LocalContext.current.applicationContext as LearningApplication
    val userRepo = app.container.userRepo

    fun userNavigateTo(route : String) {
        if (userRepo.isLoggedIn()) {
            navController.navigate(route)
        } else {
            navController.navigate(LoginDestination.route) {
                popUpTo(0)
            }
        }
    }

    fun guestNavigateTo(route : String) {
        navController.navigate(route = route)
    }

    NavHost(
        navController = navController,
        startDestination = if (!userRepo.isLoggedIn()) LoginDestination.route else HomeDestination.route,
        modifier = modifier
    ) {
        // Login screen
        composable(route = LoginDestination.route) {
            LoginScreen(
                onLoginSuccess = { userNavigateTo(HomeDestination.route) },
                onRequireRegistration = { guestNavigateTo(RegisterDestination.route) }
            )
        }
        // Register screen
        composable(route = RegisterDestination.route) {
            RegisterScreen(
                onRegistrationSuccess = { userNavigateTo(SelectInterestsDestination.route) }
            )
        }
        // Home screen
        composable(route = HomeDestination.route) {
            HomeScreen(
                onLogOut = { guestNavigateTo(LoginDestination.route) },
                onStartQuiz = { userNavigateTo("${QuizDestination.route}/$it") },
                onPickInterests = { userNavigateTo(SelectInterestsDestination.route) }
            )
        }
        // Interest selection screen
        composable(route = SelectInterestsDestination.route) {
            InterestSelectScreen(
                onFinishSelection = { userNavigateTo(HomeDestination.route) }
            )
        }
        // Quiz screen
        composable(
            route = QuizDestination.routeWithArgs,
            arguments = listOf(navArgument(QuizDestination.QUIZ_ID_ARG) {
                type = NavType.IntType
            })
        ) {
            QuizScreen(
                onQuizComplete = { userNavigateTo("${ResultsDestination.route}/$it") }
            )
        }
        // Results screen
        composable(
            route = ResultsDestination.routeWithArgs,
            arguments = listOf(navArgument(ResultsDestination.QUIZ_ID_ARG)
                { type = NavType.IntType }
            )
        ) {
            ResultsScreen(
                onGoHome = { userNavigateTo(HomeDestination.route) }
            )
        }
    }
}
