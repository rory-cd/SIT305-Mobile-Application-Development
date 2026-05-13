package com.rorycd.lostandfound.data

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Room entity, models data for an advert
 */
@Entity(tableName = "adverts")
data class Advert (
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val postType: String,
    val name: String,
    val phone: String,
    val description: String,
    val date: Long,
    val latitude: Double,
    val longitude: Double,
    val locationName: String,
    val imgUri: String
)
