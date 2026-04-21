package com.rorycd.learningassistant.data

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Room entity, models data for a user
 */
@Entity(tableName = "users")
data class User (
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val username: String,
    val password: String,
    val email: String,
    val imgUri: String?
)
