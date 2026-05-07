package com.rorycd.chatbot.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.Button
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.rorycd.chatbot.R
import com.rorycd.chatbot.navigation.NavigationDestination
import com.rorycd.chatbot.ui.components.ChatBotAppBar
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
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val conversations by viewModel.conversations.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {ChatBotAppBar(
            title = stringResource(R.string.home_screen_title),
            canGoBack = false,
            navigateUp = {},
            logOut = onLogOut
        )},
        floatingActionButton = {
            FloatingActionButton(
            onClick = { onSelectConversation(-1) }
        ) {
            Icon(
                imageVector = Icons.Outlined.Add,
                contentDescription = stringResource(R.string.new_conversation)
            )
        }},
        floatingActionButtonPosition = FabPosition.End
    ) { innerPadding ->
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            // No conversations to show
            if (conversations.isEmpty()) item {
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = stringResource(R.string.no_conversations),
                        textAlign = TextAlign.Center
                    )
                    TextButton(
                        onClick = { onSelectConversation(-1) }
                    ) {
                        Text(
                            stringResource(R.string.start_chatting),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }

            // List all conversations
            items(conversations) { conversation ->
                ConversationCard(
                    conversation.title,
                    { onSelectConversation(conversation.id) },
                    conversation.timestamp
                )
            }
        }
    }
}
