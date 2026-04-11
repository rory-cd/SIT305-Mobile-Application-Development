package com.rorycd.eventplanner

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.rorycd.eventplanner.navigation.EventPlannerNavHost
import com.rorycd.eventplanner.ui.components.EventPlannerAppBar
import com.rorycd.eventplanner.ui.components.EventPlannerNavBar
import com.rorycd.eventplanner.ui.components.NavBarOption
import com.rorycd.eventplanner.ui.editevent.EditEventDestination
import com.rorycd.eventplanner.ui.eventlist.EventListDestination
import com.rorycd.eventplanner.ui.newevent.NewEventDestination

// Destinations used in the navbar
val topLevelRoutes = NavBarOption.entries.map { it.destination.route }

/**
 * Composable defining app UI structure
 */
@Composable
fun EventPlannerApp(
    navController: NavHostController = rememberNavController()
) {
    // Get the current route from the nav controller
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route

    // List all destinations
    val allDestinations = listOf(
        EventListDestination,
        NewEventDestination,
        EditEventDestination
    )

    // Get the current destination (route matches OR start of route matches)
    val currentDestination = allDestinations.find { currentRoute == it.route }
        ?: allDestinations.find { currentRoute?.startsWith(it.route) == true }

    Scaffold(
        contentWindowInsets = WindowInsets.safeDrawing,
        topBar = {
            EventPlannerAppBar(
                title = currentDestination?.titleRes?.let { stringResource(it) } ?: "",
                canGoBack = currentRoute !in topLevelRoutes,
                navigateUp = { navController.navigateUp() }
            )
        },
        bottomBar = {
            EventPlannerNavBar(
                onSelectNavOption = {
                    navController.navigate(route = it) {
                        popUpTo(EventListDestination.route) {
                            inclusive = false
                        }
                        launchSingleTop = true
                    }
                }
            )
        }
    ) { innerPadding ->
        EventPlannerNavHost(
            navController = navController,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        )
    }
}
