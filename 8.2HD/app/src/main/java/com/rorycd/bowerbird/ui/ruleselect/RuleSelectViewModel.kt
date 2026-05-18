package com.rorycd.bowerbird.ui.ruleselect

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.rorycd.bowerbird.data.FolderRepository
import com.rorycd.bowerbird.data.RuleRepository
import com.rorycd.bowerbird.navigation.RuleSelectRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * View model for [RuleSelectScreen]
 */
@HiltViewModel
class RuleSelectViewModel @Inject constructor (
    private val ruleRepo: RuleRepository,
    private val folderRepo: FolderRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    // Get folder from route args
    private val uri: String = savedStateHandle.toRoute<RuleSelectRoute>().folderUri

    val rules = ruleRepo.getAllRules()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), initialValue = null)

    fun assignRuleToFolder(ruleId: Int) {
        Log.e("FOLDER", uri)
        Log.e("RULE", ruleId.toString())
        viewModelScope.launch {
            folderRepo.addFolderRuleJoin(uri, ruleId)
        }
    }
}
