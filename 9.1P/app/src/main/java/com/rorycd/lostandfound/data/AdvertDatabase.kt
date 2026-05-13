package com.rorycd.lostandfound.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

/**
 * Room database for [Advert]
 */
@Database(entities = [Advert::class], version = 1, exportSchema = false)
abstract class AdvertDatabase : RoomDatabase() {

    abstract fun advertDao(): AdvertDao

    companion object {
        @Volatile
        private var Instance: AdvertDatabase? = null

        fun getDatabase(context: Context): AdvertDatabase {
            // If the Instance is not null, return it, otherwise create a new database instance and save a reference to it
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, AdvertDatabase::class.java, "advert_database")
                    .build().also { Instance = it }
            }
        }
    }
}
