package com.rorycd.lostandfound.navigation


import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.rorycd.lostandfound.ui.create.CreateAdvertScreen
import com.rorycd.lostandfound.ui.create.CreateDestination
import com.rorycd.lostandfound.ui.details.DetailsDestination
import com.rorycd.lostandfound.ui.details.DetailsScreen
import com.rorycd.lostandfound.ui.home.HomeDestination
import com.rorycd.lostandfound.ui.home.HomeScreen
import com.rorycd.lostandfound.ui.itemlist.ItemListDestination
import com.rorycd.lostandfound.ui.itemlist.ItemListScreen

/**
 * Composable defining navigation routes for the main app
 */
@Composable
fun LostAndFoundNavHost (
    modifier: Modifier = Modifier
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = HomeDestination.route,
        modifier = modifier
    ) {
        // Home screen
        composable(route = HomeDestination.route) {
            HomeScreen(
                onCreateAdvert = { navController.navigate(CreateDestination.route) },
                onShowAllItems = { navController.navigate(ItemListDestination.route) },
                onShowOnMap = {}
            )
        }
        // Create advert screen
        composable(route = CreateDestination.route) {
            CreateAdvertScreen(
                onAdvertCreated = {
                    navController.navigate(
                        ItemListDestination.route
                    ){
                        popUpTo(HomeDestination.route) { inclusive = false }
                    }
                }
            )
        }
        // Item list screen
        composable(route = ItemListDestination.route) {
            ItemListScreen(
                onSelectItem = { navController.navigate("${DetailsDestination.route}/$it") }
            )
        }
        // Item details screen
        composable(route = DetailsDestination.routeWithArgs,
            arguments = listOf(navArgument(DetailsDestination.ITEM_ID_ARG) {
                type = NavType.IntType
            })
        ) {
            DetailsScreen(
                onDeleteItem = {
                    navController.navigate(
                        ItemListDestination.route
                    ){
                        popUpTo(HomeDestination.route) { inclusive = false }
                    }
                }
            )
        }
    }
}
