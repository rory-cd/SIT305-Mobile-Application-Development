package com.rorycd.bowerbird.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction

/**
 * Data Access Object for Room [Folder] table access
 */
@Dao
interface FolderDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(folder: Folder)

    @Delete
    suspend fun delete(folder: Folder)

    @Query("SELECT * from folders WHERE uri = :uri")
    suspend fun getFolder(uri: String): Folder?

    @Transaction
    @Query("SELECT * FROM folders WHERE uri = :uri")
    suspend fun getFolderWithRules(uri: String): FolderWithRules?

    @Query("SELECT * from folders")
    suspend fun getAllFolders(): List<Folder>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertFolderRuleJoin(join: FolderRuleJoin)

    @Delete
    suspend fun deleteFolderRuleJoin(join: FolderRuleJoin)
}
