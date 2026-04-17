package com.rorycd.bowerbird.data

import androidx.room.Entity

/**
 * Room entity, defines a folder to be watched
 */
@Entity(
    tableName = "processed_files",
    primaryKeys = ["folderUri, fileUri"]
)
data class ProcessedFile(
    val folderUri: String,
    val fileUri: String,
    val lastModified: Long
)
