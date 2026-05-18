package com.rorycd.bowerbird.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rorycd.bowerbird.data.PreferencesDataStore
import com.rorycd.bowerbird.workers.WorkManagerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * View model for [SettingsScreen]
 */
@HiltViewModel
class SettingsViewModel @Inject constructor (
    private val dataStore: PreferencesDataStore,
    private val workManagerRepository: WorkManagerRepository
) : ViewModel() {

    val scanInterval: StateFlow<Int> = dataStore.scanIntervalFlow()
        // Default to every 15 mins
        .map { it ?: 15 }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = 15
        )

    val processInterval: StateFlow<Int> = dataStore.processingIntervalFlow()
        // Default to every hour
        .map { it ?: 60 }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = 60
        )

    fun setScanInterval(value: Int) {
        viewModelScope.launch {
            dataStore.setScanInterval(value)
            workManagerRepository.scheduleFolderScan(scanInterval.value.toLong())
        }
    }

    fun setProcessingInterval(value: Int) {
        viewModelScope.launch {
            dataStore.setProcessingInterval(value)
            workManagerRepository.scheduleFileProcessing(processInterval.value.toLong())
        }
    }

    fun scanFoldersNow() {
        workManagerRepository.immediateScan()
    }

    fun processImagesNow() {
        workManagerRepository.immediateProcess()
    }
}
