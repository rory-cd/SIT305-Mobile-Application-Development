package com.rorycd.learningassistant.ui.interestselect

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rorycd.learningassistant.data.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * View model for [InterestSelectScreen]. Manages UI
 */
class InterestSelectViewModel(private val userRepo: UserRepository) : ViewModel() {
    private val _uiState = MutableStateFlow(InterestUiState())
    val uiState: StateFlow<InterestUiState> = _uiState.asStateFlow()

    // UI
    fun toggleInterest(interest: String) {
        _uiState.update {
            if (!it.selected.contains(interest))
                it.copy(selected = it.selected + interest)
            else
                it.copy(selected = it.selected - interest)
        }
    }

    // Repo
    fun submitInterests() {
        viewModelScope.launch {
            userRepo.addInterests(_uiState.value.selected)
        }
    }
}
