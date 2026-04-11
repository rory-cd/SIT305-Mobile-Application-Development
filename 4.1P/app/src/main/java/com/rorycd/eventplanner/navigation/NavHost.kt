package com.rorycd.eventplanner.navigation

import android.widget.Toast
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.rorycd.eventplanner.ui.eventlist.EventListScreen
import com.rorycd.eventplanner.ui.newevent.NewEventScreen

@Composable
fun EventPlannerNavHost (
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Screen.EventList.name,
        modifier = modifier
    ) {
        composable(route = Screen.EventList.name) {
            EventListScreen(
                onSelectEvent = {
                    navController.navigate(route = Screen.EditEvent.name) {
                        popUpTo(Screen.EventList.name) {
                            inclusive = false
                        }
                        launchSingleTop = true
                    }
                }
            )
        }
        composable(route = Screen.AddEvent.name) {
            val context = LocalContext.current
            NewEventScreen(
                onAddEvent = {
                    Toast.makeText(context, "Added event \"$it\"", Toast.LENGTH_SHORT).show()

                    navController.navigate(route = Screen.EventList.name) {
                        popUpTo(Screen.EventList.name) {
                            inclusive = false
                        }
                        launchSingleTop = true
                    }
                }
            )
        }
    }
}
