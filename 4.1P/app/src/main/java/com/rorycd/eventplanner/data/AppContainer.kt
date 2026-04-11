package com.rorycd.eventplanner.data

import android.content.Context

/**
 * App container for dependency injection of [EventsRepository]
 */
interface AppContainer {
    val eventsRepository: EventsRepository
}

/**
 * AppContainer implementation that provides instance of [OfflineEventsRepository]
 */
class AppDataContainer(private val context: Context) : AppContainer {
    override val eventsRepository: EventsRepository by lazy {
        OfflineEventsRepository(EventDatabase.getDatabase(context).eventDao())
    }
}
