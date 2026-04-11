package com.rorycd.eventplanner.ui.newevent

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rorycd.eventplanner.data.Event
import com.rorycd.eventplanner.data.EventsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset

class NewEventViewModel(private val eventsRepository: EventsRepository) : ViewModel() {
    private val _uiState = MutableStateFlow(NewEventUiState())
    val uiState: StateFlow<NewEventUiState> = _uiState.asStateFlow()

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
            val localMillis = Instant.ofEpochMilli(millis)
                .atZone(ZoneOffset.UTC)
                .toLocalDate()
                .atStartOfDay(ZoneId.systemDefault())
                .toInstant()
                .toEpochMilli()
            val updated = it.copy(currentDate = localMillis)
            updated.copy(isValid = isValid(updated))
        }
    }

    fun onTimeChanged(hour: Int, minute: Int) {
        _uiState.update {
            val timeMins = (hour * 60) + minute
            val updated = it.copy(currentTimeMins = timeMins)
            updated.copy(isValid = isValid(updated))
        }
    }

    fun saveEvent() {
        viewModelScope.launch {
            val state = uiState.value

            val newEvent = Event(
                title = state.currentTitle,
                location = state.currentLocation.takeIf { it.isNotBlank() },
                timeStamp = state.currentDate + state.currentTimeMins,
                category = state.currentCategory.takeIf { it.isNotBlank() }
            )
            eventsRepository.insertEvent(newEvent)
        }
    }

    fun isValid(state: NewEventUiState): Boolean {
        return with(state) {
            currentTitle.isNotBlank() &&
            isInFuture(currentDate, currentTimeMins)
        }
    }

    fun isInFuture(dateMillis: Long, timeMins: Int): Boolean {
        val timeMillis = (timeMins * 60 * 1000L)
        val dateTimeMillis = dateMillis + timeMillis
        val localDateTime = Instant.ofEpochMilli(dateTimeMillis)
            .atZone(ZoneId.systemDefault())
            .toLocalDateTime()
        return !localDateTime.isBefore(LocalDateTime.now())
    }
}
