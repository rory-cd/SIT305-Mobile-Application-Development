package com.rorycd.chatbot.ui.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.rorycd.chatbot.R
import com.rorycd.chatbot.navigation.NavigationDestination
import com.rorycd.chatbot.ui.components.ChatBotAppBar
import com.rorycd.chatbot.ui.components.ChatBubble
import com.rorycd.chatbot.ui.components.ChatInput
import com.rorycd.chatbot.ui.components.TextInputField

/**
 * Destination class for NavGraph route to [ChatScreen]
 */
object ChatDestination : NavigationDestination {
    override val route = "chat"
    override val titleRes = R.string.chat_destination_title

    const val CONVERSATION_ID_ARG = "conversationId"

    val routeWithArgs = "${route}/{$CONVERSATION_ID_ARG}"
}

/**
 * Screen to allow users to chat with AI
 */
@Composable
fun ChatScreen(
    onNavigateUp: () -> Unit,
    onLogOut: () -> Unit,
    viewModel: ChatViewModel = hiltViewModel()
) {
    // Collect state flow
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val conversation by viewModel.conversation.collectAsStateWithLifecycle()
    val messages by viewModel.messages.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {ChatBotAppBar(
            title = conversation?.title ?: stringResource(R.string.new_conversation_title),
            canGoBack = true,
            navigateUp = onNavigateUp,
            logOut = onLogOut
        )}
    ) { innerPadding ->
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            // List all messages
            items(messages) { message ->
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = if (message.isFromUser) Alignment.End else Alignment.Start
                ) {
                    ChatBubble(
                        text = message.text,
                        timeStamp = message.timestamp,
                        showIcon = !message.isFromUser,
                        color = if (message.isFromUser) {
                            MaterialTheme.colorScheme.secondaryContainer
                        } else {
                            MaterialTheme.colorScheme.primaryContainer
                        }
                    )
                }
            }

            // Show chat bubble for streaming response
            if (state.isAwaitingResponse && state.streamingResponse.isNotEmpty()) {
                item {
                    ChatBubble(
                        text = state.streamingResponse,
                        color = MaterialTheme.colorScheme.primaryContainer,
                        showIcon = true
                    )
                }
            }

            item {
                ChatInput(
                    value = state.userInput,
                    isEnabled = state.userInput.isNotEmpty(),
                    onChange = { viewModel.onUserInputChanged(it) },
                    onSend = { viewModel.postMessage() },
                    modifier = Modifier.padding(top = 32.dp)
                )
            }
        }
    }
}
