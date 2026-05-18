package com.rorycd.bowerbird.ui.newrule

import com.rorycd.bowerbird.rules.ImageCheckCondition
import com.rorycd.bowerbird.rules.RuleAction
import com.rorycd.bowerbird.rules.RuleCondition
import com.rorycd.bowerbird.rules.TagExifAction

/**
 * Represents the current state of the [NewRuleScreen] UI
 */
data class NewRuleUiState(
    val name: String = "",
    val applyConditions: Boolean = true,
    val conditions: List<RuleCondition> = listOf(ImageCheckCondition("")),
    val actions: List<RuleAction> = listOf(TagExifAction("")),
    val enableImmediately: Boolean = true
)
