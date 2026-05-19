package com.rorycd.bowerbird.ui.components

import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.rorycd.bowerbird.R

@Composable
fun FolderSelector(
    value: String,
    onSelect: (Uri) -> Unit,
    modifier: Modifier = Modifier,
    maxLines: Int = 1
) {
    val context = LocalContext.current

    // Folder select launcher
    val folderSelectLauncher = rememberLauncherForActivityResult(ActivityResultContracts.OpenDocumentTree()) { uri ->
        if (uri != null) {
            // Retain permissions to access this folder after restart
            val takeFlags: Int = Intent.FLAG_GRANT_READ_URI_PERMISSION or
                    Intent.FLAG_GRANT_WRITE_URI_PERMISSION
            // Check for the freshest data.
            context.contentResolver.takePersistableUriPermission(uri, takeFlags)
            onSelect(uri)
        }
    }

    Row(
        modifier = modifier.clickable(onClick = { folderSelectLauncher.launch(null) })
    ) {
        Icon(
            painter = painterResource(R.drawable.folders),
            contentDescription = stringResource(R.string.select_folder_icon),
            tint = MaterialTheme.colorScheme.secondary,
            modifier = Modifier.padding(end = 4.dp)
        )
        Text(
            text = value.ifEmpty { stringResource(R.string.select_folder) },
            maxLines = maxLines
        )
    }
}
