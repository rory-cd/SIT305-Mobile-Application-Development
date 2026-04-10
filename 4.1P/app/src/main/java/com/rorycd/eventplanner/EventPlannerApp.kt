package com.rorycd.eventplanner

import android.widget.Toast
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.rorycd.eventplanner.ui.EventListScreen
import com.rorycd.eventplanner.ui.NewEventScreen

enum class Screen(@StringRes val title: Int) {
    EventList(title = R.string.app_name),
    AddEvent(title = R.string.add_event),
    EditEvent(title = R.string.edit_event)
}

enum class NavBarDestination(
    val screen: Screen,
    val icon: ImageVector,
    val label: String,
    val contentDescription: String
) {
    Events(Screen.EventList, Icons.AutoMirrored.Default.List, "Events", "Event list screen"),
    NewEvent(Screen.AddEvent, Icons.Default.Add, "Add Event", "Add event screen")
}

// Tracks the screens used in the navbar
val topLevelScreens = NavBarDestination.entries.map { it.screen }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventPlannerAppBar(
    currentScreen: Screen,
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
    onSelectNavOption: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedDestination by rememberSaveable {
        mutableIntStateOf(Screen.EventList.ordinal)
    }
    NavigationBar(windowInsets = NavigationBarDefaults.windowInsets) {
        NavBarDestination.entries.forEachIndexed { index, destination ->
            NavigationBarItem(
                selected = selectedDestination == index,
                onClick = {
                    onSelectNavOption(destination.screen.name)
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
    val currentScreen = Screen.valueOf(
        backStackEntry?.destination?.route ?: Screen.EventList.name
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
            EventPlannerNavBar(
                onSelectNavOption = {
                    navController.navigate(route = it) {
                        popUpTo(Screen.EventList.name) {
                            inclusive = false
                        }
                        launchSingleTop = true
                    }
                }
            )
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.EventList.name,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            composable(route = Screen.EventList.name) {
                EventListScreen(
                    modifier = Modifier.fillMaxHeight(),
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
                    modifier = Modifier.fillMaxHeight(),
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
}

@Preview
@Composable
fun EventPlannerAppPreview() {
    EventPlannerApp()
}
