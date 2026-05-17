package com.rorycd.bowerbird.rules

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("rule")
data class Rule(
    val id: Int = 0,
    val name: String,
    val conditions: List<RuleCondition>? = null,
    val actions: List<RuleAction>,
    val isEnabled: Boolean = true
)
