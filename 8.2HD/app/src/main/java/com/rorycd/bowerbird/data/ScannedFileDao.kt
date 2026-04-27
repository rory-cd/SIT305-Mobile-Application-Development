package com.rorycd.bowerbird.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

/**
 * Data Access Object for Room [ScannedFile] table access
 */
@Dao
interface ScannedFileDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(file: ScannedFile)

    @Update
    suspend fun update(file: ScannedFile)

    @Delete
    suspend fun delete(file: ScannedFile)

    @Query("SELECT EXISTS(SELECT 1 FROM scanned_files WHERE fileUri = :fileUri)")
    suspend fun exists(fileUri: String): Boolean

    @Query("SELECT EXISTS(SELECT 1 FROM scanned_files WHERE fileUri = :fileUri AND folderUri = :folderUri)")
    suspend fun existsIn(fileUri: String, folderUri: String): Boolean

    @Query("SELECT * from scanned_files")
    suspend fun getAllFiles(): List<ScannedFile>

    @Query("SELECT * from scanned_files WHERE folderUri = :folderUri")
    suspend fun getFilesInFolder(folderUri: String): List<ScannedFile>
}
