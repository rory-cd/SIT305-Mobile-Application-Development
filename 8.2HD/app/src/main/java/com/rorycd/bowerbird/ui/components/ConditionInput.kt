package com.rorycd.bowerbird.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.rorycd.bowerbird.R
import com.rorycd.bowerbird.rules.FileSizeCondition
import com.rorycd.bowerbird.rules.FileSizeUnit
import com.rorycd.bowerbird.rules.FilenameCondition
import com.rorycd.bowerbird.rules.ImageCheckCondition
import com.rorycd.bowerbird.rules.Operator
import com.rorycd.bowerbird.rules.RuleCondition

enum class ConditionType(
    val displayName: String
) {
    FILENAME("Filename"),
    FILE_SIZE("File size"),
    IMAGE_CHECK("Image")
}

val conditionTypes = ConditionType.entries

@Composable
fun ConditionInput(
    condition: RuleCondition,
    onSelectCondition: (ConditionType) -> Unit,
    onPromptChange: (String) -> Unit,
    onOperandChange: (String) -> Unit,
    onSelectOperator: (Operator) -> Unit,
    onSelectUnit: (FileSizeUnit) -> Unit,
    canDelete: Boolean,
    onDelete: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Select condition type
        MinimalDropdown(
            options = conditionTypes,
            selectedOption = when(condition) {
                is FilenameCondition -> ConditionType.FILENAME.displayName
                is FileSizeCondition -> ConditionType.FILE_SIZE.displayName
                is ImageCheckCondition -> ConditionType.IMAGE_CHECK.displayName
                else -> "Condition"
            },
            onSelectOption = { onSelectCondition(it) },
            optionToText = { it.displayName }
        )

        if (condition is ImageCheckCondition) {
            MinimalTextInput(
                value = condition.condition,
                hint = stringResource(R.string.content_description),
                onValueChange = onPromptChange,
                modifier = Modifier.weight(1f)
            )
        }

        if (condition is FilenameCondition) {
            // Operator
            MinimalDropdown(
                options = condition.validOperators,
                selectedOption = condition.operator.displayName,
                onSelectOption = { onSelectOperator(it) },
                optionToText = { it.displayName }
            )
            // Operand
            MinimalTextInput(
                value = condition.operand,
                hint = stringResource(R.string.text),
                onValueChange = onOperandChange,
                modifier = Modifier.weight(1f)
            )
        }

        if (condition is FileSizeCondition) {
            // Operator
            MinimalDropdown(
                options = condition.validOperators,
                selectedOption = condition.operator.displayName,
                onSelectOption = { onSelectOperator(it) },
                optionToText = { it.displayName }
            )
            // Operand
            MinimalTextInput(
                value = condition.operand,
                numbersOnly = true,
                hint = stringResource(R.string.default_filesize_input),
                onValueChange = onOperandChange,
                modifier = Modifier.weight(1f)
            )
            // File size unit
            MinimalDropdown(
                options = FileSizeUnit.entries,
                selectedOption = condition.unit.displayName,
                onSelectOption = { onSelectUnit(it) },
                optionToText = { it.displayName }
            )
        }

        if (canDelete) {
            Icon(
                imageVector = Icons.Outlined.Delete,
                contentDescription = stringResource(R.string.delete_button),
                tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
                modifier = Modifier
                    .clickable( onClick = onDelete )
            )
        }
    }
}
