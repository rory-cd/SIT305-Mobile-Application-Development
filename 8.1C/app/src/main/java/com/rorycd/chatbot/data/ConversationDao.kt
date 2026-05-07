package com.rorycd.chatbot.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ConversationDao {
    @Insert
    suspend fun insertConversation(conversation: Conversation) : Long

    @Query("UPDATE conversations SET title = :newTitle WHERE id = :id")
    suspend fun updateConversationTitle(id: Int, newTitle: String)

    @Query("DELETE FROM conversations WHERE id = :id")
    suspend fun deleteConversation(id: Int)

    @Query("SELECT * FROM conversations WHERE id = :conversationId LIMIT 1")
    fun getConversationFlowById(conversationId: Int): Flow<Conversation>

    @Query("SELECT * FROM conversations WHERE userId = :userId ORDER BY timestamp DESC")
    fun getConversationsForUser(userId: Int): Flow<List<Conversation>>
}
