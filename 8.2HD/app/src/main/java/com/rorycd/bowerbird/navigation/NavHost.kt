package com.rorycd.bowerbird.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.rorycd.bowerbird.ui.rules.RulesScreen
import com.rorycd.bowerbird.ui.folders.FoldersScreen
import com.rorycd.bowerbird.ui.settings.SettingsScreen
import kotlinx.serialization.Serializable

// NavDestinations
@Serializable object RulesRoute
@Serializable object FoldersRoute
@Serializable object SettingsRoute

/**
 * Composable defining navigation routes for the main app content
 */
@Composable
fun BowerbirdNavHost (
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = RulesRoute,
        modifier = modifier
    ) {
        // Rules screen
        composable<RulesRoute> {
            RulesScreen()
        }
        // Folders screen
        composable<FoldersRoute> {
            FoldersScreen()
        }
        // Settings screen
        composable<SettingsRoute> {
            SettingsScreen()
        }
    }
}
