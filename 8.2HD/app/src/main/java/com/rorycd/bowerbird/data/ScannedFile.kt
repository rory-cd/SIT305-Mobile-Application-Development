package com.rorycd.bowerbird.data

import androidx.room.Entity

/**
 * Room entity, defines a file which has been scanned
 */
@Entity(
    tableName = "scanned_files",
    primaryKeys = ["folderUri", "fileUri"]
)
data class ScannedFile(
    val folderUri: String,
    val fileUri: String,
    val lastModified: Long = System.currentTimeMillis()
)
