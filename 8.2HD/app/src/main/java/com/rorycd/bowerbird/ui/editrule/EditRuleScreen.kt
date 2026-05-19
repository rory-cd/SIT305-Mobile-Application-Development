package com.rorycd.bowerbird.ui.editrule

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.rorycd.bowerbird.R
import com.rorycd.bowerbird.rules.CopyAction
import com.rorycd.bowerbird.rules.FileSizeCondition
import com.rorycd.bowerbird.rules.FileSizeUnit
import com.rorycd.bowerbird.rules.FilenameCondition
import com.rorycd.bowerbird.rules.ImageCheckCondition
import com.rorycd.bowerbird.rules.MoveAction
import com.rorycd.bowerbird.rules.Operator
import com.rorycd.bowerbird.rules.RenameAction
import com.rorycd.bowerbird.rules.TagExifAction
import com.rorycd.bowerbird.ui.components.ActionBlock
import com.rorycd.bowerbird.ui.components.ActionType
import com.rorycd.bowerbird.ui.components.CheckboxWithLabel
import com.rorycd.bowerbird.ui.components.ConditionBlock
import com.rorycd.bowerbird.ui.components.ConditionType
import com.rorycd.bowerbird.ui.components.MinimalTextInput
import com.rorycd.bowerbird.ui.newrule.ValidationError

/**
 * Screen for editing a rule
 */
@Composable
fun EditRuleScreen(
    onSaveRule: () -> Unit,
    onDeleteRule: () -> Unit,
    viewModel: EditRuleViewModel = hiltViewModel()
) {
    // Collect state flow
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val scrollState = rememberScrollState()

    val context = LocalContext.current
    val validationFailedMessage = stringResource(R.string.validationFailed)

    Column(
        modifier = Modifier.padding(16.dp)
            .fillMaxSize()
            .verticalScroll(scrollState)
    ) {
        // Rule name
        MinimalTextInput(
            value = state.name,
            onValueChange = { viewModel.onNameChanged(it) },
            hint = stringResource(R.string.new_rule_name_hint),
            largeText = true,
            modifier = Modifier
                .padding(bottom = 10.dp, start = 4.dp)
                .fillMaxWidth()
        )
        CheckboxWithLabel(
            checked = state.applyConditions,
            onCheckedChange = { viewModel.onToggleConditions(it) },
            label = stringResource(R.string.apply_conditions_label)
        )

        // Condition set
        if (state.applyConditions) {

            HorizontalDivider(
                modifier = Modifier.padding(bottom = 24.dp, top = 18.dp)
            )

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
                    viewModel.onConditionPromptChange(idx, prompt)
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

        HorizontalDivider(
            modifier = Modifier.padding(bottom = 24.dp, top = if (state.applyConditions) 24.dp else 18.dp)
        )

        if (state.applyConditions) {
            // THEN
            Text(
                text = stringResource(R.string.then),
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(bottom = 14.dp, start = 3.dp)
            )
        }

        // Action set
        ActionBlock(
            actions = state.actions,
            onSelectAction = { idx, actionType ->
                val newAction = when(actionType) {
                    ActionType.TAG -> TagExifAction("")
                    ActionType.RENAME -> RenameAction("")
                    ActionType.MOVE -> MoveAction("")
                    ActionType.COPY -> CopyAction("")
                }
                viewModel.onSetActionType(idx, newAction)
            },
            onPromptChange = { idx, prompt ->
                viewModel.onActionPromptChange(idx, prompt)
            },
            onValueChange = { idx, value ->
                viewModel.onActionValueChange(idx, value)
            },
            onSelectFolder = { idx, uri ->
                viewModel.onSelectFolder(idx, uri)
            },
            onAddAction = { viewModel.onAddAction() },
            canDeleteActions = true,
            onDelete = { viewModel.onDeleteAction(it) }
        )

        HorizontalDivider(
            modifier = Modifier.padding(vertical = 24.dp)
        )

        CheckboxWithLabel(
            checked = state.isEnabled,
            onCheckedChange = { viewModel.onToggleEnable() },
            label = stringResource(R.string.enable)
        )

        Row(
            modifier = Modifier.padding(top = 14.dp)
        ) {
            // Save rule
            Button(
                onClick = {
                    if (viewModel.validateRuleInput()) {
                        viewModel.updateRule()
                        onSaveRule()
                    } else {
                        Toast.makeText(context, validationFailedMessage, Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier.padding(end = 14.dp)
            ) {
                Text(stringResource(R.string.save_rule))
            }
            // Delete rule
            OutlinedButton(
                onClick = {
                    viewModel.deleteRule()
                    onDeleteRule()
                },
                border = ButtonDefaults.outlinedButtonBorder(
                    enabled = true
                ).copy(brush = SolidColor(MaterialTheme.colorScheme.primary))
            ) {
                Text(stringResource(R.string.delete_rule))
            }
        }

        // Error message
        if (state.error != null) {

            val errorMessage = when(state.error) {
                ValidationError.ActionFolderNull -> stringResource(R.string.error_action_folder_null)
                ValidationError.ActionInputBlank -> stringResource(R.string.error_action_input_empty)
                ValidationError.ConditionInputBlank -> stringResource(R.string.error_condition_input_empty)
                ValidationError.NameBlank -> stringResource(R.string.error_name_empty)
                null -> ""
            }

            Text(
                text = "Error: $errorMessage",
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier.padding(vertical = 24.dp)
                    .fillMaxWidth()
            )
        }
    }
}
