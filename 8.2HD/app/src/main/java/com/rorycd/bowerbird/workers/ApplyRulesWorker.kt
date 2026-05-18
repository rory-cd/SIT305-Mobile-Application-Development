package com.rorycd.bowerbird.workers

import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import com.rorycd.bowerbird.data.FolderRepository
import com.rorycd.bowerbird.data.QueueRepository
import com.rorycd.bowerbird.prompt.PromptRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

private const val TAG = "ApplyRulesWorker"

@HiltWorker
class ApplyRulesWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val folderRepo: FolderRepository,
    private val queueRepo: QueueRepository,
    private val promptRepo: PromptRepository
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        return try {
            Log.d(TAG, "Processing...")

            // Get folders
            val folders = folderRepo.getAllWatchedFolders()

            for (folder in folders) {
                // Get queued files for folder
                val files = queueRepo.getQueuedFilesIn(folder)
                if (files.isEmpty()) continue

                // Ensure AI is loaded
                promptRepo.loadModel()

                // Get rules for folder
                for (file in files) {
                    val response = promptRepo.getResponse("Describe this image in two sentences, each in JSON format with keys of '1' and '2' respectively.", file)
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
