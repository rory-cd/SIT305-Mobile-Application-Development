package com.rorycd.chatbot.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rorycd.chatbot.data.ConversationRepository
import com.rorycd.chatbot.data.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

/**
 * View model for [HomeScreen]
 */
@HiltViewModel
class HomeViewModel @Inject constructor (
    private val userRepo: UserRepository,
    private val conversationRepo: ConversationRepository
) : ViewModel() {

    // UI state
    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    val currentUser = userRepo.getCurrentUserFlow()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = null
        )

    // Conversation list - reacts to current user changing
    @OptIn(ExperimentalCoroutinesApi::class)
    val conversations = userRepo.getCurrentUserFlow()
        .flatMapLatest { user ->
            if (user == null) flowOf(emptyList())
            else conversationRepo.getConversations(user.id)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )
}
