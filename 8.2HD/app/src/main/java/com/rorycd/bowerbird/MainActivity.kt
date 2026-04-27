package com.rorycd.bowerbird

import android.content.Context
import android.content.Intent
import java.util.concurrent.TimeUnit
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.lifecycle.lifecycleScope
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.rorycd.bowerbird.data.AppDatabase
import com.rorycd.bowerbird.data.FolderRepository
import com.rorycd.bowerbird.ui.theme.BowerbirdTheme
import com.rorycd.bowerbird.workers.ApplyRulesWorker
import com.rorycd.bowerbird.workers.EnqueueImagesWorker
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Get database and repo
        val db = AppDatabase.getDatabase(applicationContext)
        val repo = FolderRepository(
            db.folderDao(),
            db.scannedFileDao()
        )

//        // Get folder
//        val folderSelectLauncher = registerForActivityResult(ActivityResultContracts.OpenDocumentTree()) { uri ->
//            if (uri != null) {
//                Log.e("FOLDER", uri.toString());
//
//                val uri = uri
//
//                // Retain permissions to access this folder after restart
//                val takeFlags: Int = Intent.FLAG_GRANT_READ_URI_PERMISSION or
//                        Intent.FLAG_GRANT_WRITE_URI_PERMISSION
//                // Check for the freshest data.
//                contentResolver.takePersistableUriPermission(uri, takeFlags)
//
//                // Add folder to watched folders
//                lifecycleScope.launch {
//                    repo.addFolder(uri)
//                }
//            }
//            // Perform a scan on opening app
//            val immediateScan = OneTimeWorkRequestBuilder<EnqueueImagesWorker>().build()
//            WorkManager.getInstance(this).enqueue(immediateScan)
//        }
//        folderSelectLauncher.launch(null)
//
//
//        // Schedule ongoing scans
//        scheduleFolderScan(this)
//        scheduleFileProcessing(this)

        // Perform a scan on opening app
        val immediateScan2 = OneTimeWorkRequestBuilder<EnqueueImagesWorker>().build()
        WorkManager.getInstance(this).enqueue(immediateScan2)

//        val immediateScan = OneTimeWorkRequestBuilder<ApplyRulesWorker>().build()
//        WorkManager.getInstance(this).enqueue(immediateScan)

        scheduleFileProcessing(this)

        // UI
        enableEdgeToEdge()
        setContent {
            BowerbirdTheme {
                Column {
                    Text("Test")
                }
            }
        }
    }
}

fun scheduleFolderScan(context: Context) {
    val scanBuilder = PeriodicWorkRequestBuilder<EnqueueImagesWorker>(
        repeatInterval = 15,
        repeatIntervalTimeUnit = TimeUnit.MINUTES,
        flexTimeInterval = 5,
        flexTimeIntervalUnit = TimeUnit.MINUTES
    )
    val workManager = WorkManager.getInstance(context)

    workManager.enqueueUniquePeriodicWork(
        "folder_scan_work",
        androidx.work.ExistingPeriodicWorkPolicy.KEEP, // If there's a match, keep existing running
        scanBuilder.build()
    )
}

fun scheduleFileProcessing(context: Context) {
    val scanBuilder = PeriodicWorkRequestBuilder<ApplyRulesWorker>(
        repeatInterval = 15,
        repeatIntervalTimeUnit = TimeUnit.MINUTES,
        flexTimeInterval = 5,
        flexTimeIntervalUnit = TimeUnit.MINUTES
    )
    val workManager = WorkManager.getInstance(context)

    workManager.enqueueUniquePeriodicWork(
        "apply_rules_work",
        androidx.work.ExistingPeriodicWorkPolicy.KEEP, // If there's a match, keep existing running
        scanBuilder.build()
    )
}
