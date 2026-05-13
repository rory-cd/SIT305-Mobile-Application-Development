package com.rorycd.lostandfound.ui.components

import com.rorycd.lostandfound.R
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.rorycd.lostandfound.data.PostType
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp

/**
 * Horizontal radio group for post types
 */
@Composable
fun RadioGroupHorizontal(
    radioOptions: List<PostType>,
    selectedOption: PostType,
    onSelect: (PostType) -> Unit,
    modifier: Modifier = Modifier
) {
    // Modifier.selectableGroup() is for accessibility behaviour
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.selectableGroup()
    ) {
        Text(stringResource(R.string.post_type_label))
        radioOptions.forEach { option ->
            Row(
                Modifier
                    .height(56.dp)
                    .selectable(
                        selected = (option == selectedOption),
                        onClick = { onSelect(option) },
                        role = Role.RadioButton
                    )
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = (option == selectedOption),
                    onClick = null
                )
                Text(
                    text = option.toString().lowercase().replaceFirstChar { it.uppercase() },
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
        }
    }
}
