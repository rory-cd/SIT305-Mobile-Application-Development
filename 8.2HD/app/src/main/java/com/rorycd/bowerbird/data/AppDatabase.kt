package com.rorycd.bowerbird.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

/**
 * Room database for [Folder]
 */
@Database(
    entities = [
        Folder::class,
        ProcessedFile::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun folderDao(): FolderDao
    abstract fun processedFileDao(): ProcessedFileDao

    companion object {
        @Volatile
        private var Instance: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            // If the Instance is not null, return it, otherwise create a new database instance and save a reference to it
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, AppDatabase::class.java, "folder_database")
                    .build().also { Instance = it }
            }
        }
    }
}
