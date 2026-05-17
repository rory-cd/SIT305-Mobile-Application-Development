package com.rorycd.bowerbird.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

/**
 * Room database for Bowerbird
 */
@Database(
    entities = [
        Folder::class,
        ScannedFile::class,
        QueuedFile::class,
        RuleEntity::class,
        FolderRuleJoin::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun folderDao(): FolderDao
    abstract fun scannedFileDao(): ScannedFileDao
    abstract fun queuedFileDao(): QueuedFileDao
    abstract fun ruleDao(): RuleDao

    companion object {
        @Volatile
        private var Instance: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            // If the Instance is not null, return it, otherwise create a new database instance and save a reference to it
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, AppDatabase::class.java, "bowerbird_database")
                    .build().also { Instance = it }
            }
        }
    }
}
