package com.rorycd.bowerbird.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

@Singleton
class PreferencesDataStore @Inject constructor(
    @ApplicationContext private val context: Context
) {
    companion object {
        val SCAN_INTERVAL = intPreferencesKey("scan_interval")
        val PROCESSING_INTERVAL = intPreferencesKey("processing_interval")
    }

    fun scanIntervalFlow(): Flow<Int?> = context.dataStore.data.map { prefs ->
        prefs[SCAN_INTERVAL]
    }

    suspend fun setScanInterval(newInterval: Int) {
        context.dataStore.edit { prefs ->
            prefs[SCAN_INTERVAL] = newInterval
        }
    }

    fun processingIntervalFlow(): Flow<Int?> = context.dataStore.data.map { prefs ->
        prefs[PROCESSING_INTERVAL]
    }

    suspend fun setProcessingInterval(newInterval: Int) {
        context.dataStore.edit { prefs ->
            prefs[PROCESSING_INTERVAL] = newInterval
        }
    }
}
