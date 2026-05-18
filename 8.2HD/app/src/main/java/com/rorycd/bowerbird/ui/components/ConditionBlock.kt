package com.rorycd.bowerbird.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.rorycd.bowerbird.R
import com.rorycd.bowerbird.rules.FileSizeUnit
import com.rorycd.bowerbird.rules.Operator
import com.rorycd.bowerbird.rules.RuleCondition

@Composable
fun ConditionBlock(
    conditions: List<RuleCondition>,
    onSelectCondition: (Int, ConditionType) -> Unit,
    onPromptChange: (Int, String) -> Unit,
    onOperandChange: (Int, String) -> Unit,
    onSelectOperator: (Int, Operator) -> Unit,
    onSelectUnit: (Int, FileSizeUnit) -> Unit,
    onAddCondition: () -> Unit,
    canDeleteConditions: Boolean,
    onDelete: (Int) -> Unit
) {
    Column(

    ) {
        // IF
        Text(
            text = stringResource(R.string.if_the_image),
            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(vertical = 14.dp, horizontal = 3.dp)
        )
        conditions.forEachIndexed { idx, condition ->
            ConditionInput(
                condition = condition,
                onSelectCondition = { onSelectCondition(idx, it)},
                onPromptChange = { onPromptChange(idx, it) },
                onOperandChange = { onOperandChange(idx, it) },
                onSelectOperator = { onSelectOperator(idx, it) },
                onSelectUnit = { onSelectUnit(idx, it) },
                canDelete = canDeleteConditions && conditions.size > 1,
                onDelete = { onDelete(idx) }
            )
            // AND
            if (idx < conditions.size - 1) {
                Text(
                    text = stringResource(R.string.and),
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(vertical = 14.dp, horizontal = 3.dp)
                )
            }
        }
        // Add condition
        TextButtonAdd(
            label = stringResource(R.string.add_condition_button),
            onClick = onAddCondition,
            modifier = Modifier.padding(vertical = 18.dp)
        )
    }
}
