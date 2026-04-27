package com.rorycd.bowerbird.data

import android.net.Uri
import androidx.core.net.toUri

class QueueRepository(
    private val queuedFileDao: QueuedFileDao
) {
    suspend fun enqueue (file: Uri, originFolder: Uri) {
        queuedFileDao.insert(
            QueuedFile(
                fileUri = file.toString(),
                folderUri = originFolder.toString()
            )
        )
    }

    suspend fun markAsProcessing(file: QueuedFile) {
        queuedFileDao.update(file.copy(status = FileStatus.PROCESSING, lastModified = System.currentTimeMillis()))
    }

    suspend fun markAsDone(file: Uri, folder: Uri) {
        val updated = QueuedFile(
            fileUri = file.toString(),
            folderUri = folder.toString(),
            status = FileStatus.DONE,
            lastModified = System.currentTimeMillis()
        )
        queuedFileDao.update(updated)
    }

    suspend fun markAsFailed(file: QueuedFile) {
        queuedFileDao.update(file.copy(status = FileStatus.FAILED, lastModified = System.currentTimeMillis()))
    }

    suspend fun getQueuedFilesIn(folder: Uri): List<Uri> {
        val queuedFiles = queuedFileDao.getFilesWithStatusIn(FileStatus.QUEUED, folder.toString())
        val result = mutableListOf<Uri>()

        for (queuedFile in queuedFiles) {
            try {
                val file = queuedFile.fileUri.toUri()
                result += file
            } catch (e: Exception) {
                // Error here - not a valid URI
            }
        }
        return result
    }
}
