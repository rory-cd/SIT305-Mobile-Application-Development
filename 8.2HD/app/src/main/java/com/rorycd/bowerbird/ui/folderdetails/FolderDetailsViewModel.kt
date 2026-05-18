package com.rorycd.bowerbird.ui.folderdetails

import kotlin.collections.plus
import android.net.Uri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.rorycd.bowerbird.data.Folder
import com.rorycd.bowerbird.data.RuleRepository
import com.rorycd.bowerbird.data.FolderRepository
import com.rorycd.bowerbird.navigation.EditRuleRoute
import com.rorycd.bowerbird.navigation.FolderDetailsRoute
import com.rorycd.bowerbird.rules.CopyAction
import com.rorycd.bowerbird.rules.FileSizeCondition
import com.rorycd.bowerbird.rules.FileSizeUnit
import com.rorycd.bowerbird.rules.FilenameCondition
import com.rorycd.bowerbird.rules.FolderWithRulesDomain
import com.rorycd.bowerbird.rules.ImageCheckCondition
import com.rorycd.bowerbird.rules.MoveAction
import com.rorycd.bowerbird.rules.Operator
import com.rorycd.bowerbird.rules.RenameAction
import com.rorycd.bowerbird.rules.Rule
import com.rorycd.bowerbird.rules.RuleAction
import com.rorycd.bowerbird.rules.RuleCondition
import com.rorycd.bowerbird.rules.TagExifAction
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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
