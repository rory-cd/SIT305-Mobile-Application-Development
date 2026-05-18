package com.rorycd.bowerbird.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.rorycd.bowerbird.ui.editrule.EditRuleScreen
import com.rorycd.bowerbird.ui.rules.RulesScreen
import com.rorycd.bowerbird.ui.folders.FoldersScreen
import com.rorycd.bowerbird.ui.newrule.NewRuleScreen
import com.rorycd.bowerbird.ui.settings.SettingsScreen
import kotlinx.serialization.Serializable

// NavDestinations
@Serializable object RulesRoute
@Serializable object NewRuleRoute
@Serializable data class EditRuleRoute(val ruleId: Int)
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
            RulesScreen(
                onSelectRule = {
                    navController.navigate(EditRuleRoute(it)) {
                        launchSingleTop = true
                    }
                }
            )
        }
        // New rule screen
        composable<NewRuleRoute> {
            NewRuleScreen(
                onAddRule = {
                    // To rules list
                    navController.navigate(RulesRoute) {
                        popUpTo(navController.graph.findStartDestination().id)
                        launchSingleTop = true
                    }
                }
            )
        }
        // Edit rule screen
        composable<EditRuleRoute> {
            EditRuleScreen(
                onSaveRule = {
                    // To rules list
                    navController.navigate(RulesRoute) {
                        popUpTo(navController.graph.findStartDestination().id)
                        launchSingleTop = true
                    }
                },
                onDeleteRule = {
                    // To rules list
                    navController.navigate(RulesRoute) {
                        popUpTo(navController.graph.findStartDestination().id)
                        launchSingleTop = true
                    }
                }
            )
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
