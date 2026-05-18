package com.rorycd.bowerbird.ui.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.rorycd.bowerbird.R
import com.rorycd.bowerbird.ui.components.MinimalDropdown

enum class TimeInterval(
    val displayName: String,
    val valueInMins: Int
) {
    FIFTEEN_MINS("15 mins", 15),
    THIRTY_MINS("30 mins", 30),
    ONE_HOUR("1 hour", 60),
    TWO_HOURS("2 hours", 120),
    DAILY("Daily", 1440)
}

/**
 * Screen showing app settings
 */
@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val scrollState = rememberScrollState()
    val scanInterval by viewModel.scanInterval.collectAsStateWithLifecycle()
    val processingInterval by viewModel.processInterval.collectAsStateWithLifecycle()
    val intervals = TimeInterval.entries

    Column(
        modifier = Modifier.padding(horizontal = 20.dp)
            .fillMaxSize()
            .verticalScroll(scrollState)
    ) {
        // Scan interval
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = stringResource(R.string.scan_interval),
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.Bold
                )
            )
            MinimalDropdown(
                options = TimeInterval.entries,
                selectedOption = (intervals.first {it.valueInMins == scanInterval}).displayName,
                optionToText = { it.displayName },
                onSelectOption = { viewModel.setScanInterval(it.valueInMins) }
            )
        }
        Text(
            text = stringResource(R.string.scan_interval_description),
            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
        )

        Button(
            onClick = { viewModel.scanFoldersNow() },
            modifier = Modifier.padding(top = 20.dp)
        ) {
            Text(stringResource(R.string.scan_folders_now))
        }

        HorizontalDivider(modifier = Modifier.padding(vertical = 24.dp))

        // Processing interval
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = stringResource(R.string.processing_interval),
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.Bold
                )
            )
            MinimalDropdown(
                options = TimeInterval.entries,
                selectedOption = (intervals.first {it.valueInMins == processingInterval}).displayName,
                optionToText = { it.displayName },
                onSelectOption = { viewModel.setProcessingInterval(it.valueInMins) }
            )
        }
        Text(
            text = stringResource(R.string.processing_interval_description),
            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
        )

        Button(
            onClick = { viewModel.processImagesNow() },
            modifier = Modifier.padding(top = 20.dp)
        ) {
            Text(stringResource(R.string.process_images_now))
        }
    }
}
