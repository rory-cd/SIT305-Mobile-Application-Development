package com.rorycd.bowerbird.ui.folderdetails

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.rorycd.bowerbird.data.Folder
import com.rorycd.bowerbird.data.FolderRepository
import com.rorycd.bowerbird.navigation.FolderDetailsRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * View model for [FolderDetailsScreen]
 */
@HiltViewModel
class FolderDetailsViewModel @Inject constructor (
    private val folderRepo: FolderRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    // Get folder from route args
    private val uri: String = savedStateHandle.toRoute<FolderDetailsRoute>().uri

    val folderDetails = folderRepo.getFolderWithRules(uri)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), initialValue = null)

    init {
        viewModelScope.launch {
            // Check folder exists
            val existingFolder = folderRepo.getFolder(uri)

            // If not, add one
            if (existingFolder == null) {
                val newFolder = Folder(uri)
                folderRepo.addFolder(newFolder)
            }
        }
    }

    fun removeRuleFromFolder(folderUri: String, ruleId: Int) {
        viewModelScope.launch {
            folderRepo.deleteFolderRuleJoin(folderUri, ruleId)
        }
    }
}
