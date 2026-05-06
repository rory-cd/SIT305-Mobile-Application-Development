package com.rorycd.chatbot.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class PreferencesDataStore(private val context: Context) {

    companion object val CURRENT_USER_ID = intPreferencesKey("current_user")

    fun currentUserIdFlow(): Flow<Int?> = context.dataStore.data.map { prefs ->
        prefs[CURRENT_USER_ID]
    }

    suspend fun setCurrentUserId(newId: Int) {
        context.dataStore.edit { prefs ->
            prefs[CURRENT_USER_ID] = newId
        }
    }

    suspend fun clearCurrentUser() {
        context.dataStore.edit { prefs ->
            prefs.remove(CURRENT_USER_ID)
        }
    }
}
