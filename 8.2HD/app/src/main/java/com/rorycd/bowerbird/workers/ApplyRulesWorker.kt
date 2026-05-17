package com.rorycd.bowerbird.workers

import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import android.content.Context
import android.util.Log
import com.rorycd.bowerbird.data.AppDatabase
import com.rorycd.bowerbird.data.FolderRepository
import com.rorycd.bowerbird.data.QueueRepository
import com.rorycd.bowerbird.BowerbirdApplication

private const val TAG = "ApplyRulesWorker"

class ApplyRulesWorker(val context: Context, params: WorkerParameters) : CoroutineWorker(context, params) {
    override suspend fun doWork(): Result {

        val db = AppDatabase.getDatabase(applicationContext)
        val folderRepo = FolderRepository(
            db.folderDao(),
            db.scannedFileDao()
        )
        val queueRepo = QueueRepository(db.queuedFileDao())

        return try {
            Log.d(TAG, "Processing...")

            // Get folders
            val folders = folderRepo.getAllWatchedFolders()

            for (folder in folders) {
                // Get queued files for folder
                val files = queueRepo.getQueuedFilesIn(folder)
                if (files.isEmpty()) continue

                // Ensure AI is loaded
                val app = applicationContext as BowerbirdApplication
                val repo = app.container.promptRepository
                repo.loadModel()

                // Get rules for folder

                for (file in files) {
                    val response = repo.getResponse("Describe this image in two sentences, each in JSON format with keys of '1' and '2' respectively.", file)
                    queueRepo.markAsDone(file, folder)
                    Log.e(TAG, response)
                }
            }
            Result.success()

        } catch (throwable: Throwable) {
            Log.e(TAG, "Failed", throwable)
            Result.failure()
        }
    }
}
