package com.rorycd.bowerbird.ui.editrule

import com.rorycd.bowerbird.rules.ImageCheckCondition
import com.rorycd.bowerbird.rules.RuleAction
import com.rorycd.bowerbird.rules.RuleCondition
import com.rorycd.bowerbird.rules.TagExifAction

/**
 * Represents the current state of the [EditRuleScreen] UI
 */
data class EditRuleUiState(
    val name: String = "",
    val applyConditions: Boolean = true,
    val conditions: List<RuleCondition> = listOf(ImageCheckCondition("")),
    val actions: List<RuleAction> = listOf(TagExifAction("")),
    val isEnabled: Boolean = true
)
