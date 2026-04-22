package com.rorycd.learningassistant.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.paddingFromBaseline
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.rorycd.learningassistant.R
import com.rorycd.learningassistant.data.UserRepository
import kotlinx.coroutines.launch
import androidx.compose.ui.platform.LocalResources
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.rorycd.learningassistant.ui.theme.LearningAssistantTheme

@Composable
fun InterestSelectScreen(
    repo: UserRepository,
    onFinishSelection: () -> Unit
) {
    val interests = LocalResources.current.getStringArray(R.array.interests).toList()
    val selected = remember { mutableStateListOf<String>() }
    val scope = rememberCoroutineScope()

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
                    onClick = {
                        if (selected.contains(label)) selected.remove(label)
                        else if (selected.size < 10) selected.add(label)
                    },
                    label = { Text(
                        text = label,
                        modifier = Modifier.padding(2.dp)
                    ) },
                    selected = selected.contains(label),
                    leadingIcon = if (selected.contains(label)) {
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
                scope.launch { repo.addInterests(selected) }
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
