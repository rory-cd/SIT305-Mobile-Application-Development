package com.rorycd.learningassistant.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rorycd.learningassistant.data.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * View model for [LoginScreen]. Manages UI
 */
class LoginViewModel(private val userRepo: UserRepository) : ViewModel() {
    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    // UI
    fun onUsernameChanged(newUsername: String) {
        _uiState.update {
            val updated = it.copy(username = newUsername)
            updated.copy(isValid = isValid(updated))
        }
    }

    fun onPasswordChanged(newPassword: String) {
        _uiState.update {
            val updated = it.copy(password = newPassword)
            updated.copy(isValid = isValid(updated))
        }
    }

    // Database
    fun login(onSuccess: () -> Unit, onFailure: () -> Unit) {
        val currentState = _uiState.value

        viewModelScope.launch {
            val success = userRepo.login(currentState.username.trim(), currentState.password)
            if (success) onSuccess()
            else onFailure()
        }
    }

    // Helper
    fun isValid(state: LoginUiState): Boolean {
        val currentState = _uiState.value
        return with(state) {
            currentState.username.isNotBlank() && currentState.password.isNotBlank()
        }
    }
}
