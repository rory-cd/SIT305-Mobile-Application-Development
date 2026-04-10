package com.rorycd.eventplanner.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import com.rorycd.eventplanner.R
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun NewEventScreen(
    modifier: Modifier = Modifier,
    viewModel: NewEventViewModel = viewModel(factory = AppViewModelProvider.factory)
) {
    // Collect state flow
    val state by viewModel.uiState.collectAsState()

    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .padding(horizontal = dimensionResource(R.dimen.padding_medium)),
        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_medium))
    ) {
        OutlinedTextField(
            value = state.currentTitle,
            onValueChange = { viewModel.onTitleChanged(it) },
            label = { Text(stringResource(R.string.event_title_label)) },
            singleLine = true,
            maxLines = 1,
            modifier = Modifier.fillMaxWidth()
        )
//        DatePickerDocked(
//            dateValue = state.currentDate,
//            onDateSelected = { millis -> viewModel.onDateChanged(millis) }
//        )
        DatePickerText(
            value = state.currentDate,
            onDateSelected = { millis -> viewModel.onDateChanged(millis) }
        )
        OutlinedTextField(
            value = state.currentLocation,
            onValueChange = { viewModel.onLocationChanged(it) },
            label = { Text(stringResource(R.string.event_location_label)) },
            singleLine = true,
            maxLines = 1,
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = state.currentCategory,
            onValueChange = { viewModel.onCategoryChanged(it) },
            label = { Text(stringResource(R.string.event_category_label)) },
            singleLine = true,
            maxLines = 1,
            modifier = Modifier.fillMaxWidth()
        )
        Button(
            onClick = { viewModel.saveEvent() },
            enabled = state.isValid
        ) {
            Text(stringResource(R.string.new_event_confirm_button))
        }
    }
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
                    Text("OK")
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

//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun DatePickerDocked(
//    dateValue: String,
//    onDateSelected: (Long?) -> Unit
//) {
//    var showDatePicker by remember { mutableStateOf(false) }
//    val datePickerState = rememberDatePickerState()
//
//    // Observe the selected date and call onDateSelected when it changes
//    LaunchedEffect(datePickerState.selectedDateMillis) {
//        datePickerState.selectedDateMillis?.let {
//            onDateSelected(it)
//            showDatePicker = false
//        }
//    }
//
//    Box(
//        modifier = Modifier.fillMaxWidth()
//    ) {
//        OutlinedTextField(
//            value = dateValue,
//            onValueChange = { },
//            label = { Text(stringResource(R.string.event_date_label)) },
//            readOnly = true,
//            singleLine = true,
//            maxLines = 1,
//            trailingIcon = {
//                IconButton(onClick = { showDatePicker = true }) {
//                    Icon(
//                        imageVector = Icons.Default.DateRange,
//                        contentDescription = "Select date"
//                    )
//                }
//            },
//            modifier = Modifier
//                .fillMaxWidth()
//                .height(64.dp)
//                .onFocusChanged { if (it.isFocused) showDatePicker = true }
//        )
//
//        if (showDatePicker) {
//            Popup(
//                onDismissRequest = { showDatePicker = false },
//                alignment = Alignment.TopStart
//            ) {
//                Box(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .offset(y = 64.dp)
//                        .shadow(elevation = 4.dp)
//                        .background(MaterialTheme.colorScheme.surface)
//                        .padding(16.dp)
//                ) {
//                    DatePicker(
//                        state = datePickerState,
//                        showModeToggle = false
//                    )
//                }
//            }
//        }
//    }
//}

@Preview
@Composable
fun NewEventScreenPreview () {
    NewEventScreen()
}
