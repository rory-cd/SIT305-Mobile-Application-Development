package com.rorycd.eventplanner.ui.eventlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rorycd.eventplanner.data.Event
import com.rorycd.eventplanner.data.EventsRepository
import com.rorycd.eventplanner.utils.formatDate
import com.rorycd.eventplanner.utils.formatTime
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

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

private fun Event.toUiModel() = EventUiModel(
    id = id,
    title = title,
    location = location,
    category = category,
    dateFormatted = formatDate(timeStamp),
    timeFormatted = formatTime(timeStamp)
)
