package com.rorycd.chatbot.ui.chat

/**
 * Represents the current state of the [ChatScreen] UI
 */
data class ChatUiState(
    val userInput: String = "",
    val error: String = "",
    val streamingResponse: String = "",
    val sessionMsgCount: Int = 0,
    val isAwaitingResponse: Boolean = false
)
