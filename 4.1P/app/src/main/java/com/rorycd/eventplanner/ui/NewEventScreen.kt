package com.rorycd.eventplanner.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import com.rorycd.eventplanner.R
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import java.time.Instant
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewEventScreen(
    onAddEvent: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: NewEventViewModel = viewModel(factory = AppViewModelProvider.factory)
) {
    // Collect state flow
    val state by viewModel.uiState.collectAsState()

    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .padding(horizontal = dimensionResource(R.dimen.padding_medium)),
        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_medium)),
        horizontalAlignment = Alignment.End
    ) {
        // Title
        OutlinedTextField(
            value = state.currentTitle,
            onValueChange = { viewModel.onTitleChanged(it) },
            label = { Text(stringResource(R.string.event_title_label)) },
            singleLine = true,
            maxLines = 1,
            modifier = Modifier.fillMaxWidth()
        )
        Row (
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = modifier
                .fillMaxWidth()
                .padding(vertical = dimensionResource(R.dimen.padding_medium))
        ) {
            // Date
            DatePickerText(
                value = formatDate(state.currentDate),
                onDateSelected = { millis -> viewModel.onDateChanged(millis) }
            )
            // Time
            TimePickerText(
                value = formatMinutes(state.currentTimeMins),
                onConfirm = { hour, minutes -> viewModel.onTimeChanged(hour, minutes) }
            )
        }
        // Location
        OutlinedTextField(
            value = state.currentLocation,
            onValueChange = { viewModel.onLocationChanged(it) },
            label = { Text(stringResource(R.string.event_location_label)) },
            singleLine = true,
            maxLines = 1,
            modifier = Modifier.fillMaxWidth()
        )
        // Category
        OutlinedTextField(
            value = state.currentCategory,
            onValueChange = { viewModel.onCategoryChanged(it) },
            label = { Text(stringResource(R.string.event_category_label)) },
            singleLine = true,
            maxLines = 1,
            modifier = Modifier.fillMaxWidth()
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

fun formatDate(millis: Long): String {
    val formatter = DateTimeFormatter.ofPattern("d MMMM yyyy")
    return Instant.ofEpochMilli(millis)
        .atZone(ZoneId.systemDefault())
        .format(formatter)
}

fun formatTime(millis: Long): String {
    val formatter = DateTimeFormatter.ofPattern("h:mm a")
    return Instant.ofEpochMilli(millis)
        .atZone(ZoneId.systemDefault())
        .format(formatter)
}

fun formatMinutes(mins: Int): String {
    val hour = mins / 60
    val minute = mins % 60
    val localTime = LocalTime.of(hour, minute)
    return localTime.format(DateTimeFormatter.ofPattern("h:mm a"))
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerText(
    value: String,
    onDateSelected: (Long?) -> Unit,
    modifier: Modifier = Modifier
) {
    var showDatePicker by remember { mutableStateOf(false) }
    val onDismiss = { showDatePicker = false }
    val datePickerState = rememberDatePickerState()

    Text(
        text = value,
        modifier = modifier.clickable { showDatePicker = true }
    )

    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = onDismiss,
            confirmButton = {
                TextButton(onClick = {
                    onDateSelected(datePickerState.selectedDateMillis)
                    onDismiss()
                }) {
                    Text("Ok")
                }
            },
            dismissButton = {
                TextButton(onClick = onDismiss) {
                    Text("Cancel")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimePickerText(
    value: String,
    onConfirm: (Int, Int) -> Unit,
    modifier: Modifier = Modifier
) {
    var showTimePicker by remember { mutableStateOf(false) }
    val onDismiss = { showTimePicker = false }
    val currentTime = Calendar.getInstance()

    val timePickerState = rememberTimePickerState(
        initialHour = currentTime.get(Calendar.HOUR_OF_DAY),
        initialMinute = currentTime.get(Calendar.MINUTE),
        is24Hour = false
    )

    Text(
        text = value,
        modifier = modifier.clickable { showTimePicker = true }
    )

    if (showTimePicker) {
        BasicAlertDialog(
            onDismissRequest = onDismiss,
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = MaterialTheme.colorScheme.surface,
                    shape = RoundedCornerShape(28.dp)
                )
        ) {
            Column (
                modifier = Modifier.padding(dimensionResource(R.dimen.padding_medium))
            ) {
                TimePicker(
                    state = timePickerState,
                )
                Row (
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("Cancel")
                    }
                    TextButton(
                        onClick = {
                            onConfirm(timePickerState.hour, timePickerState.minute)
                            onDismiss()
                        }
                    ) {
                        Text("Ok")
                    }
                }
            }
        }
    }
}
