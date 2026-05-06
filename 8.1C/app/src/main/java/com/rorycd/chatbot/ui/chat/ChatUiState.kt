package com.rorycd.chatbot.ui.chat

/**
 * Represents the current state of the [ChatScreen] UI
 */
data class ChatUiState(
    val userInput: String = "",
    val error: String = "",
    val streamingResponse: String = "",
    val isAwaitingResponse: Boolean = false
)
