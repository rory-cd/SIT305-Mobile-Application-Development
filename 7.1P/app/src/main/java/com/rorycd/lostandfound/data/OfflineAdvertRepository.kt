package com.rorycd.lostandfound.data

import kotlinx.coroutines.flow.Flow

enum class PostType {
    LOST, FOUND
}

class OfflineAdvertRepository(private val advertDao: AdvertDao) : AdvertRepository {
    override fun getAllAdvertStream(): Flow<List<Advert>> = advertDao.getAllAdverts()

    override fun getAdvert(id: Int): Advert? {
        TODO("Not yet implemented")
    }

    override suspend fun insertAdvert(advert: Advert) = advertDao.insert(advert)

    override suspend fun deleteAdvert(advert: Advert) {
        TODO("Not yet implemented")
    }

    override suspend fun updateAdvert(advert: Advert) {
        TODO("Not yet implemented")
    }
}