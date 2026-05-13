package com.rorycd.lostandfound.data

import kotlinx.coroutines.flow.Flow

enum class PostType {
    LOST, FOUND
}

class OfflineAdvertRepository(private val advertDao: AdvertDao) : AdvertRepository {
    override fun getAllAdvertStream(): Flow<List<Advert>> = advertDao.getAllAdverts()

    override suspend fun getAdvert(id: Int): Advert = advertDao.getAdvert(id)

    override suspend fun insertAdvert(advert: Advert) = advertDao.insert(advert)

    override suspend fun deleteAdvert(advert: Advert) = advertDao.delete(advert)

    override suspend fun updateAdvert(advert: Advert) = advertDao.update(advert)
}