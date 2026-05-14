package com.rorycd.learningassistant.ui.quiz

import android.widget.Toast
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.getValue
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.rorycd.learningassistant.R
import com.rorycd.learningassistant.navigation.NavigationDestination
import com.rorycd.learningassistant.ui.AppViewModelProvider
import com.rorycd.learningassistant.ui.register.RegisterScreen

/**
 * Destination class for NavGraph route to [QuizScreen]
 */
object QuizDestination : NavigationDestination {
    override val route = "quiz"
    override val titleRes = R.string.quiz_destination_title

    const val QUIZ_ID_ARG = "quizId"
    val routeWithArgs = "${route}/{$QUIZ_ID_ARG}"
}

/**
 * Screen where quizzes are completed
 */
@Composable
fun QuizScreen(
    onQuizComplete: (quizId: Int) -> Unit,
    viewModel: QuizViewModel = viewModel(factory = AppViewModelProvider.factory)
) {
    // Collect state flow
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    var selected by rememberSaveable { mutableIntStateOf(-1) }
    val context = LocalContext.current

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = dimensionResource(R.dimen.padding_medium))
    ) {
        // Header
        item {
            Text(
                text = "${state.questionNumber + 1}/${state.totalQuestions}",
                modifier = Modifier.padding(top = 16.dp),
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            )
        }

        // Question
        item {
            Text(
                text = state.question,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp, top = 20.dp)
            )
        }

        // Options
        item {
            state.options.forEachIndexed { index, option ->
                val letter = (65 + index).toChar()

                Card(
                    onClick = { selected = index },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .then(
                            if (selected == index) Modifier.border(
                                4.dp,
                                MaterialTheme.colorScheme.primary,
                                MaterialTheme.shapes.medium
                            )
                            else Modifier
                        )
                ) {
                    Text(
                        text = "$letter:  $option",
                        fontWeight = if (selected == index) FontWeight.Bold else FontWeight.Normal,
                        color = if (selected == index) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.padding(28.dp)
                    )
                }
            }
        }

        // Button
        item {
            val isLastQuestion = state.questionNumber == state.totalQuestions - 1

            Button(
                enabled = selected != -1,
                onClick = {
                    if (isLastQuestion) {
                        viewModel.submitAnswer(selected)
                        viewModel.submitResult()
                        onQuizComplete(state.quizId)
                    }
                    else {
                        viewModel.submitAnswer(selected)
                        selected = -1
                    }
                },
                modifier = Modifier.fillMaxWidth()
                    .padding(top = 12.dp)
            ) {
                Text(
                    if (isLastQuestion) stringResource(R.string.finish)
                    else stringResource(R.string.next),
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(8.dp)
                )
            }
        }
    }
    // Show toast if toast message is set
    LaunchedEffect(state.toastMessage) {
        state.toastMessage?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            viewModel.clearToast()
        }
    }
}
