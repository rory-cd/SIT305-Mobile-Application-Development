package com.rorycd.lostandfound.navigation


import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.rorycd.lostandfound.ui.home.HomeDestination
import com.rorycd.lostandfound.ui.home.HomeScreen

/**
 * Composable defining navigation routes for the main app content
 */
@Composable
fun LostAndFoundNavHost (
    modifier: Modifier = Modifier
) {
    val navController = rememberNavController()

//    fun navigateWithGuard(route : String, clearStack: Boolean = false) {
//        if (!userRepo.isLoggedIn()) {
//            // Redirect logged-out users
//            navController.navigate(LoginDestination.route) {
//                popUpTo(0) { inclusive = true }
//            }
//            return
//        }
//
//        navController.navigate(route) {
//            if (clearStack) {
//                popUpTo(0) { inclusive = true }
//            }
//            launchSingleTop = true
//        }
//    }

    NavHost(
        navController = navController,
        startDestination = HomeDestination.route,
        modifier = modifier
    ) {
        // Home screen
        composable(route = HomeDestination.route) {
            HomeScreen(
                onCreateAdvert = {},
                onShowAllItems = {}
            )
        }
//        // Register screen
//        composable(route = RegisterDestination.route) {
//            RegisterScreen(
//                onRegistrationSuccess = { navigateWithGuard(SelectInterestsDestination.route) }
//            )
//        }
//        // Home screen
//        composable(route = HomeDestination.route) {
//            HomeScreen(
//                onLogOut = { navController.navigate(LoginDestination.route) { popUpTo(0) } },
//                onStartQuiz = { navigateWithGuard("${QuizDestination.route}/$it") },
//                onPickInterests = { navigateWithGuard(SelectInterestsDestination.route) }
//            )
//        }
//        // Interest selection screen
//        composable(route = SelectInterestsDestination.route) {
//            InterestSelectScreen(
//                onFinishSelection = { navigateWithGuard(HomeDestination.route, true) }
//            )
//        }
//        // Quiz screen
//        composable(
//            route = QuizDestination.routeWithArgs,
//            arguments = listOf(navArgument(QuizDestination.QUIZ_ID_ARG) {
//                type = NavType.IntType
//            })
//        ) {
//            QuizScreen(
//                onQuizComplete = {
//                    navigateWithGuard(route = "${ResultsDestination.route}/$it")
//                }
//            )
//        }
//        // Results screen
//        composable(
//            route = ResultsDestination.routeWithArgs,
//            arguments = listOf(navArgument(ResultsDestination.QUIZ_ID_ARG)
//            { type = NavType.IntType }
//            )
//        ) {
//            ResultsScreen(
//                onGoHome = { navigateWithGuard(HomeDestination.route, true) }
//            )
//        }
    }
}
