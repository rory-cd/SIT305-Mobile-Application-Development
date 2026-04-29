package com.rorycd.lostandfound.data

import android.content.Context

/**
 * App container for dependency injection of [AdvertRepository]
 */
interface AppContainer {
    val advertRepository: AdvertRepository
}

/**
 * AppContainer implementation that provides instance of [OfflineAdvertRepository]
 */
class AppDataContainer(private val context: Context) : AppContainer {
    override val advertRepository: OfflineAdvertRepository by lazy {
        OfflineAdvertRepository(AdvertDatabase.getDatabase(context).advertDao())
    }
}
