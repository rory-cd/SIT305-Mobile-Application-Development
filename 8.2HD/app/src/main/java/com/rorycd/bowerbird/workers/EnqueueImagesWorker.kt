package com.rorycd.bowerbird.workers

import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.documentfile.provider.DocumentFile
import androidx.hilt.work.HiltWorker
import com.rorycd.bowerbird.data.AppDatabase
import com.rorycd.bowerbird.data.FolderRepository
import com.rorycd.bowerbird.data.QueueRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

private const val TAG = "EnqueueImagesWorker"

@HiltWorker
class EnqueueImagesWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val folderRepo: FolderRepository,
    private val queueRepo: QueueRepository,
) : CoroutineWorker(context, params) {

    private val db = AppDatabase.getDatabase(applicationContext)

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
