package com.rorycd.chatbot.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.rorycd.chatbot.R
import com.rorycd.chatbot.navigation.NavigationDestination
import com.rorycd.chatbot.ui.components.ConversationCard
import com.rorycd.chatbot.ui.components.LoadingSpinner

/**
 * Destination class for NavGraph route to [HomeScreen]
 */
object HomeDestination : NavigationDestination {
    override val route = "home"
    override val titleRes = R.string.home_destination_title
}

/**
 * Screen to allow users to chat with AI
 */
@Composable
fun HomeScreen(
    onSelectConversation: (id: Int) -> Unit,
    onLogOut: () -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    // Collect state flow
    val currentUser by viewModel.currentUser.collectAsStateWithLifecycle()
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val conversations by viewModel.conversations.collectAsStateWithLifecycle()

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = dimensionResource(R.dimen.padding_medium))
    ) {
        item {
            Text("Hello, ${currentUser?.username}")
        }

        item {
            Button(
                onClick = onLogOut
            ){
                Text(stringResource(R.string.log_out_button))
            }
        }

        item {
            Button(
                onClick = { onSelectConversation(-1) }
            ){
                Text(stringResource(R.string.new_conversation))
            }
        }

        // Loading icon
        if (state.loading) item {
            LoadingSpinner(stringResource(R.string.fetching_conversations))
        }

        // List all conversations
        items(conversations) { conversation ->
            ConversationCard(
                conversation.title,
                { onSelectConversation(conversation.id) },
                conversation.summary,
                conversation.timestamp
            )
        }
    }
}
