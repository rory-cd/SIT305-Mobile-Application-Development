package com.rorycd.eventplanner.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import com.rorycd.eventplanner.R
import com.rorycd.eventplanner.utils.formatDateAsString
import com.rorycd.eventplanner.utils.formatMinutesAsTime

@Composable
fun EventDetailsForm(
    title: String,
    location: String,
    category: String,
    date: Long,
    time: Int,
    onTitleChanged: (String) -> Unit,
    onDateChanged: (Long?) -> Unit,
    onTimeChanged: (Int, Int) -> Unit,
    onLocationChanged: (String) -> Unit,
    onCategoryChanged: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_medium)),
        horizontalAlignment = Alignment.End,
        modifier = modifier
    ) {
        // Title
        OutlinedTextField(
            value = title,
            onValueChange = onTitleChanged,
            label = { Text(stringResource(R.string.event_title_label)) },
            singleLine = true,
            maxLines = 1,
            modifier = Modifier.fillMaxWidth()
        )
        Row (
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = dimensionResource(R.dimen.padding_medium))
        ) {
            // Date
            DatePickerText(
                value = formatDateAsString(date),
                onDateSelected = onDateChanged
            )
            // Time
            TimePickerText(
                value = formatMinutesAsTime(time),
                onConfirm = onTimeChanged
            )
        }
        // Location
        OutlinedTextField(
            value = location,
            onValueChange = onLocationChanged,
            label = { Text(stringResource(R.string.event_location_label)) },
            singleLine = true,
            maxLines = 1,
            modifier = Modifier.fillMaxWidth()
        )
        // Category
        OutlinedTextField(
            value = category,
            onValueChange = onCategoryChanged,
            label = { Text(stringResource(R.string.event_category_label)) },
            singleLine = true,
            maxLines = 1,
            modifier = Modifier.fillMaxWidth()
        )
    }
}
