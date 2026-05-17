package com.rorycd.bowerbird.data

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.rorycd.bowerbird.rules.Rule
import com.rorycd.bowerbird.rules.RuleAction
import com.rorycd.bowerbird.rules.RuleCondition

/**
 * Room entity, models data for a rule
 */
@Entity(
    tableName = "rules",
    indices = [Index(value = ["name"], unique = true)]
)
data class RuleEntity (
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val conditions: List<RuleCondition>?,
    val actions: List<RuleAction>
)

// Extension function - convert POCO to DB entity (for saving)
fun Rule.toEntity(id: Int = 0): RuleEntity {
    return RuleEntity(
        id = id,
        name = this.name,
        conditions = this.conditions,
        actions = this.actions
    )
}

// Extension function - convert DB entity to POCO (For reading)
fun RuleEntity.toDomain(): Rule {
    return Rule(
        name = this.name,
        conditions = this.conditions,
        actions = this.actions
    )
}
