package com.rorycd.chatbot.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.rorycd.chatbot.ui.chat.ChatDestination
import com.rorycd.chatbot.ui.chat.ChatScreen
import com.rorycd.chatbot.ui.home.HomeDestination
import com.rorycd.chatbot.ui.home.HomeScreen
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
        if (!isLoggedIn) {
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
        startDestination = if (!isLoggedIn) LoginDestination.route else HomeDestination.route,
        modifier = modifier
    ) {
        // Login screen
        composable(route = LoginDestination.route) {
            LoginScreen(
                onLoginSuccess = { navigateWithGuard(route = HomeDestination.route, true) },
                onRequireRegistration = { navController.navigate(RegisterDestination.route) }
            )
        }
        // Register screen
        composable(route = RegisterDestination.route) {
            RegisterScreen(
                onRegistrationSuccess = { navigateWithGuard(route = HomeDestination.route) }
            )
        }
        // Home screen
        composable(route = HomeDestination.route) {
            HomeScreen(
                onSelectConversation = { navigateWithGuard("${ChatDestination.route}/$it") },
                onLogOut = { navController.navigate(LoginDestination.route) { popUpTo(0) } }
            )
        }
        // Chat screen
        composable(
            route = ChatDestination.routeWithArgs,
            arguments = listOf(navArgument(ChatDestination.CONVERSATION_ID_ARG)
                { type = NavType.IntType }
            )
            ) {
            ChatScreen()
        }
    }
}
