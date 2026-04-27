package com.rorycd.bowerbird.workers

import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.documentfile.provider.DocumentFile
import com.rorycd.bowerbird.data.AppDatabase
import com.rorycd.bowerbird.data.FolderRepository
import com.rorycd.bowerbird.data.QueueRepository

private const val TAG = "EnqueueImagesWorker"

class EnqueueImagesWorker(val context: Context, params: WorkerParameters) : CoroutineWorker(context, params) {

    private val db = AppDatabase.getDatabase(applicationContext)

    private val folderRepo = FolderRepository(
        db.folderDao(),
        db.scannedFileDao()
    )

    private val queueRepo = QueueRepository(
        db.queuedFileDao()
    )

    override suspend fun doWork(): Result {
        return try {
            Log.d(TAG, "Scanning...")
            val folders = folderRepo.getAllWatchedFolders()
            for (folder in folders) { enqueueInFolder(folder) }
            Result.success()
        } catch (throwable: Throwable) {
            Log.e(TAG, "Failed", throwable)
            Result.failure()
        }
    }

    suspend fun enqueueInFolder(folder: Uri) {
        Log.d(TAG, "Scanning folder $folder")
        // Convert to DocumentFile for scanning
        val folderDf = DocumentFile.fromTreeUri(applicationContext, folder)

        if (folderDf != null) {
            // Get all files
            val files = folderDf.listFiles()

            // Check each file
            for (file in files) {
                Log.d(TAG, "Checking file...")
                // If the file is valid and hasn't been scanned yet, enqueue it
                if (isSupportedImage(file.uri) &&
                    !folderRepo.fileHasBeenScanned(file.uri, folder)) {
                        Log.d(TAG, "Adding $file to queue...")
                        queueRepo.enqueue(file.uri, folder)
                        folderRepo.addToScanned(file.uri, folder)
                }
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
}
