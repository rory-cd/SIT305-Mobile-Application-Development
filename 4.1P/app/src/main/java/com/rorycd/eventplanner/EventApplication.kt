package com.rorycd.eventplanner

import android.app.Application
import com.rorycd.eventplanner.data.AppContainer
import com.rorycd.eventplanner.data.AppDataContainer

/**
 * Application subclass providing EventsRepository via dependency injection
 */
class EventApplication : Application() {

    // AppContainer instance used by the rest of the classes
    lateinit var container: AppContainer

    // On app creation, instantiate an AppDataContainer (with Events repository)
    override fun onCreate() {
        super.onCreate()
        container = AppDataContainer(this)
    }
}
