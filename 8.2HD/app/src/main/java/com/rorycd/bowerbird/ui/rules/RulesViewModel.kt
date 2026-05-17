package com.rorycd.bowerbird.ui.rules

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rorycd.bowerbird.data.RuleRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

/**
 * View model for [RulesScreen]
 */
@HiltViewModel
class RulesViewModel @Inject constructor (
    private val ruleRepo: RuleRepository
) : ViewModel() {

    val rules = ruleRepo.getAllRules()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), initialValue = null)
}
