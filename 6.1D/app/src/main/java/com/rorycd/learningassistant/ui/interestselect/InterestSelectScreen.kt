package com.rorycd.learningassistant.ui.interestselect

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.rorycd.learningassistant.R
import androidx.compose.ui.platform.LocalResources
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.rorycd.learningassistant.ui.AppViewModelProvider

@Composable
fun InterestSelectScreen(
    onFinishSelection: () -> Unit,
    viewModel: InterestSelectViewModel = viewModel(factory = AppViewModelProvider.factory)
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val interests = LocalResources.current.getStringArray(R.array.interests).toList()

    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .padding(horizontal = dimensionResource(R.dimen.padding_medium))
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = stringResource(R.string.your_interests),
            style = MaterialTheme.typography.displayMedium
        )
        Text(
            text = stringResource(R.string.interest_message),
            style = MaterialTheme.typography.bodyLarge
        )
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(top = 32.dp)
        ) {
            interests.forEach { label ->
                FilterChip(
                    onClick = { viewModel.toggleInterest(label) },
                    label = { Text(
                        text = label,
                        modifier = Modifier.padding(2.dp)
                    ) },
                    selected = state.selected.contains(label),
                    leadingIcon = if (state.selected.contains(label)) {
                        {
                            Icon(
                                painter = painterResource(id = R.drawable.check_small_24px),
                                contentDescription = stringResource(R.string.tick_desc)
                            )
                        }
                    } else { null }
                )
            }
        }
        Button(
            onClick = {
                viewModel.submitInterests()
                onFinishSelection()
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 32.dp)
        )
        { Text(
            text = stringResource(R.string.next),
            fontWeight = FontWeight.Bold
        ) }
    }
}
