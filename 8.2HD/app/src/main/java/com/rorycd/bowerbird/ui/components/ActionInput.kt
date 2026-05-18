package com.rorycd.bowerbird.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.rorycd.bowerbird.R
import com.rorycd.bowerbird.rules.CopyAction
import com.rorycd.bowerbird.rules.FileSizeCondition
import com.rorycd.bowerbird.rules.FileSizeUnit
import com.rorycd.bowerbird.rules.FilenameCondition
import com.rorycd.bowerbird.rules.ImageCheckCondition
import com.rorycd.bowerbird.rules.MoveAction
import com.rorycd.bowerbird.rules.Operator
import com.rorycd.bowerbird.rules.RenameAction
import com.rorycd.bowerbird.rules.RuleAction
import com.rorycd.bowerbird.rules.RuleCondition
import com.rorycd.bowerbird.rules.TagExifAction

enum class ActionType(
    val displayName: String
) {
    TAG("Tag with"),
    RENAME("Rename to"),
    MOVE("Move to"),
    COPY("Copy to")
}

val actionTypes = ActionType.entries

@Composable
fun ActionInput(
    action: RuleAction,
    onSelectAction: (ActionType) -> Unit,
    onPromptChange: (String) -> Unit,
    onValueChange: (String) -> Unit,
    onSelectFolder: () -> Unit,
    canDelete: Boolean,
    onDelete: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Select action type
        MinimalDropdown(
            options = actionTypes,
            selectedOption = when(action) {
                is TagExifAction -> ActionType.TAG.displayName
                is RenameAction -> ActionType.RENAME.displayName
                is MoveAction -> ActionType.MOVE.displayName
                is CopyAction -> ActionType.COPY.displayName
                else -> "Action"
            },
            onSelectOption = { onSelectAction(it) },
            optionToText = { it.displayName }
        )

        if (action is TagExifAction) {
            MinimalTextInput(
                value = action.prompt,
                hint = stringResource(R.string.tag_description),
                onValueChange = onPromptChange,
                modifier = Modifier.weight(1f)
            )
        }

        if (action is RenameAction) {
            // New name
            MinimalTextInput(
                value = action.value,
                hint = stringResource(R.string.new_name),
                onValueChange = onValueChange,
                modifier = Modifier.weight(1f)
            )
        }

        if (action is MoveAction) {
            FolderSelector(
                value = action.targetFolder ?: "",
                onSelect = onSelectFolder,
                modifier = Modifier.weight(1f)
            )
        }

        if (action is CopyAction) {
            FolderSelector(
                value = action.targetFolder ?: "",
                onSelect = onSelectFolder,
                modifier = Modifier.weight(1f)
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
