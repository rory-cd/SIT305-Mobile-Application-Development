package com.rorycd.learningassistant.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.rorycd.learningassistant.data.UserRepository
import com.rorycd.learningassistant.ui.HomeScreen
import com.rorycd.learningassistant.ui.RegisterScreen
import com.rorycd.learningassistant.ui.LoginScreen

/**
 * Composable defining navigation routes for the main app content
 */
@Composable
fun LearningAssistantNavHost (
    repo: UserRepository,
    modifier: Modifier = Modifier
) {
    val navController = rememberNavController()

    fun navigateTo(destination : Destination) {
        val finalDestination = if (repo.isLoggedIn()) destination else Destination.LOGIN
        navController.navigate(route = finalDestination.name)
    }

    NavHost(
        navController = navController,
        startDestination = if (repo.isLoggedIn()) Destination.LOGIN.name else Destination.HOME.name,
        modifier = modifier
    ) {
        // Login screen
        composable(route = Destination.LOGIN.name) {
            LoginScreen(
                repo = repo,
                onLoginSuccess = { navigateTo(Destination.HOME) },
                onRequireRegistration = { navigateTo(Destination.REGISTER) }
            )
        }
        // Register screen
        composable(route = Destination.REGISTER.name) {
            RegisterScreen(
                repo = repo,
                onRegistrationSuccess = { navigateTo(Destination.HOME) }
            )
        }
        // Home screen
        composable(route = Destination.HOME.name) {
            HomeScreen(
                repo = repo
            )
        }
    }
}
