package com.rorycd.chatbot.data

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Repository for managing [Conversation] and [ChatMessage] data
 */
@Singleton
class ConversationRepository @Inject constructor(
    private val conversationDao: ConversationDao,
    private val messageDao: MessageDao
) {
    // Conversations
    fun getConversations(userId: Int): Flow<List<Conversation>> =
        conversationDao.getConversationsForUser(userId)

    fun getConversationFlow(id: Int): Flow<Conversation?> =
        conversationDao.getConversationFlowById(id)

    suspend fun createConversation(userId: Int): Long =
        conversationDao.insertConversation(Conversation(userId = userId))

    suspend fun renameConversation(id: Int, newName: String) =
        conversationDao.updateConversationTitle(id, newName)

    suspend fun deleteConversation(id: Int) =
        conversationDao.deleteConversation(id)

    // Messages
    fun getMessages(conversationId: Int): Flow<List<ChatMessage>> =
        messageDao.getMessagesByConversation(conversationId)

    suspend fun getMessageCount(conversationId: Int): Int =
        messageDao.getMessageCount(conversationId)

    suspend fun createMessage(conversationId: Int, text: String, isFromUser: Boolean) =
        messageDao.insertMessage(ChatMessage(
            text = text,
            conversationId = conversationId,
            isFromUser = isFromUser
        ))

    fun changeMessage(id: Int, newText: String) =
        messageDao.updateMessageText(id, newText)

    suspend fun deleteMessage(id: Int) =
        messageDao.deleteMessage(id)
}
