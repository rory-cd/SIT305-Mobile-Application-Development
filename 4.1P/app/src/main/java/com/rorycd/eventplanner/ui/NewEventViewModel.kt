package com.rorycd.eventplanner.ui

import android.icu.util.Calendar
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
        if (millis == null) return
        _uiState.update {
            val updated = it.copy(currentDate = millis)
            updated.copy(isValid = isValid(updated))
        }
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
            isDateTodayOrLater(currentDate)
        }
    }

    fun isDateTodayOrLater(dateMillis: Long): Boolean {
        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        return dateMillis >= calendar.timeInMillis
    }
}
