package com.rorycd.learningassistant.data

import androidx.room.TypeConverter
import java.util.Date

class Converters {
    @TypeConverter
    fun fromString(value: String): List<String>? {
        return if (value.isEmpty()) emptyList() else value.split("|||")
    }

    @TypeConverter
    fun listToString(list: List<String>): String {
        return list.joinToString(separator = "|||")
    }

    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }
}
