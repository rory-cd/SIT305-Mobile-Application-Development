package com.rorycd.eventplanner.ui.editevent

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rorycd.eventplanner.data.Event
import com.rorycd.eventplanner.data.EventsRepository
import com.rorycd.eventplanner.utils.getMinsPastMidnight
import com.rorycd.eventplanner.utils.timeStampAtMidnight
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset

/**
 * View model for [EditEventScreen]. Initialises UI and updates values for selected event
 */
class EditEventViewModel(
    private val eventsRepository: EventsRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    // Retrieve navigation parameter (event ID) from the nav host
    private val eventId: Int = checkNotNull(savedStateHandle[EditEventDestination.eventIdArg])

    private val _uiState: MutableStateFlow<EditEventUiState?> = MutableStateFlow(null)
    val uiState: StateFlow<EditEventUiState?> = _uiState.asStateFlow()

    // Initialise the UI state based on the selected event
    init {
        viewModelScope.launch {
            val event = eventsRepository.getEventStream(eventId).first()
            checkNotNull(event) { "Event with id $eventId not found" }
            _uiState.update {
                EditEventUiState(
                    currentTitle = event.title,
                    currentLocation = event.location ?: "",
                    currentCategory = event.category ?: "",
                    currentDate = timeStampAtMidnight(event.timeStamp),
                    currentTimeMins = getMinsPastMidnight(event.timeStamp),
                    isValid = true
                )
            }
        }
    }

    // UI Logic
    fun onTitleChanged(newTitle: String) {
        _uiState.update {
            val updated = it?.copy(currentTitle = newTitle)
            updated?.copy(isValid = isValid(updated))
        }
    }

    fun onLocationChanged(newLocation: String) {
        _uiState.update { it?.copy(currentLocation = newLocation) }
    }

    fun onCategoryChanged(newCategory: String) {
        _uiState.update { it?.copy(currentCategory = newCategory) }
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
            val updated = it?.copy(currentDate = localMillis)
            updated?.copy(isValid = isValid(updated))
        }
    }

    fun onTimeChanged(hour: Int, minute: Int) {
        _uiState.update {
            val timeMins = (hour * 60) + minute
            val updated = it?.copy(currentTimeMins = timeMins)
            updated?.copy(isValid = isValid(updated))
        }
    }

    // Database interaction
    fun updateEvent() {
        viewModelScope.launch {
            val state = uiState.value ?: return@launch

            val newEvent = Event(
                id = eventId,
                title = state.currentTitle,
                location = state.currentLocation.takeIf { it.isNotBlank() },
                timeStamp = state.currentDate + (state.currentTimeMins * 60 * 1000L),
                category = state.currentCategory.takeIf { it.isNotBlank() }
            )
            eventsRepository.updateEvent(newEvent)
        }
    }

    fun deleteEvent() {
        viewModelScope.launch {
            val event = eventsRepository.getEventStream(eventId).first()
            if (event != null) {
                eventsRepository.deleteEvent(event)
            }
        }
    }

    // Helpers
    fun isValid(state: EditEventUiState): Boolean {
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
