package com.rorycd.eventplanner

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.rorycd.eventplanner.navigation.EventPlannerNavHost
import com.rorycd.eventplanner.ui.components.EventPlannerAppBar
import com.rorycd.eventplanner.ui.components.EventPlannerNavBar
import com.rorycd.eventplanner.ui.components.NavBarOption
import com.rorycd.eventplanner.ui.eventlist.EventListDestination

// Tracks the destinations used in the navbar
val topLevelDestinations = NavBarOption.entries.map { it.destination.route }

@Composable
fun EventPlannerApp(
    navController: NavHostController = rememberNavController()
) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = backStackEntry?.destination?.route

    Scaffold(
        contentWindowInsets = WindowInsets.safeDrawing,
        topBar = {
            EventPlannerAppBar(
//                currentDestination = currentDestination,
                canGoBack = currentDestination !in topLevelDestinations,
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
