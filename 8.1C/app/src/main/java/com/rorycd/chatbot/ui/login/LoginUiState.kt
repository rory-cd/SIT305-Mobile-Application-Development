package com.rorycd.chatbot.ui.login

/**
 * Represents the current state of the [LoginScreen] UI
 */
data class LoginUiState(
    val username: String = "",
    val password: String = "",
    val isValid: Boolean = false
)
