package com.rorycd.eventplanner.ui.newevent

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import com.rorycd.eventplanner.R
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.rorycd.eventplanner.navigation.NavigationDestination
import com.rorycd.eventplanner.ui.AppViewModelProvider
import com.rorycd.eventplanner.ui.components.EventDetailsForm

/**
 * Destination for [NewEventScreen]. Implements [NavigationDestination]
 */
object NewEventDestination : NavigationDestination {
    override val route = "new_event"
    override val titleRes = R.string.add_event
}

/**
 * Composable for new event screen
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewEventScreen(
    onAddEvent: (String) -> Unit,
    viewModel: NewEventViewModel = viewModel(factory = AppViewModelProvider.factory)
) {
    // Collect state flow
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.End,
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

        // Add button
        Button(
            onClick = {
                viewModel.saveEvent()
                onAddEvent(state.currentTitle)
            },
            enabled = state.isValid
        ) {
            Text(stringResource(R.string.new_event_confirm_button))
        }
    }
}
