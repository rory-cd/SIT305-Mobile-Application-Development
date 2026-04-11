package com.rorycd.eventplanner.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import com.rorycd.eventplanner.navigation.NavigationDestination
import com.rorycd.eventplanner.ui.eventlist.EventListDestination
import com.rorycd.eventplanner.ui.newevent.NewEventDestination

enum class NavBarOption(
    val destination: NavigationDestination,
    val icon: ImageVector,
    val label: String,
    val contentDescription: String
) {
    Events(EventListDestination, Icons.AutoMirrored.Default.List, "Events", "Event list screen"),
    NewEvent(NewEventDestination, Icons.Default.Add, "Add Event", "Add event screen")
}

@Composable
fun EventPlannerNavBar(
    onSelectNavOption: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedDestination by rememberSaveable {
        mutableIntStateOf(0)
    }

    NavigationBar(
        windowInsets = NavigationBarDefaults.windowInsets,
        modifier = modifier
    ) {
        NavBarOption.entries.forEachIndexed { index, navOption ->
            NavigationBarItem(
                selected = selectedDestination == index,
                onClick = {
                    onSelectNavOption(navOption.destination.route)
                    selectedDestination = index
                },
                icon = {
                    Icon(
                        navOption.icon,
                        contentDescription = navOption.contentDescription
                    )
                },
                label = { Text(navOption.label) }
            )
        }
    }
}
