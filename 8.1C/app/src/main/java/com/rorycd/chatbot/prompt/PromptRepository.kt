package com.rorycd.chatbot.prompt

import android.net.Uri
import com.rorycd.chatbot.data.ChatMessage
import kotlinx.coroutines.flow.Flow

interface PromptRepository {
    suspend fun loadModel()
    suspend fun getResponseAsync(prompt: String, uri: Uri? = null): Flow<String>?
    suspend fun getResponse(prompt: String, uri: Uri? = null): String?
    fun startConversation(userName: String)
    fun resumeConversation(userName: String, initialMessages: List<ChatMessage>)
    fun endConversation()
}
