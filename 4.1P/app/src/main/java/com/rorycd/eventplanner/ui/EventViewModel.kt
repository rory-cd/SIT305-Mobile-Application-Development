package com.rorycd.eventplanner.ui

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class EventViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(EventUiState())
    val uiState: StateFlow<EventUiState> = _uiState.asStateFlow()

    fun onTitleChanged(newTitle: String) {
        _uiState.update { it.copy(currentTitle = newTitle.trim()) }
    }

    fun onLocationChanged(newLocation: String) {
        _uiState.update { it.copy(currentLocation = newLocation.trim()) }
    }

    fun onCategoryChanged(newCategory: String) {
        _uiState.update { it.copy(currentCategory = newCategory.trim()) }
    }

    fun onDateChanged(millis: Long?) {
        val dateString = millis?.let { convertMillisToDate(it) } ?: ""
        _uiState.update { it.copy(currentDate = dateString) }
    }

    fun convertMillisToDate(millis: Long): String {
        val formatter = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())
        return formatter.format(Date(millis))
    }
}
