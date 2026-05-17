package com.rorycd.bowerbird.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.rorycd.bowerbird.rules.Rule

/**
 * Room entity, defines a folder to be watched
 */
@Entity(tableName = "folders")
data class Folder(
    @PrimaryKey
    val uri: String
)
