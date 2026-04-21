package com.rorycd.learningassistant.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.rorycd.learningassistant.data.UserRepository
import com.rorycd.learningassistant.ui.HomeScreen
import com.rorycd.learningassistant.ui.InterestSelectScreen
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

    fun userNavigateTo(destination : Destination) {
        val finalDestination = if (repo.isLoggedIn()) destination else Destination.LOGIN
        navController.navigate(route = finalDestination.name)
    }

    fun guestNavigateTo(destination : Destination) {
        navController.navigate(route = destination.name)
    }

    NavHost(
        navController = navController,
        startDestination = if (!repo.isLoggedIn()) Destination.LOGIN.name else Destination.HOME.name,
        modifier = modifier
    ) {
        // Login screen
        composable(route = Destination.LOGIN.name) {
            LoginScreen(
                repo = repo,
                onLoginSuccess = { userNavigateTo(Destination.HOME) },
                onRequireRegistration = { guestNavigateTo(Destination.REGISTER) }
            )
        }
        // Register screen
        composable(route = Destination.REGISTER.name) {
            RegisterScreen(
                repo = repo,
                onRegistrationSuccess = { userNavigateTo(Destination.INTEREST_SELECTION) }
            )
        }
        // Home screen
        composable(route = Destination.HOME.name) {
            HomeScreen(
                onLogOut = { guestNavigateTo(Destination.LOGIN) },
                repo = repo
            )
        }
        // Interest selection screen
        composable(route = Destination.INTEREST_SELECTION.name) {
            InterestSelectScreen(
                onFinishSelection = { userNavigateTo(Destination.HOME) },
                repo = repo
            )
        }
    }
}
