package com.rorycd.learningassistant.ui.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.rorycd.learningassistant.R
import com.rorycd.learningassistant.navigation.NavigationDestination
import com.rorycd.learningassistant.ui.AppViewModelProvider
import com.rorycd.learningassistant.ui.components.LoadingSpinner
import com.rorycd.learningassistant.ui.components.QuizCard
import androidx.compose.ui.text.style.TextAlign

/**
 * Destination class for NavGraph route to [ProfileScreen]
 */
object ProfileDestination : NavigationDestination {
    override val route = "profile"
    override val titleRes = R.string.profile_destination_title
}

/**
 * Screen where users can view their profile
 */
@Composable
fun ProfileScreen(
    onLogOut: () -> Unit,
    onViewHistory: () -> Unit,
    viewModel: ProfileViewModel = viewModel(factory = AppViewModelProvider.factory)
) {
    val currentUser by viewModel.currentUser.collectAsStateWithLifecycle()
    val results by viewModel.results.collectAsStateWithLifecycle()
    val averageScore by viewModel.averageScore.collectAsStateWithLifecycle()
    val topTopics by viewModel.topTopics.collectAsStateWithLifecycle()
    val bottomTopics by viewModel.bottomTopics.collectAsStateWithLifecycle()

    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = dimensionResource(R.dimen.padding_medium))
    ) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth()
                .padding(top = 24.dp)
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                // Message
                Text(
                    text = stringResource(R.string.profile),
                    style = MaterialTheme.typography.displayMedium
                )
                Text(
                    text = currentUser?.username ?: "",
                    style = MaterialTheme.typography.displaySmall,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = currentUser?.email ?: "",
                    style = MaterialTheme.typography.bodyLarge
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
        // General stats
        Row(
            modifier = Modifier.fillMaxWidth()
                .padding(top = 16.dp)
                .height(IntrinsicSize.Max)
        ) {
            Card(
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.padding(end = 8.dp)
                    .weight(1f)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxHeight()
                        .padding(8.dp)
                ) {
                    // Quiz history
                    Text(
                        text = stringResource(R.string.total_quiz_count),
                        style = MaterialTheme.typography.headlineMedium,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                    Text(
                        text = results.size.toString(),
                        style = MaterialTheme.typography.displayLarge,
                        textAlign = TextAlign.Center
                    )
                }
            }
            Card(
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.padding(start = 8.dp)
                    .weight(1f)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(8.dp)
                ) {
                    // Average score
                    Text(
                        text = stringResource(R.string.average_score),
                        style = MaterialTheme.typography.headlineMedium,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                    Text(
                        text = "${averageScore}%",
                        style = MaterialTheme.typography.displayLarge,
                        textAlign = TextAlign.Center
                    )
                    TextButton(
                        onClick = onViewHistory,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(stringResource(R.string.view_history))
                    }
                }
            }
        }
        // Best topics
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(vertical = 16.dp)
        ) {
            Text(
                text = stringResource(R.string.top_topics),
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(bottom = 4.dp)
            )
            if (topTopics.isEmpty()) {
                Text(stringResource(R.string.no_results_to_display))
            }
            topTopics.forEach {
                Text(
                    text = "${it.first}: ${it.second}%",
                    modifier = Modifier.padding(bottom = 2.dp)
                )
            }
        }
        // Worst topics
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.bottom_topics),
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(bottom = 4.dp)
            )
            if (bottomTopics.isEmpty()) {
                Text(stringResource(R.string.no_results_to_display))
            }
            bottomTopics.forEach {
                Text(
                    text = "${it.first}: ${it.second}%",
                    modifier = Modifier.padding(bottom = 2.dp)
                )
            }
        }
    }
}
