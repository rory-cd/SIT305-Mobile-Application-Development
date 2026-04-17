package com.rorycd.bowerbird.workers

import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import android.content.Context
import android.util.Log
import androidx.documentfile.provider.DocumentFile
import com.rorycd.bowerbird.data.AppDatabase
import com.rorycd.bowerbird.data.FolderRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

private const val TAG = "FolderScanWorker"

class FolderScanWorker(context: Context, params: WorkerParameters) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        val db = AppDatabase.getDatabase(applicationContext)
        val repo = FolderRepository(
            db.folderDao(),
            db.processedFileDao()
        )

//        val contentResolver = applicationContext.contentResolver

        // Run on IO thread (more suited for this)
        return withContext(Dispatchers.IO) {
            return@withContext try {
                Log.d(TAG, "Scanning...")

                val folders = repo.getAllWatchedFolders()

                for (folder in folders) {
                    Log.d(TAG, "Scanning folder ${folder.toString()}")
                    // Convert to DocumentFile for scanning
                    val folderDf = DocumentFile.fromTreeUri(applicationContext, folder)

                    if (folderDf != null) {
                        // Get all files
                        val files = folderDf.listFiles()

                        for (file in files) {
                            // If the file hasn't been processed yet, action it
                            Log.d(TAG, "Checking file...")

                            if (!repo.fileHasBeenProcessedIn(file.uri, folder)) {
                                Log.d(TAG, "Updating file ${file.uri.toString()}")
                            }
                        }
                    }
                }
                Result.success()
            } catch (throwable: Throwable) {
                Log.e(TAG, "Failed", throwable)
                Result.failure()
            }
        }
    }
}
