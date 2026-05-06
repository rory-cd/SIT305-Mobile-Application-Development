package com.rorycd.chatbot.ui.chat

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.rorycd.chatbot.R
import com.rorycd.chatbot.navigation.NavigationDestination
import com.rorycd.chatbot.ui.components.ChatBubble
import com.rorycd.chatbot.ui.components.LoadingSpinner
import com.rorycd.chatbot.ui.components.TextInputField
import java.util.Date

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
    viewModel: ChatViewModel = hiltViewModel()
) {
    // Collect state flow
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val messages by viewModel.messages.collectAsStateWithLifecycle()

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = dimensionResource(R.dimen.padding_medium))
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
                    color = if (message.isFromUser) {
                        colorResource(R.color.purple_200)
                    } else {
                        colorResource(R.color.teal_200)
                    }
                )
            }
        }

        if (state.isAwaitingResponse && state.streamingResponse.isNotEmpty()) {
            item {
                ChatBubble(
                    text = state.streamingResponse,
                    color = colorResource(R.color.teal_200)
                )
            }
        }

        item {
            TextInputField(
                value = state.userInput,
                onValueChange = { viewModel.onUserInputChanged(it) },
                label = stringResource(R.string.user_input),
                modifier = Modifier.padding(top = 32.dp)
            )
            Button(
                enabled = state.userInput.isNotEmpty(),
                onClick = {
                    viewModel.postMessage()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
            ) {
                Text(
                    text = stringResource(R.string.send_button),
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
