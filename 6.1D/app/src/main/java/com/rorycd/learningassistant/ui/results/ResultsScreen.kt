package com.rorycd.learningassistant.ui.results

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.getValue
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.rorycd.learningassistant.R
import com.rorycd.learningassistant.navigation.NavigationDestination
import com.rorycd.learningassistant.ui.AppViewModelProvider
import com.rorycd.learningassistant.ui.components.LoadingSpinner

object ResultsDestination : NavigationDestination {
    override val route = "results"
    override val titleRes = R.string.results_destination_title

    const val QUIZ_ID_ARG = "quizId"
    val routeWithArgs = "${route}/{$QUIZ_ID_ARG}"
}

@Composable
fun ResultsScreen(
    onGoHome: () -> Unit,
    viewModel: ResultsViewModel = viewModel(factory = AppViewModelProvider.factory)
) {
    // Collect state flow
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .padding(horizontal = dimensionResource(R.dimen.padding_medium))
    ) {
        if (!state.isLoaded) {
            LoadingSpinner(stringResource(R.string.loading_results))
        } else {
            Text(
                text = stringResource(R.string.results_heading),
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(top = 32.dp)
            )
            Text(
                text = "${state.correctAnswers}/${state.questionCount}",
                style = MaterialTheme.typography.displayLarge,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(top = 8.dp)
            )
            Text(
                text = stringResource(R.string.feedback),
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(top = 16.dp)
            )
            Text(
                text = state.feedback,
                textAlign = TextAlign.Left,
                modifier = Modifier
                    .padding(top = 8.dp, bottom = 24.dp, start = 8.dp, end = 8.dp)
                    .fillMaxWidth()
            )
            Button(
                onClick = onGoHome,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 24.dp)
            ) {
                Text(
                    text = stringResource(R.string.back_to_home),
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(8.dp)
                )
            }
        }
    }
}
