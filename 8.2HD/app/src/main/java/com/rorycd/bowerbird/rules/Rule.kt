package com.rorycd.bowerbird.rules

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("rule")
data class Rule(
    val name: String,
    val conditions: List<RuleCondition>? = null,
    val actions: List<RuleAction>
)
