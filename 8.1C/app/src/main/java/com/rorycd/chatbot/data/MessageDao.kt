package com.rorycd.chatbot.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface MessageDao {
    @Insert
    suspend fun insertMessage(chatMessage: ChatMessage) : Long

    @Query("UPDATE messages SET text = :newText WHERE id = :id")
    fun updateMessageText(id: Int, newText: String)

    @Query("DELETE FROM messages WHERE id = :id")
    suspend fun deleteMessage(id: Int)

    @Query("SELECT * FROM messages WHERE conversationId = :conversationId ORDER BY timestamp ASC")
    fun getMessagesByConversation(conversationId: Int): Flow<List<ChatMessage>>

    @Query("SELECT COUNT(*) FROM messages WHERE conversationId = :conversationId")
    suspend fun getMessageCount(conversationId: Int): Int
}
