package com.rorycd.eventplanner.ui.editevent

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import com.rorycd.eventplanner.R
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.material3.Icon
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.rorycd.eventplanner.navigation.NavigationDestination
import com.rorycd.eventplanner.ui.AppViewModelProvider
import com.rorycd.eventplanner.ui.components.EventDetailsForm

/**
 * Destination for [EditEventScreen]. Implements [NavigationDestination]
 */
object EditEventDestination : NavigationDestination {
    override val route = "edit_event"
    override val titleRes = R.string.edit_event
    const val eventIdArg = "eventId"            // Used for referencing the route parameter
    val routeWithArgs = "$route/{$eventIdArg}"  // Dynamic route based on selected event
}

/**
 * Composable for edit event screen
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditEventScreen(
    onEditEvent: (String) -> Unit,
    onDeleteEvent: (String) -> Unit,
    viewModel: EditEventViewModel = viewModel(factory = AppViewModelProvider.factory)
) {
    // Collect state flow
    val collectedState by viewModel.uiState.collectAsState()
    val state = collectedState

    if (state == null) {
        CircularProgressIndicator()
    } else {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(horizontal = dimensionResource(R.dimen.padding_medium)),
        ) {
            EventDetailsForm(
                title = state.currentTitle,
                date = state.currentDate,
                time = state.currentTimeMins,
                location = state.currentLocation,
                category = state.currentCategory,
                onTitleChanged = { viewModel.onTitleChanged(it) },
                onDateChanged = { millis -> viewModel.onDateChanged(millis) },
                onTimeChanged = { hour, minutes -> viewModel.onTimeChanged(hour, minutes) },
                onLocationChanged = { viewModel.onLocationChanged(it) },
                onCategoryChanged = { viewModel.onCategoryChanged(it) }
            )

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                // Delete button
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = stringResource(R.string.delete_content_desc),
                    modifier = Modifier
                        .padding(all = 8.dp)
                        .clickable {
                            viewModel.deleteEvent()
                            onDeleteEvent(state.currentTitle)
                        }
                )

                // Confirm button
                Button(
                    onClick = {
                        viewModel.updateEvent()
                        onEditEvent(state.currentTitle)
                    },
                    enabled = state.isValid
                ) {
                    Text(stringResource(R.string.edit_event_confirm_button))
                }
            }
        }
    }
}
