package com.rorycd.lostandfound.data

import kotlinx.coroutines.flow.Flow

/**
 * Repository that provides insert, update, delete, and retrieve of [Advert] from a given data source
 */
interface AdvertRepository {
    /**
     * Retrieve all the adverts from the given data source
     */
    fun getAllAdvertStream(): Flow<List<Advert>>

    /**
     * Retrieve an advert from the given data source that matches with the [id]
     */
    suspend fun getAdvert(id: Int): Advert?

    /**
     * Insert advert in the data source
     */
    suspend fun insertAdvert(advert: Advert)

    /**
     * Delete advert from the data source
     */
    suspend fun deleteAdvert(advert: Advert)

    /**
     * Update advert in the data source
     */
    suspend fun updateAdvert(advert: Advert)
}
