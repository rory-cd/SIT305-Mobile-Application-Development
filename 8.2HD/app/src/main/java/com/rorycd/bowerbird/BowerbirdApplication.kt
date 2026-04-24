package com.rorycd.bowerbird

import android.app.Application
import com.rorycd.bowerbird.data.AppContainer
import com.rorycd.bowerbird.data.AppDataContainer

/**
 * Application subclass
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
