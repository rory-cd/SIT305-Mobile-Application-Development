package com.rorycd.learningassistant.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.rorycd.learningassistant.ui.WelcomeScreen

/**
 * Composable defining navigation routes for the main app content
 */
@Composable
fun LearningAssistantNavHost (
    modifier: Modifier = Modifier
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Destination.WELCOME.name,
        modifier = modifier
    ) {
        // Welcome screen
        composable(route = Destination.WELCOME.name) {
            WelcomeScreen(
                onLogin = {
                    navController.navigate(route = Destination.HOME.name)
                },
                onRegister = {
                    navController.navigate(route = Destination.REGISTER.name)
                }
            )
        }
    }
}
