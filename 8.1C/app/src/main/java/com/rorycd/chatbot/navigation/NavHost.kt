package com.rorycd.chatbot.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.rorycd.chatbot.ui.login.LoginDestination
import com.rorycd.chatbot.ui.login.LoginScreen
import com.rorycd.chatbot.ui.register.RegisterScreen
import com.rorycd.chatbot.ui.register.RegisterDestination

/**
 * Composable defining navigation routes for the main app content
 */
@Composable
fun ChatBotNavHost (
    modifier: Modifier = Modifier,
    viewModel: NavViewModel = hiltViewModel()
) {
    val navController = rememberNavController()
    val isLoggedIn by viewModel.isLoggedIn.collectAsStateWithLifecycle(false)

    fun navigateWithGuard(route : String, clearStack: Boolean = false) {
        if (isLoggedIn) {
            // Redirect logged-out users
            navController.navigate(LoginDestination.route) {
                popUpTo(0) { inclusive = true }
            }
            return
        }

        navController.navigate(route) {
            if (clearStack) {
                popUpTo(0) { inclusive = true }
            }
            launchSingleTop = true
        }
    }

    NavHost(
        navController = navController,
        startDestination = LoginDestination.route,
//        startDestination = if (!isLoggedIn) LoginDestination.route else ChatDestination.route,
        modifier = modifier
    ) {
        // Login screen
        composable(route = LoginDestination.route) {
            LoginScreen(
                onLoginSuccess = { /*navigateWithGuard(route = ChatDestination.route, true)*/},
                onRequireRegistration = { navController.navigate(RegisterDestination.route) }
            )
        }
        // Register screen
        composable(route = RegisterDestination.route) {
            RegisterScreen(
                onRegistrationSuccess = { /* navigateWithGuard(ChatDestination.route) */ }
            )
        }
//        // Chat screen
//        composable(route = HomeDestination.route) {
//            HomeScreen(
//                onLogOut = { navController.navigate(LoginDestination.route) { popUpTo(0) } },
//                onChat = { navigateWithGuard("${QuizDestination.route}/$it") }
//            )
//        }
    }
}
