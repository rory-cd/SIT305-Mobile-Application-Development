package com.rorycd.chatbot.ui.chat

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rorycd.chatbot.data.Conversation
import com.rorycd.chatbot.data.ConversationRepository
import com.rorycd.chatbot.data.User
import com.rorycd.chatbot.data.UserRepository
import com.rorycd.chatbot.prompt.PromptRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

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

    private var _currentUser: User? = null

    private val conversationId = MutableStateFlow(-1)

    @OptIn(ExperimentalCoroutinesApi::class)
    val conversation = conversationId
        .flatMapLatest { id ->
            if (id == -1) {
                flowOf(null)
            } else {
                conversationRepo.getConversationFlow(id)
            }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)

    @OptIn(ExperimentalCoroutinesApi::class)
    val messages = conversation
        .filterNotNull()
        .flatMapLatest { c ->
            conversationRepo.getMessages(c.id)
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList()
        )

    fun onUserInputChanged(newInput: String) {
        _uiState.update {
            it.copy(userInput = newInput)
        }
    }

    fun postMessage() {
        val input = _uiState.value.userInput
        val conversation = conversation.value
        if (input.isEmpty() || _currentUser == null || conversation == null) return

        viewModelScope.launch {
            // Add user input to DB
            conversationRepo.createMessage(conversation.id, input,true)
            incrementMessageCounter()

            // Clear ui input
            _uiState.update { it.copy(userInput = "", isAwaitingResponse = true) }

            // Update conversation title/summary
            updateTitle(conversation, input)

            // Prompt LLM
            try {
                val response = getResponse(input)
                if (response.isNotEmpty()) {
                    conversationRepo.createMessage(conversation.id, response, false)
                    incrementMessageCounter()
                }
            } catch(e: Exception) {
                _uiState.update { it.copy(error = "Failed to get a response") }
            } finally {
                _uiState.update { it.copy(isAwaitingResponse = false) }
            }
        }
    }

    private fun incrementMessageCounter() {
        _uiState.update { it.copy(sessionMsgCount = _uiState.value.sessionMsgCount + 1) }
    }

    private suspend fun updateTitle(conversation: Conversation, input: String) {
        val msgCount = conversationRepo.getMessageCount(conversation.id)
        val sessionMsgCount = _uiState.value.sessionMsgCount;

        // Rename conversation if it's the first message, or there have been 10 new messages since the last update
        if (msgCount == 1) {
            val newTitle = promptRepo.getResponse(
                "Create a title for this chat, using 5 words or less, based on the initial prompt, which is '$input'. Respond with ONLY the title."
            )
            if (newTitle != null)
                conversationRepo.renameConversation(conversation.id, newTitle)
        } else if (sessionMsgCount % 10 == 0) {
            val newTitle = promptRepo.getResponse(
                "If you were to summarise this chat into a <5 word title, what would you call it? Respond with ONLY the title."
            )
            if (newTitle != null)
                conversationRepo.renameConversation(conversation.id, newTitle)
        }
        val history = conversationRepo.getMessages(
            conversationId.value).filter { it.isNotEmpty() }.first()

        // Restart conversation before the title was prompted
        promptRepo.resumeConversation(_currentUser!!.username, history)
    }

    private suspend fun getResponse(input: String): String {
        var response = ""
        promptRepo.getResponseAsync(input, null)
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
        return response
    }

    init {
        viewModelScope.launch {
            val user = userRepo.getCurrentUser() ?: return@launch
            _currentUser = user

            // Get conversationId from route args
            var idFromNav: Int = checkNotNull(savedStateHandle["conversationId"])

            // If it's a new conversation
            if (idFromNav == -1) {
                val newId = conversationRepo.createConversation(_currentUser!!.id).toInt()

                conversationId.value = newId

                promptRepo.startConversation(_currentUser!!.username)
            } else {

                launch {
                    val history = conversationRepo.getMessages(
                        idFromNav).filter { it.isNotEmpty() }.first()
                    promptRepo.resumeConversation(_currentUser!!.username, history)
                    conversationId.value = idFromNav
                }
            }
        }
    }
}
