package com.rorycd.eventplanner.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rorycd.eventplanner.data.Event
import com.rorycd.eventplanner.data.EventsRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class EventListViewModel(private val eventsRepository: EventsRepository) : ViewModel() {
    val events: StateFlow<List<Event>> = eventsRepository.getAllEventsStream()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
}
