package com.rorycd.lostandfound

import android.app.Application
import com.rorycd.lostandfound.data.AppContainer
import com.rorycd.lostandfound.data.AppDataContainer

/**
 * Application subclass providing AdvertRepository via dependency injection
 */
class LostAndFoundApplication : Application() {

    // AppContainer instance used by the rest of the classes
    lateinit var container: AppContainer

    // On app creation, instantiate an AppDataContainer (with advert repository)
    override fun onCreate() {
        super.onCreate()
        container = AppDataContainer(this)
    }
}
