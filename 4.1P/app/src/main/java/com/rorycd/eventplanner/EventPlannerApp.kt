package com.rorycd.eventplanner

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.rorycd.eventplanner.ui.EventListScreen
import com.rorycd.eventplanner.ui.NewEventScreen
import com.rorycd.eventplanner.ui.eventsTest

enum class PlannerScreen(@StringRes val title: Int) {
    EventList(title = R.string.app_name),
    AddEvent(title = R.string.add_event),
    EditEvent(title = R.string.edit_event)
}

enum class NavBarDestination(
    val screen: PlannerScreen,
    val icon: ImageVector,
    val label: String,
    val contentDescription: String
) {
    Events(PlannerScreen.EventList, Icons.AutoMirrored.Default.List, "Events", "Event list screen"),
    NewEvent(PlannerScreen.AddEvent, Icons.Default.Add, "Add Event", "Add event screen")
}

// Tracks the screens used in the navbar
val topLevelScreens = NavBarDestination.entries.map { it.screen }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventPlannerAppBar(
    currentScreen: PlannerScreen,
    canGoBack: Boolean,
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier
) {
    TopAppBar(
        title = { Text(stringResource(currentScreen.title)) },
        modifier = modifier,
        navigationIcon = {
            if (canGoBack) {
                IconButton(onClick = navigateUp) {
                    Icon (
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.back_button)
                    )
                }
            }
        }
    )
}

@Composable
fun EventPlannerNavBar(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    var selectedDestination by rememberSaveable {
        mutableIntStateOf(PlannerScreen.EventList.ordinal)
    }
    NavigationBar(windowInsets = NavigationBarDefaults.windowInsets) {
        NavBarDestination.entries.forEachIndexed { index, destination ->
            NavigationBarItem(
                selected = selectedDestination == index,
                onClick = {
                    navController.navigate(route = destination.screen.name) {
                        popUpTo(PlannerScreen.EventList.name) {
                            inclusive = false
                        }
                        launchSingleTop = true
                    }
                    selectedDestination = index
                },
                icon = {
                    Icon(
                        destination.icon,
                        contentDescription = destination.contentDescription
                    )
                },
                label = { Text(destination.label) }
            )
        }
    }
}

@Composable
fun EventPlannerApp(
    navController: NavHostController = rememberNavController()
) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentScreen = PlannerScreen.valueOf(
        backStackEntry?.destination?.route ?: PlannerScreen.EventList.name
    )

    Scaffold(
        contentWindowInsets = WindowInsets.safeDrawing,
        topBar = {
            EventPlannerAppBar(
                currentScreen = currentScreen,
                canGoBack = currentScreen !in topLevelScreens,
                navigateUp = { navController.navigateUp() }
            )
        },
        bottomBar = {
            EventPlannerNavBar(navController)
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = PlannerScreen.EventList.name,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            composable(route = PlannerScreen.EventList.name) {
                EventListScreen(
                    events = eventsTest,
                    modifier = Modifier.fillMaxHeight()
                )
            }
            composable(route = PlannerScreen.AddEvent.name) {
                NewEventScreen(
                    modifier = Modifier.fillMaxHeight()
                )
            }
        }
    }
}

@Preview
@Composable
fun EventPlannerAppPreview() {
    EventPlannerApp()
}
