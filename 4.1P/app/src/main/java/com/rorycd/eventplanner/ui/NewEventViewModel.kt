package com.rorycd.eventplanner.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rorycd.eventplanner.data.Event
import com.rorycd.eventplanner.data.EventsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class NewEventViewModel(private val eventsRepository: EventsRepository) : ViewModel() {
    private val _uiState = MutableStateFlow(EventUiState())
    val uiState: StateFlow<EventUiState> = _uiState.asStateFlow()

    // UI Logic
    fun onTitleChanged(newTitle: String) {
        _uiState.update {
            val updated = it.copy(currentTitle = newTitle)
            updated.copy(isValid = isValid(updated))
        }
    }

    fun onLocationChanged(newLocation: String) {
        _uiState.update { it.copy(currentLocation = newLocation) }
    }

    fun onCategoryChanged(newCategory: String) {
        _uiState.update { it.copy(currentCategory = newCategory) }
    }

    fun onDateChanged(millis: Long?) {
        val dateString = millis?.let { convertMillisToDate(it) } ?: ""
        _uiState.update {
            val updated = it.copy(currentDate = dateString)
            updated.copy(isValid = isValid(updated))
        }
    }

    fun convertMillisToDate(millis: Long): String {
        val formatter = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())
        return formatter.format(Date(millis))
    }

    fun saveEvent() {
        viewModelScope.launch {
            val currentState = uiState.value

            val newEvent = Event(
                title = currentState.currentTitle,
                location = currentState.currentLocation,
                date = currentState.currentDate,
                category = currentState.currentCategory
            )
            eventsRepository.insertEvent(newEvent)
        }
    }

    fun isValid(state: EventUiState): Boolean {
        return with(state) {
            currentTitle.isNotBlank() &&
            currentDate.isNotBlank()
        }
    }
}
