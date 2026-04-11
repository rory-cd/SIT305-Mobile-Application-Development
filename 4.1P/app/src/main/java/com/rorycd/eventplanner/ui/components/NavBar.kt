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
import com.rorycd.eventplanner.navigation.Screen

enum class NavBarDestination(
    val screen: Screen,
    val icon: ImageVector,
    val label: String,
    val contentDescription: String
) {
    Events(Screen.EventList, Icons.AutoMirrored.Default.List, "Events", "Event list screen"),
    NewEvent(Screen.AddEvent, Icons.Default.Add, "Add Event", "Add event screen")
}

@Composable
fun EventPlannerNavBar(
    onSelectNavOption: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedDestination by rememberSaveable {
        mutableIntStateOf(Screen.EventList.ordinal)
    }

    NavigationBar(
        windowInsets = NavigationBarDefaults.windowInsets,
        modifier = modifier
    ) {
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
