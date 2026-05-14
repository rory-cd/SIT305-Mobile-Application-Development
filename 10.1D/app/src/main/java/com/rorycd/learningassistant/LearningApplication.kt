package com.rorycd.learningassistant

import android.app.Application
import com.rorycd.learningassistant.data.AppContainer

/**
 * Application subclass providing repositories via dependency injection
 */
class LearningApplication : Application() {

    // AppContainer instance used by the rest of the classes
    lateinit var container: AppContainer

    // On app creation, instantiate an AppContainer
    override fun onCreate() {
        super.onCreate()
        container = AppContainer(this)
    }
}
