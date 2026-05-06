package com.rorycd.chatbot.ui.chat

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rorycd.chatbot.data.ConversationRepository
import com.rorycd.chatbot.data.User
import com.rorycd.chatbot.data.UserRepository
import com.rorycd.chatbot.prompt.PromptRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.text.get

/**
 * View model for [ChatScreen]
 */
@HiltViewModel
class ChatViewModel @Inject constructor (
    savedStateHandle: SavedStateHandle,
    private val userRepo: UserRepository,
    private val conversationRepo: ConversationRepository,
    private val promptRepo: PromptRepository
) : ViewModel() {

    // UI state
    private val _uiState = MutableStateFlow(ChatUiState())
    val uiState: StateFlow<ChatUiState> = _uiState.asStateFlow()

    private var currentUser: User? = null
    private var conversationId: Int = -1

    val messages = conversationRepo.getMessages(1)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    fun onUserInputChanged(newInput: String) {
        _uiState.update {
            it.copy(userInput = newInput)
        }
    }

    fun postMessage() {
        val input = _uiState.value.userInput
        if (input.isEmpty() || currentUser == null) return

        viewModelScope.launch {
            // Add user input to DB
            conversationRepo.createMessage(conversationId, input,true)

            // Clear ui input
            _uiState.update { it.copy(userInput = "", isAwaitingResponse = true) }

            // Prompt LLM
            try {
                var response = ""
                promptRepo.getResponse(input, null)
                    ?.onCompletion { cause ->
                        if (cause == null) {
                            _uiState.update { it.copy(
                                streamingResponse = ""
                            ) }
                        } else {
                            _uiState.update { it.copy(error = cause.message.toString()) }
                        }
                    }
                    ?.collect { chunk ->
                        response += chunk
                        _uiState.update { it.copy(streamingResponse = it.streamingResponse + chunk)
                    }
                }

                if (response.isNotEmpty())
                    conversationRepo.createMessage(currentUser!!.id, response, false)
            } catch(e: Exception) {
                _uiState.update { it.copy(error = "Failed to get a response") }
            } finally {
                _uiState.update { it.copy(isAwaitingResponse = false) }
            }
        }
    }

    init {
        viewModelScope.launch {
            currentUser = userRepo.getCurrentUser() ?: return@launch

            conversationId = checkNotNull(savedStateHandle["conversationId"])

            // If it's a new conversation, add it to the DB
            if (conversationId == -1)
                conversationId = conversationRepo.createConversation(currentUser!!.id).toInt()

            promptRepo.startConversation(currentUser!!.username)
        }
    }
}
