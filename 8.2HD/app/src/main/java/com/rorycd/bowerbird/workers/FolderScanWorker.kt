package com.rorycd.bowerbird.workers

import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import android.content.Context
import android.net.Uri
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
                            Log.d(TAG, "Checking file...")
                            // If the file is valid and hasn't been processed yet, action it
                            if (isSupportedImage(file.uri) &&
                                    !repo.fileHasBeenProcessedIn(file.uri, folder)) {
                                applyRules(file.uri)
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

    fun isSupportedImage(file: Uri): Boolean {
        val type = applicationContext.contentResolver.getType(file) ?: return false

        return when (type) {
            "image/jpeg",
            "image/png",
            "image/webp" -> true
            else -> false
        }
    }

    fun applyRules(file: Uri) {
        Log.d(TAG, "Updating file $file")
    }
}
