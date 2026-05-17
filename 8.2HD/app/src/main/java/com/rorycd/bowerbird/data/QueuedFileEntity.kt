package com.rorycd.bowerbird.data

import androidx.room.Entity

enum class FileStatus {
    QUEUED, PROCESSING, DONE, FAILED
}

/**
 * Room entity, defines a file queued for processing
 */
@Entity(
    tableName = "queued_files",
    primaryKeys = ["folderUri", "fileUri"]
)
data class QueuedFile(
    val folderUri: String,
    val fileUri: String,
    val status: FileStatus = FileStatus.QUEUED,
    val lastModified: Long = System.currentTimeMillis()
)
