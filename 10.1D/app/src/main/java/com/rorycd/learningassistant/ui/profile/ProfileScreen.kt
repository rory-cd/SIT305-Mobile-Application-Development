package com.rorycd.learningassistant.ui.profile

import android.content.Context
import android.content.Intent
import android.content.Intent.ACTION_SEND
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.rorycd.learningassistant.R
import com.rorycd.learningassistant.navigation.NavigationDestination
import com.rorycd.learningassistant.ui.AppViewModelProvider
import androidx.compose.ui.text.style.TextAlign
import com.rorycd.learningassistant.data.User

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
    onNavigateBack: () -> Unit,
    viewModel: ProfileViewModel = viewModel(factory = AppViewModelProvider.factory)
) {
    val context by rememberUpdatedState(LocalContext.current)
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
        // Heading
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                // Message
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
                        text = stringResource(R.string.profile),
                        style = MaterialTheme.typography.displayMedium
                    )
                }
                HorizontalDivider(Modifier.padding(bottom = 4.dp))
                Text(
                    text = currentUser?.username ?: "",
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(start = 34.dp)
                )
                Text(
                    text = currentUser?.email ?: "",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(start = 34.dp)
                )
                HorizontalDivider(Modifier.padding(vertical = 10.dp))
            }
            Column(
                horizontalAlignment = Alignment.End,
                modifier = Modifier
                    .padding(start = 16.dp, top = 24.dp)
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
        // Share
        Button(
            enabled = topTopics.isNotEmpty() && bottomTopics.isNotEmpty() && currentUser != null,
            onClick = {
                val topTopic = topTopics.first()
                val bottomTopic = bottomTopics.first()

                val sharedText = context.getString(
                    R.string.share_stats_template,
                    results.size,
                    averageScore,
                    topTopic.first,
                    topTopic.second,
                    bottomTopic.first,
                    bottomTopic.second
                )

                shareProfileStats(context, sharedText, currentUser!!)
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 24.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.share_24px),
                contentDescription = stringResource(R.string.share_desc),
                Modifier.padding(end = 4.dp)
            )
            Text(
                text = stringResource(R.string.share_button),
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}

fun shareProfileStats(context: Context, content: String, user: User) {
    val share = Intent.createChooser(
        Intent().apply {
            action = ACTION_SEND
            type = "text/plain"
            // Title
            putExtra(
                Intent.EXTRA_TITLE,
                context.getString(R.string.share_profile_title, user.username)
            )
            // Content
            putExtra(Intent.EXTRA_TEXT, content)
        },
        null
    )
    context.startActivity(share)
}
