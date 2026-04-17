package com.rorycd.bowerbird.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

/**
 * Data Access Object for Room [ProcessedFile] table access
 */
@Dao
interface ProcessedFileDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(file: ProcessedFile)

    @Update
    suspend fun update(file: ProcessedFile)

    @Delete
    suspend fun delete(file: ProcessedFile)

    @Query("SELECT EXISTS(SELECT 1 FROM processed_files WHERE fileUri = :fileUri)")
    suspend fun exists(fileUri: String): Boolean

    @Query("SELECT EXISTS(SELECT 1 FROM processed_files WHERE fileUri = :fileUri AND folderUri = :folderUri)")
    suspend fun existsIn(fileUri: String, folderUri: String): Boolean

    @Query("SELECT * from processed_files")
    suspend fun getAllFiles(): List<ProcessedFile>

    @Query("SELECT * from processed_files WHERE folderUri = :folderUri")
    suspend fun getFilesInFolder(folderUri: String): List<ProcessedFile>
}
