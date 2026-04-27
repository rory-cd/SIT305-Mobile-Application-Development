package com.rorycd.bowerbird.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

/**
 * Data Access Object for Room [QueuedFile] table access
 */
@Dao
interface QueuedFileDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(file: QueuedFile)

    @Update
    suspend fun update(file: QueuedFile)

    @Delete
    suspend fun delete(file: QueuedFile)

    @Query("SELECT EXISTS(SELECT 1 FROM queued_files WHERE fileUri = :fileUri)")
    suspend fun exists(fileUri: String): Boolean

    @Query("SELECT EXISTS(SELECT 1 FROM queued_files WHERE fileUri = :fileUri AND folderUri = :folderUri)")
    suspend fun existsIn(fileUri: String, folderUri: String): Boolean

    @Query("SELECT * from queued_files")
    suspend fun getAllFiles(): List<QueuedFile>

    @Query("SELECT * from queued_files WHERE folderUri = :folderUri")
    suspend fun getFilesInFolder(folderUri: String): List<QueuedFile>

    @Query("SELECT * from queued_files WHERE status = :status")
    suspend fun getFilesWithStatus(status: FileStatus): List<QueuedFile>

    @Query("SELECT * from queued_files WHERE folderUri = :folderUri AND status = :status")
    suspend fun getFilesWithStatusIn(status: FileStatus, folderUri: String): List<QueuedFile>
}
