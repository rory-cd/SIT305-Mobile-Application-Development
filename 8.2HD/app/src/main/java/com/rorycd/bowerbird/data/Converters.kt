package com.rorycd.bowerbird.data

import androidx.room.TypeConverter
import com.rorycd.bowerbird.rules.RuleAction
import com.rorycd.bowerbird.rules.RuleCondition
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer

/**
 * Room type converters for serialization
 */
class Converters {
    // For future-proofing
    private val json = Json { ignoreUnknownKeys = true }

    @TypeConverter
    fun fromActionList(actionList: List<RuleAction>): String {
        return json.encodeToString(actionList)
    }

    @TypeConverter
    fun toActionList(value: String): List<RuleAction> {
        return json.decodeFromString(value)
    }

    @TypeConverter
    fun fromConditionList(conditions: List<RuleCondition>?): String? {
        if (conditions == null) return null
        return json.encodeToString(conditions)
    }

    @TypeConverter
    fun toConditionList(value: String?): List<RuleCondition>? {
        if (value == null) return null
        return json.decodeFromString(value)
    }
}
