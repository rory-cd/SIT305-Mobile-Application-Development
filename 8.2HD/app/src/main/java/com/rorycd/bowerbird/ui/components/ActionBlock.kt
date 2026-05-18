package com.rorycd.bowerbird.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.rorycd.bowerbird.R
import com.rorycd.bowerbird.rules.RuleAction

@Composable
fun ActionBlock(
    actions: List<RuleAction>,
    onSelectAction: (Int, ActionType) -> Unit,
    onPromptChange: (Int, String) -> Unit,
    onValueChange: (Int, String) -> Unit,
    onSelectFolder: () -> Unit,
    onAddAction: () -> Unit,
    canDeleteActions: Boolean,
    onDelete: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
    ) {
        actions.forEachIndexed { idx, action ->
            ActionInput(
                action = action,
                onSelectAction = { onSelectAction(idx, it)},
                onPromptChange = { onPromptChange(idx, it) },
                onValueChange = { onValueChange(idx, it) },
                onSelectFolder = onSelectFolder,
                canDelete = canDeleteActions && actions.size > 1,
                onDelete = { onDelete(idx) }
            )
            // AND
            if (idx < actions.size - 1) {
                Text(
                    text = stringResource(R.string.and_then),
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(vertical = 14.dp, horizontal = 3.dp)
                )
            }
        }
        // Add condition
        TextButtonAdd(
            label = stringResource(R.string.add_action_button),
            onClick = onAddAction,
            modifier = Modifier.padding(top = 18.dp)
        )
    }
}
