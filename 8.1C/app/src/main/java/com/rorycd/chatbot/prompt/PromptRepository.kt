package com.rorycd.chatbot.prompt

import android.net.Uri
import com.rorycd.chatbot.data.ChatMessage
import kotlinx.coroutines.flow.Flow

interface PromptRepository {
    suspend fun loadModel()
    suspend fun getResponse(prompt: String, uri: Uri?): Flow<String>?
    fun startConversation(userName: String)
    fun resumeConversation(userName: String, summary: String, initialMessages: List<ChatMessage>)
    fun endConversation()
}
