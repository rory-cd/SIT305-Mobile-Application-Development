package com.rorycd.learningassistant.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.rorycd.learningassistant.R
import com.rorycd.learningassistant.navigation.NavigationDestination
import com.rorycd.learningassistant.ui.AppViewModelProvider
import com.rorycd.learningassistant.ui.components.LoadingSpinner
import com.rorycd.learningassistant.ui.components.QuizCard

object HomeDestination : NavigationDestination {
    override val route = "home"
    override val titleRes = R.string.home_destination_title
}

@Composable
fun HomeScreen(
    onLogOut: () -> Unit,
    onStartQuiz: (Int) -> Unit,
    onPickInterests: () -> Unit,
    viewModel: HomeViewModel = viewModel(factory = AppViewModelProvider.factory)
) {
    val currentUser by viewModel.currentUser.collectAsStateWithLifecycle()
    val quizzes by viewModel.quizzes.collectAsStateWithLifecycle()

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = dimensionResource(R.dimen.padding_medium))
    ) {
        // Header
        item {
            Row(
                modifier = Modifier.fillMaxWidth()
                    .padding(top = 24.dp)
            ) {
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    // Welcome message
                    Text(
                        text = stringResource(R.string.hello_message),
                        style = MaterialTheme.typography.displaySmall
                    )
                    Text(
                        text = currentUser?.username ?: "",
                        style = MaterialTheme.typography.displayLarge
                    )
                }
                Column(
                    horizontalAlignment = Alignment.End,
                    modifier = Modifier
                        .padding(start = 16.dp)
                        .width(75.dp)
                ) {
                    AsyncImage(
                        model = currentUser?.imgUri,
                        contentDescription = stringResource(id = R.string.user_image_description),
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(75.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.tertiaryContainer)
                            .border(
                                width = 2.dp,
                                color = MaterialTheme.colorScheme.primary,
                                shape = CircleShape
                            )
                    )
                    TextButton(
                        onClick = {
                            viewModel.logOut()
                            onLogOut()
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(stringResource(R.string.log_out_button))
                    }
                }
            }
        }

        // If no interests listed, prompt to pick some
        item {
            if (currentUser?.interests.isNullOrEmpty()) {
                TextButton(
                    onClick = onPickInterests,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp)
                ) {
                    Text(stringResource(R.string.no_interests))
                }
            } else if (quizzes.isEmpty()) {
                LoadingSpinner(stringResource(R.string.generating_quizzes))
            }
        }

        // List incomplete quizzes
        items(quizzes) { quiz ->
            if (quiz != null)
                QuizCard(
                    topic = quiz.topic,
                    onClick = { onStartQuiz(quiz.id) }
                )
        }
    }
}
