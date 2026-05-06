package com.rorycd.chatbot.ui.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rorycd.chatbot.data.UserRepository
import com.rorycd.chatbot.R
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * View model for [RegisterScreen]
 */
@HiltViewModel
class RegisterViewModel @Inject constructor (
    private val userRepo: UserRepository
) : ViewModel() {

    // UI state
    private val _uiState = MutableStateFlow(RegisterUiState())
    val uiState: StateFlow<RegisterUiState> = _uiState.asStateFlow()

    // UI management
    fun onUsernameChanged(newUsername: String) {
        _uiState.update { it.copy(
            username = newUsername,
            usernameError = null
        ) }
    }

    fun onPasswordChanged(newPassword: String) {
        _uiState.update { it.copy(
            password = newPassword,
            passwordError = null
        ) }
    }

    fun onConfirmPasswordChanged(newPassword: String) {
        _uiState.update { it.copy(
            confirmPassword = newPassword,
            confirmPasswordError = null
        ) }
    }

    // Database
    fun register(onSuccess: () -> Unit, onFailure: () -> Unit) {
        val currentState = _uiState.value

        viewModelScope.launch {
            if (formIsValid()) {
                // Try to register
                val success = userRepo.register(
                    username = currentState.username.trim(),
                    password = currentState.password
                )
                if (success) onSuccess()
                else onFailure()
            }
        }
    }

    // Form validation
    suspend fun formIsValid(): Boolean {
        val currentState = _uiState.value

        // Check if username already exists, or is empty
        val usernameErr = if (userRepo.userExists(currentState.username)) R.string.username_taken
        else if (currentState.username.isEmpty()) R.string.username_empty
        else null

        // Check password is long enough and matches
        val passwordErr = if (currentState.password.length < 6) R.string.password_length
        else null
        val confirmPassErr = if (currentState.password != currentState.confirmPassword) R.string.password_match
        else null

        _uiState.update {
            it.copy(
                usernameError = usernameErr,
                passwordError = passwordErr,
                confirmPasswordError = confirmPassErr
            )
        }
        return usernameErr == null && passwordErr == null && confirmPassErr == null
    }
}
