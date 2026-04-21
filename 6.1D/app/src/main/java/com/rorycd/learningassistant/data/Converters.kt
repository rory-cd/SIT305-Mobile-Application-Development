package com.rorycd.learningassistant.data

import androidx.room.TypeConverter

class Converters {
    @TypeConverter
    fun fromString(value: String?): List<String>? {
        return value?.split(',')
    }

    @TypeConverter
    fun listToString(list: List<String>?): String? {
        return list?.joinToString(separator = ",")?.ifEmpty { null }
    }
}
