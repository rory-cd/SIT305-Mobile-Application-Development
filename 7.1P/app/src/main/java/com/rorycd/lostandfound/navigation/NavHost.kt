package com.rorycd.lostandfound.navigation


import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.rorycd.lostandfound.ui.create.CreateAdvertScreen
import com.rorycd.lostandfound.ui.create.CreateDestination
import com.rorycd.lostandfound.ui.home.HomeDestination
import com.rorycd.lostandfound.ui.home.HomeScreen
import com.rorycd.lostandfound.ui.itemlist.ItemListDestination
import com.rorycd.lostandfound.ui.itemlist.ItemListScreen

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
                onCreateAdvert = { navController.navigate(CreateDestination.route) },
                onShowAllItems = { navController.navigate(ItemListDestination.route) }
            )
        }
        // Create advert screen
        composable(route = CreateDestination.route) {
            CreateAdvertScreen(
                onAdvertCreated = { navController.navigate(ItemListDestination.route) }
            )
        }
        // Create advert screen
        composable(route = ItemListDestination.route) {
            ItemListScreen(
                onSelectItem = { navController.navigate(HomeDestination.route) }
            )
        }
    }
}
