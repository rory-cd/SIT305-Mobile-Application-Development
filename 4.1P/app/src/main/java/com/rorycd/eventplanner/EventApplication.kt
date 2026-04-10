package com.rorycd.eventplanner

import android.app.Application
import com.rorycd.eventplanner.data.AppContainer
import com.rorycd.eventplanner.data.AppDataContainer

class EventApplication : Application() {

    // AppContainer instance used by the rest of the classes
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = AppDataContainer(this)
    }
}
