package com.rorycd.bowerbird.rules

data class Rule(
    val name: String,
    val conditions: List<RuleCondition>? = null,
    val actions: List<RuleAction>
)
