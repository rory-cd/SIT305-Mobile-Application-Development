package com.rorycd.eventplanner.navigation

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.rorycd.eventplanner.ui.editevent.EditEventDestination
import com.rorycd.eventplanner.ui.editevent.EditEventScreen
import com.rorycd.eventplanner.ui.eventlist.EventListDestination
import com.rorycd.eventplanner.ui.eventlist.EventListScreen
import com.rorycd.eventplanner.ui.newevent.NewEventDestination
import com.rorycd.eventplanner.ui.newevent.NewEventScreen

/**
 * Composable defining navigation routes for the main app content
 */
@Composable
fun EventPlannerNavHost (
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = EventListDestination.route,
        modifier = modifier
    ) {
        // Event list screen
        composable(route = EventListDestination.route) {
            EventListScreen(
                onSelectEvent = {
                    navController.navigate(route = "${EditEventDestination.route}/$it")
                }
            )
        }
        // New event screen
        composable(route = NewEventDestination.route) {
            val context = LocalContext.current
            NewEventScreen(
                onAddEvent = {
                    Toast.makeText(context, "Added event \"$it\"", Toast.LENGTH_SHORT).show()
                    navController.navigateUp()
                }
            )
        }
        // Edit event screen
        composable(
            route = EditEventDestination.routeWithArgs,
            arguments = listOf(navArgument(EditEventDestination.eventIdArg) {
                type = NavType.IntType
            })
        ) {
            val context = LocalContext.current
            EditEventScreen(
                onEditEvent = {
                    Toast.makeText(context, "Edited event \"$it\"", Toast.LENGTH_SHORT).show()
                    navController.navigateUp()
                },
                onDeleteEvent = {
                    Toast.makeText(context, "Deleted event \"$it\"", Toast.LENGTH_SHORT).show()
                    navController.navigateUp()
                }
            )
        }
    }
}
