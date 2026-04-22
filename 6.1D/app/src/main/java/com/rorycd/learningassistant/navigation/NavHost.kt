package com.rorycd.learningassistant.navigation

import android.app.Application
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.rorycd.learningassistant.LearningApplication
import com.rorycd.learningassistant.ui.home.HomeScreen
import com.rorycd.learningassistant.ui.interestselect.InterestSelectScreen
import com.rorycd.learningassistant.ui.register.RegisterScreen
import com.rorycd.learningassistant.ui.login.LoginScreen

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

    fun userNavigateTo(destination : Destination) {
        val finalDestination = if (userRepo.isLoggedIn()) destination else Destination.LOGIN
        navController.navigate(route = finalDestination.name)
    }

    fun guestNavigateTo(destination : Destination) {
        navController.navigate(route = destination.name)
    }

    NavHost(
        navController = navController,
        startDestination = if (!userRepo.isLoggedIn()) Destination.LOGIN.name else Destination.HOME.name,
        modifier = modifier
    ) {
        // Login screen
        composable(route = Destination.LOGIN.name) {
            LoginScreen(
                onLoginSuccess = { userNavigateTo(Destination.HOME) },
                onRequireRegistration = { guestNavigateTo(Destination.REGISTER) }
            )
        }
        // Register screen
        composable(route = Destination.REGISTER.name) {
            RegisterScreen(
                onRegistrationSuccess = { userNavigateTo(Destination.INTEREST_SELECTION) }
            )
        }
        // Home screen
        composable(route = Destination.HOME.name) {
            HomeScreen(
                onLogOut = { guestNavigateTo(Destination.LOGIN) }
            )
        }
        // Interest selection screen
        composable(route = Destination.INTEREST_SELECTION.name) {
            InterestSelectScreen(
                onFinishSelection = { userNavigateTo(Destination.HOME) }
            )
        }
    }
}
