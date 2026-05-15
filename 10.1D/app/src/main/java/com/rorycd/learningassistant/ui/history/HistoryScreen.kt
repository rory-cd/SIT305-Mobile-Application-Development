package com.rorycd.learningassistant.ui.history

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.rorycd.learningassistant.R
import com.rorycd.learningassistant.navigation.NavigationDestination
import com.rorycd.learningassistant.ui.AppViewModelProvider
import com.rorycd.learningassistant.ui.components.ResultCard

/**
 * Destination class for NavGraph route to [HistoryScreen]
 */
object HistoryDestination : NavigationDestination {
    override val route = "history"
    override val titleRes = R.string.history_destination_title
}

/**
 * Screen where users can view their past quiz results
 */
@Composable
fun HistoryScreen(
    onNavigateBack: () -> Unit,
    viewModel: HistoryViewModel = viewModel(factory = AppViewModelProvider.factory)
) {
    val results by viewModel.results.collectAsStateWithLifecycle()

    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = dimensionResource(R.dimen.padding_medium))
    ) {
        // Heading
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = R.drawable.arrow_back_24px),
                contentDescription = stringResource(R.string.arrow_back_desc),
                modifier = Modifier.padding(end = 8.dp)
                    .clickable(enabled = true, onClick = onNavigateBack)
            )
            Text(
                text = stringResource(R.string.history),
                style = MaterialTheme.typography.displayMedium
            )
        }
        LazyColumn(
            Modifier.fillMaxHeight()
        ) {
            items(results) {
                ResultCard(it)
            }
        }
    }
}
