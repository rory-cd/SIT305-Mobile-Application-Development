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
import com.rorycd.bowerbird.prompt.GeminiNanoRepository
import com.rorycd.bowerbird.ui.theme.BowerbirdTheme
import com.rorycd.bowerbird.workers.FolderScanWorker
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val repoTest = GeminiNanoRepository()
        lifecycleScope.launch {
            repoTest.loadModel()
            val response = repoTest.getResponse("Give me two sentences describing lizard habitats.")
            Log.e("RESPONSE", response)
        }

        // UI
        enableEdgeToEdge()
        setContent {
            BowerbirdTheme {
                Column {
                    Text("Test")
                }
            }
        }

//        // Get database and repo
//        val db = AppDatabase.getDatabase(applicationContext)
//        val repo = FolderRepository(
//            db.folderDao(),
//            db.processedFileDao()
//        )
//
//        // Get folder
//        val folderSelectLauncher = registerForActivityResult(ActivityResultContracts.OpenDocumentTree()) { uri ->
//            if (uri != null) {
//                Log.e("FOLDER", uri.toString());
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
//        }
//        folderSelectLauncher.launch(null)
//
//        // Perform a scan on opening app
//        val immediateScan = OneTimeWorkRequestBuilder<FolderScanWorker>().build()
//        WorkManager.getInstance(this).enqueue(immediateScan)
//
//        // Schedule ongoing scans
//        scheduleFolderScan(this)
    }
}

fun scheduleFolderScan(context: Context) {
    val scanBuilder = PeriodicWorkRequestBuilder<FolderScanWorker>(
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
