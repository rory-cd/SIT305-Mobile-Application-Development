package com.rorycd.bowerbird.ui.folders

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rorycd.bowerbird.data.Folder
import com.rorycd.bowerbird.data.FolderRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * View model for [FoldersScreen]
 */
@HiltViewModel
class FoldersViewModel @Inject constructor (
    private val folderRepo: FolderRepository
) : ViewModel() {

    val folders = folderRepo.getAllWatchedFoldersFlow()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), initialValue = null)

    fun deleteFolder(folder: Folder) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                folderRepo.deleteFolder(folder)
            }
        }
    }
}
