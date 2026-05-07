package com.rorycd.chatbot.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.rorycd.chatbot.R
import com.rorycd.chatbot.ui.chat.ChatDestination
import com.rorycd.chatbot.ui.chat.ChatScreen
import com.rorycd.chatbot.ui.components.LoadingSpinner
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

    if (isLoggedIn == null) {
        LoadingSpinner(stringResource(R.string.loading))
        return
    }

    fun navigateWithGuard(route : String, clearStack: Boolean = false) {
        if (isLoggedIn == false) {
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
        startDestination = if (isLoggedIn == false) LoginDestination.route else HomeDestination.route,
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
                onLogOut = {
                    viewModel.logOut()
                    navController.navigate(LoginDestination.route) { popUpTo(0) }
                }
            )
        }
        // Chat screen
        composable(
            route = ChatDestination.routeWithArgs,
            arguments = listOf(navArgument(ChatDestination.CONVERSATION_ID_ARG)
                { type = NavType.IntType }
            )
            ) {
            ChatScreen(
                onNavigateUp = { navController.navigateUp() },
                onLogOut = {
                    viewModel.logOut()
                    navController.navigate(LoginDestination.route) { popUpTo(0) }
                }
            )
        }
    }
}
