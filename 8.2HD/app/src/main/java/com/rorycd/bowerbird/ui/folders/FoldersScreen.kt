package com.rorycd.bowerbird.ui.folders

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.rorycd.bowerbird.R
import com.rorycd.bowerbird.ui.components.FolderCard

/**
 * Screen showing the full list of watched folders
 */
@Composable
fun FoldersScreen(
    onSelectFolder: (String) -> Unit,
    onDeleteFolder: () -> Unit,
    viewModel: FoldersViewModel = hiltViewModel()
) {
    val folders by viewModel.folders.collectAsStateWithLifecycle()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        if (folders.isNullOrEmpty()) item {
            Text(stringResource(R.string.no_folders_message))
        }

        items(folders ?: emptyList()) { folder ->
            FolderCard(
                folder = folder,
                onSelect = { onSelectFolder(folder.uri) },
                onDelete = {
                    viewModel.deleteFolder(folder)
                    onDeleteFolder()
                }
            )
        }
    }
}
