package com.rorycd.lostandfound.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import com.google.android.libraries.places.api.model.AutocompletePrediction
import com.rorycd.lostandfound.R

@Composable
fun AutoCompleteLocation(
    value: String,
    onValueChange: (newLocation: String) -> Unit,
    onSelectLocation: (AutocompletePrediction) -> Unit,
    predictions: List<AutocompletePrediction>,
    isLoading: Boolean,
    locationSelected: Boolean,
    label: String,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        OutlinedTextField(
            value = value,
            onValueChange = { onValueChange(it) },
            label = { Text(label) },
            singleLine = true,
            maxLines = 1,
            modifier = Modifier.fillMaxWidth(),
            prefix = {
                if (locationSelected) Icon(
                    imageVector = Icons.Filled.LocationOn,
                    contentDescription = stringResource(R.string.location_icon),
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        )
        if (isLoading || predictions.isNotEmpty()) {

            Surface(
                modifier = Modifier
                    .fillMaxWidth(),
                color = MaterialTheme.colorScheme.surfaceContainer,
                tonalElevation = 3.dp,
                shadowElevation = 4.dp
            ) {
                Column {
                    if (isLoading) LoadingSpinner("")

                    predictions.forEach { prediction ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onSelectLocation(prediction) }
                                .padding(horizontal = 12.dp, vertical = 12.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Filled.LocationOn,
                                contentDescription = stringResource(R.string.location_icon),
                                tint = MaterialTheme.colorScheme.primary
                            )
                            Text(
                                text = prediction.getFullText(null).toString(),
                                modifier = Modifier.padding(start = 12.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}
