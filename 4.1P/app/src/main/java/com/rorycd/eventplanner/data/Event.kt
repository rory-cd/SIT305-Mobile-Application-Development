package com.rorycd.eventplanner.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "events")
data class Event (
    @PrimaryKey(autoGenerate = true)
    val id: Int = 1,
    val title: String,
    val location: String,
    val category: String,
    val date: String,
    val time: String
)
