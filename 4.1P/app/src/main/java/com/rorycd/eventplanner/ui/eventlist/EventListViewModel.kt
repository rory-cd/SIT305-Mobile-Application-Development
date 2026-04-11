package com.rorycd.eventplanner.ui.eventlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rorycd.eventplanner.data.Event
import com.rorycd.eventplanner.data.EventsRepository
import com.rorycd.eventplanner.utils.formatDateAsString
import com.rorycd.eventplanner.utils.formatTimeAsString
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

/**
 * View model for [EventListScreen]. Supplies a list of all events sorted by date
 */
class EventListViewModel(private val eventsRepository: EventsRepository) : ViewModel() {
    val events: StateFlow<List<EventUiModel>> = eventsRepository.getAllEventsStream()
        // For each event, map it to a ui model
        .map { events -> events.map { it.toUiModel() } }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
}

/**
 * Extension function for Event, used to create an [EventUiModel] from an [Event]
 */
private fun Event.toUiModel() = EventUiModel(
    id = id,
    title = title,
    location = location,
    category = category,
    dateFormatted = formatDateAsString(timeStamp),
    timeFormatted = formatTimeAsString(timeStamp)
)
