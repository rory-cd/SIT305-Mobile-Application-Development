package com.rorycd.bowerbird.workers

import android.content.Context
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WorkManagerRepository @Inject constructor (
    @ApplicationContext private val context: Context
) {
    private val workManager by lazy { WorkManager.getInstance(context) }

    fun scheduleFolderScan(interval: Long) {
        val scanBuilder = PeriodicWorkRequestBuilder<EnqueueImagesWorker>(
            repeatInterval = interval,
            repeatIntervalTimeUnit = TimeUnit.MINUTES,
            flexTimeInterval = 5,
            flexTimeIntervalUnit = TimeUnit.MINUTES
        )

        workManager.enqueueUniquePeriodicWork(
            "folder_scan_work",
            androidx.work.ExistingPeriodicWorkPolicy.UPDATE,
            scanBuilder.build()
        )
    }

    fun scheduleFileProcessing(interval: Long) {
        val scanBuilder = PeriodicWorkRequestBuilder<ApplyRulesWorker>(
            repeatInterval = interval,
            repeatIntervalTimeUnit = TimeUnit.MINUTES,
            flexTimeInterval = 5,
            flexTimeIntervalUnit = TimeUnit.MINUTES
        )

        workManager.enqueueUniquePeriodicWork(
            "apply_rules_work",
            androidx.work.ExistingPeriodicWorkPolicy.UPDATE,
            scanBuilder.build()
        )
    }

    fun immediateScan() {
        val immediateScan = OneTimeWorkRequestBuilder<EnqueueImagesWorker>().build()
        workManager.enqueue(immediateScan)
    }

    fun immediateProcess() {
        val immediateProcess = OneTimeWorkRequestBuilder<ApplyRulesWorker>().build()
        workManager.enqueue(immediateProcess)
    }
}
