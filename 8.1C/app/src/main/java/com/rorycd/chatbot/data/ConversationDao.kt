package com.rorycd.chatbot.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ConversationDao {
    @Insert
    suspend fun insertConversation(conversation: Conversation) : Long

    @Query("UPDATE conversations SET title = :newTitle WHERE id = :id")
    fun updateConversationTitle(id: Int, newTitle: String)

    @Query("UPDATE conversations SET summary = :newSummary WHERE id = :id")
    fun updateConversationSummary(id: Int, newSummary: String)

    @Query("DELETE FROM conversations WHERE id = :id")
    suspend fun deleteConversation(id: Int)

    @Query("SELECT * FROM conversations WHERE userId = :userId ORDER BY timestamp DESC")
    fun getConversationsForUser(userId: Int): Flow<List<Conversation>>
}
