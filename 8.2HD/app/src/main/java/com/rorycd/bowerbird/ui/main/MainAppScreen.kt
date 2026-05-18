package com.rorycd.bowerbird.ui.main

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavDestination
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.rorycd.bowerbird.R
import com.rorycd.bowerbird.navigation.BowerbirdNavHost
import com.rorycd.bowerbird.navigation.FoldersRoute
import com.rorycd.bowerbird.navigation.RulesRoute
import com.rorycd.bowerbird.navigation.SettingsRoute
import androidx.navigation.NavDestination.Companion.hasRoute
import com.rorycd.bowerbird.navigation.EditRuleRoute
import com.rorycd.bowerbird.navigation.NewRuleRoute

enum class NavBarOption(
    val destination: Any,
    val iconRes: Int
) {
    Rules(RulesRoute, R.drawable.rules),
    Folders(FoldersRoute, R.drawable.folders),
    Settings(SettingsRoute, R.drawable.settings)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainAppScreen() {
    val navController = rememberNavController()

    val activeColor = MaterialTheme.colorScheme.primary
    val inActiveColor = MaterialTheme.colorScheme.onSurfaceVariant

    // Get current destination
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val isTopLevelDestination = NavBarOption.entries.any {
        currentDestination?.hasRoute(it.destination::class) == true
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(getDestinationTitleRes(currentDestination)),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    if (!isTopLevelDestination) {
                        IconButton(onClick = { navController.navigateUp() }) {
                            Icon (
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = stringResource(R.string.back_button)
                            )
                        }
                    }
                }
            )
        },
        bottomBar = {
            NavigationBar(
                windowInsets = NavigationBarDefaults.windowInsets
            ) {
                NavBarOption.entries.forEachIndexed { index, navOption ->
                    val isSelected = currentDestination?.hasRoute(navOption.destination::class) == true
                    val navOptionTitle = stringResource(getDestinationTitleRes(navOption.destination))

                    NavigationBarItem(
                        selected = isSelected,
                        onClick = {
                            if (!isSelected) {
                                navController.navigate(navOption.destination) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    // Restore state when re-selecting a previously selected tab
                                    restoreState = true
                                }
                            }
                        },
                        icon = {
                            Icon(
                                painter = painterResource(navOption.iconRes),
                                contentDescription = navOptionTitle,
                                tint = if (isSelected) activeColor else inActiveColor
                            )
                        },
                        label = {
                            Text(
                                text = navOptionTitle.uppercase(),
                                fontWeight = FontWeight.Bold
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            indicatorColor = Color.Transparent
                        )
                    )
                }
            }
        },
        floatingActionButton = {
            when {
                currentDestination?.hasRoute(RulesRoute::class) == true -> {
                    ExtendedFloatingActionButton(
                        onClick = { navController.navigate(NewRuleRoute) },
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary,
                        shape = CircleShape,
                        icon = {
                            Icon(
                                Icons.Filled.Add,
                                contentDescription = stringResource(R.string.new_rule)
                            )
                        },
                        text = { Text(stringResource(R.string.new_rule)) }
                    )
                }
                currentDestination?.hasRoute(FoldersRoute::class) == true -> {
                    ExtendedFloatingActionButton(
                        onClick = {
                            // navController.navigate(AddFolderRoute)
                        },
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary,
                        shape = CircleShape,
                        icon = {
                            Icon(
                                Icons.Filled.Add,
                                contentDescription = stringResource(R.string.add_folder)
                            )
                        },
                        text = { Text(stringResource(R.string.add_folder)) }
                    )
                }
            }
        },
//        floatingActionButton = {
//            if (currentDestination?.hasRoute(RulesRoute::class) == true) {
//                ExtendedFloatingActionButton(
//                    onClick = {
//                        // navController.navigate(AddRuleRoute)
//                    },
//                    containerColor = MaterialTheme.colorScheme.primary,
//                    contentColor = MaterialTheme.colorScheme.onPrimary,
//                    shape = CircleShape,
//                    icon = {
//                        Icon(
//                            Icons.Filled.Add,
//                            contentDescription = stringResource(R.string.new_rule)
//                        )
//                    },
//                    text = { Text(stringResource(R.string.new_rule)) }
//                )
//            }
//        }
    ) { innerPadding ->
        BowerbirdNavHost(
            navController = navController,
            modifier = Modifier.padding(innerPadding)
        )
    }
}

fun getDestinationTitleRes(route: Any?): Int {
    return when (route) {
        null -> R.string.app_name

        is NavDestination -> when {
            route.hasRoute(RulesRoute::class) -> R.string.rules_destination_title
            route.hasRoute(NewRuleRoute::class) -> R.string.new_rule_destination_title
            route.hasRoute(EditRuleRoute::class) -> R.string.edit_rule_destination_title
            route.hasRoute(FoldersRoute::class) -> R.string.folders_destination_title
            route.hasRoute(SettingsRoute::class) -> R.string.settings_destination_title
            else -> R.string.app_name
        }

        is RulesRoute -> R.string.rules_destination_title
        is NewRuleRoute -> R.string.new_rule_destination_title
        is EditRuleRoute -> R.string.edit_rule_destination_title
        is FoldersRoute -> R.string.folders_destination_title
        is SettingsRoute -> R.string.settings_destination_title

        else -> R.string.app_name
    }
}
