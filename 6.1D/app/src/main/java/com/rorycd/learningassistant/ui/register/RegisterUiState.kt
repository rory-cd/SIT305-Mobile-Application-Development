package com.rorycd.learningassistant.ui.register

/**
 * Represents the current state of the [RegisterScreen] UI
 */
data class RegisterUiState (
    val username: String = "",
    val usernameError: Int? = null,
    val password: String = "",
    val passwordError: Int? = null,
    val email: String = "",
    val emailError: Int? = null,
    val confirmPassword: String = "",
    val confirmPasswordError: Int? = null,
    val imgUri: String = ""
)
