package com.rorycd.bowerbird.ui.components

import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import com.rorycd.bowerbird.R
import com.rorycd.bowerbird.rules.CopyAction
import com.rorycd.bowerbird.rules.MoveAction
import com.rorycd.bowerbird.rules.RenameAction
import com.rorycd.bowerbird.rules.RuleAction
import com.rorycd.bowerbird.rules.TagExifAction
import com.rorycd.bowerbird.utils.getFolderNameFromUri

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
    onSelectFolder: (Uri) -> Unit,
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
            },
            onSelectOption = { onSelectAction(it) },
            optionToText = { it.displayName }
        )

        if (action is TagExifAction) {
            Icon(
                painter = painterResource(R.drawable.ai_icon),
                contentDescription = stringResource(R.string.ai_prompt_icon),
                tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.25f),
                modifier = Modifier.padding(end = 4.dp)
            )
            MinimalTextInput(
                value = action.prompt,
                hint = stringResource(R.string.tag_description),
                onValueChange = onPromptChange,
                maxLines = 7,
                charLimit = 200,
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
                value = getFolderNameFromUri(action.targetFolder?.toUri()),
                onSelect = onSelectFolder,
                maxLines = 3,
                modifier = Modifier.weight(1f)
            )
        }

        if (action is CopyAction) {
            FolderSelector(
                value = getFolderNameFromUri(action.targetFolder?.toUri()),
                onSelect = onSelectFolder,
                maxLines = 3,
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
