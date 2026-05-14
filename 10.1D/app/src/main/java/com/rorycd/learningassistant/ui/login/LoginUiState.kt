package com.rorycd.learningassistant.ui.login

/**
 * Represents the current state of the [LoginScreen] UI
 */
data class LoginUiState(
    val username: String = "",
    val password: String = "",
    val isValid: Boolean = false
)
