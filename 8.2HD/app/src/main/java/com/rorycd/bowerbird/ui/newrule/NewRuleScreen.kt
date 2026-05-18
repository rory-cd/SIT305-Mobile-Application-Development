package com.rorycd.bowerbird.ui.newrule

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.rorycd.bowerbird.R
import com.rorycd.bowerbird.rules.FileSizeCondition
import com.rorycd.bowerbird.rules.FileSizeUnit
import com.rorycd.bowerbird.rules.FilenameCondition
import com.rorycd.bowerbird.rules.ImageCheckCondition
import com.rorycd.bowerbird.rules.Operator
import com.rorycd.bowerbird.ui.components.CheckboxWithLabel
import com.rorycd.bowerbird.ui.components.ConditionBlock
import com.rorycd.bowerbird.ui.components.ConditionType
import com.rorycd.bowerbird.ui.components.MinimalTextInput

/**
 * Screen for creating a new rule
 */
@Composable
fun NewRuleScreen(
    viewModel: NewRuleViewModel = hiltViewModel()
) {
    // Collect state flow
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier.padding(16.dp)
    ) {
        MinimalTextInput(
            value = state.name,
            onValueChange = { viewModel.onNameChanged(it) },
            hint = stringResource(R.string.new_rule_name_hint),
            largeText = true,
            modifier = Modifier
                .padding(bottom = 8.dp)
                .fillMaxWidth()
        )
        CheckboxWithLabel(
            checked = state.applyConditions,
            onCheckedChange = { viewModel.onToggleConditions(it) },
            label = stringResource(R.string.apply_conditions_label)
        )
        HorizontalDivider(
            modifier = Modifier.padding(bottom = 10.dp, top = 4.dp)
        )

        // Condition set
        if (state.applyConditions) {
            ConditionBlock(
                conditions = state.conditions,
                onSelectCondition = { idx, conditionType ->
                    val newCondition = when(conditionType) {
                        ConditionType.FILENAME -> FilenameCondition(Operator.CONTAINS, "")
                        ConditionType.FILE_SIZE -> FileSizeCondition(Operator.GREATER_THAN, "2", FileSizeUnit.MEGABYTES)
                        ConditionType.IMAGE_CHECK -> ImageCheckCondition("")
                    }
                    viewModel.onSetConditionType(idx, newCondition)
                },
                onPromptChange = { idx, prompt ->
                    if (state.conditions[idx] is ImageCheckCondition) {
                        viewModel.onConditionPromptChange(idx, prompt)
                    }
                 },
                onSelectOperator = { idx, operator ->
                    viewModel.onSetConditionOperator(idx, operator)
                },
                onOperandChange = { idx, operand ->
                    viewModel.onConditionOperandChange(idx, operand)
                },
                onSelectUnit = { idx, unit ->
                    viewModel.onSetConditionUnit(idx, unit)
                },
                onAddCondition = { viewModel.onAddCondition() },
                canDeleteConditions = true,
                onDelete = { viewModel.onDeleteCondition(it) }
            )
        }
    }
}
