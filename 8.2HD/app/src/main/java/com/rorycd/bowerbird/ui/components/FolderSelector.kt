package com.rorycd.bowerbird.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.rorycd.bowerbird.R

@Composable
fun FolderSelector(
    value: String,
    onSelect: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.clickable(onClick = onSelect)
    ) {
        Icon(
            painter = painterResource(R.drawable.folders),
            contentDescription = stringResource(R.string.select_folder_icon),
            tint = MaterialTheme.colorScheme.secondary,
            modifier = Modifier.padding(end = 4.dp)
        )
        Text(
            text = value.ifEmpty { stringResource(R.string.select_folder) }
        )
    }
}
