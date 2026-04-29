package com.rorycd.lostandfound.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object for Room [Advert] database access
 */
@Dao
interface AdvertDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(advert: Advert)

    @Update
    suspend fun update(advert: Advert)

    @Delete
    suspend fun delete(advert: Advert)

    @Query("SELECT * from adverts WHERE id = :id")
    suspend fun getAdvert(id: Int): Advert

    @Query("SELECT * from adverts ORDER BY date ASC")
    fun getAllAdverts(): Flow<List<Advert>>
}
