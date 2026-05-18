package com.rorycd.bowerbird

import android.app.Application
import android.content.Context
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.rorycd.bowerbird.data.PreferencesDataStore
import com.rorycd.bowerbird.workers.ApplyRulesWorker
import com.rorycd.bowerbird.workers.EnqueueImagesWorker
import com.rorycd.bowerbird.workers.WorkManagerRepository
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/**
 * Application subclass
 */
@HiltAndroidApp
class BowerbirdApplication : Application(), Configuration.Provider {

    @Inject
    lateinit var workManagerRepo: WorkManagerRepository
    @Inject
    lateinit var dataStore: PreferencesDataStore
    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    override val workManagerConfiguration
        get() = Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()

    override fun onCreate() {
        super.onCreate()

        MainScope().launch {
            val scanInterval = dataStore.scanIntervalFlow().first()?.toLong() ?: 15L
            val processInterval = dataStore.processingIntervalFlow().first()?.toLong() ?: 60L

            workManagerRepo.scheduleFolderScan(scanInterval)
            workManagerRepo.scheduleFileProcessing(processInterval)
        }
    }
}
