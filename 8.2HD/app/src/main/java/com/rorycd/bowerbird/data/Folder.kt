package com.rorycd.bowerbird.data

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Room entity, defines a folder to be watched
 */
@Entity(tableName = "folders")
data class Folder(
    @PrimaryKey
    val uri: String,
)
