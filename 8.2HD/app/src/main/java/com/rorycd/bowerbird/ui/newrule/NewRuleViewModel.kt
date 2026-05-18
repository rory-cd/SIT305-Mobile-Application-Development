package com.rorycd.bowerbird.ui.newrule

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rorycd.bowerbird.data.RuleRepository
import com.rorycd.bowerbird.rules.CopyAction
import com.rorycd.bowerbird.rules.FileSizeCondition
import com.rorycd.bowerbird.rules.FileSizeUnit
import com.rorycd.bowerbird.rules.FilenameCondition
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
 * View model for [NewRuleScreen]
 */
@HiltViewModel
class NewRuleViewModel @Inject constructor (
    private val ruleRepo: RuleRepository
) : ViewModel() {

    // UI state
    private val _uiState = MutableStateFlow(NewRuleUiState())
    val uiState: StateFlow<NewRuleUiState> = _uiState.asStateFlow()

    val rules = ruleRepo.getAllRules()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), initialValue = null)

    // UI management
    fun onNameChanged(newName: String) {
        _uiState.update { it.copy(
            name = newName
        ) }
    }

    // CONDITIONS
    fun onToggleConditions(value: Boolean) {
        _uiState.update { it.copy(
            applyConditions = value
        ) }
    }

    fun onSetConditionType(index: Int, updatedCondition: RuleCondition) {
        _uiState.update {
            val updatedConditionsList = it.conditions.mapIndexed { idx, condition ->
                if (idx == index) updatedCondition else condition
            }
            it.copy(conditions = updatedConditionsList)
        }
    }

    fun onConditionPromptChange(index: Int, prompt: String) {
        _uiState.update {
            val updatedConditionsList = it.conditions.mapIndexed { idx, condition ->
                if (idx == index && condition is ImageCheckCondition)
                    condition.copy(condition = prompt)
                else condition
            }
            it.copy(conditions = updatedConditionsList)
        }
    }

    fun onSetConditionOperator(index: Int, operator: Operator) {
        _uiState.update {
            val updatedConditionsList = it.conditions.mapIndexed { idx, condition ->
                if (idx == index) {
                    when (condition) {
                        is FilenameCondition -> {
                            condition.copy(operator = operator)
                        }
                        is FileSizeCondition -> {
                            condition.copy(operator = operator)
                        }
                        else -> condition
                    }
                } else condition
            }
            it.copy(conditions = updatedConditionsList)
        }
    }

    fun onConditionOperandChange(index: Int, operand: String) {
        _uiState.update {
            val updatedConditionsList = it.conditions.mapIndexed { idx, condition ->
                if (idx == index) {
                    when (condition) {
                        is FilenameCondition -> {
                            condition.copy(operand = operand)
                        }
                        is FileSizeCondition -> {
                            condition.copy(operand = operand)
                        }
                        else -> condition
                    }
                } else condition
            }
            it.copy(conditions = updatedConditionsList)
        }
    }

    fun onSetConditionUnit(index: Int, unit: FileSizeUnit) {
        _uiState.update {
            val updatedConditionsList = it.conditions.mapIndexed { idx, condition ->
                if (idx == index && condition is FileSizeCondition) {
                    condition.copy(unit = unit)
                } else condition
            }
            it.copy(conditions = updatedConditionsList)
        }
    }

    fun onDeleteCondition(index: Int) {
        _uiState.update {
            val updatedConditionsList = it.conditions.filterIndexed { idx, _ ->
                idx != index
            }
            it.copy(conditions = updatedConditionsList)
        }
    }

    fun onAddCondition() {
        _uiState.update {
            val updatedConditionsList = it.conditions + ImageCheckCondition("")
            it.copy(conditions = updatedConditionsList)
        }
    }

    // ACTIONS
    fun onSetActionType(index: Int, updatedAction: RuleAction) {
        _uiState.update {
            val updatedActionsList = it.actions.mapIndexed { idx, action ->
                if (idx == index) updatedAction else action
            }
            it.copy(actions = updatedActionsList)
        }
    }

    fun onActionPromptChange(index: Int, prompt: String) {
        _uiState.update {
            val updatedActionsList = it.actions.mapIndexed { idx, action ->
                if (idx == index && action is TagExifAction)
                    action.copy(prompt = prompt)
                else action
            }
            it.copy(actions = updatedActionsList)
        }
    }

    fun onActionValueChange(index: Int, value: String) {
        _uiState.update {
            val updatedActionsList = it.actions.mapIndexed { idx, action ->
                if (idx == index) {
                    when (action) {
                        is RenameAction -> {
                            action.copy(value = value)
                        }
                        else -> action
                    }
                } else action
            }
            it.copy(actions = updatedActionsList)
        }
    }

    fun onDeleteAction(index: Int) {
        _uiState.update {
            val updatedActionsList = it.actions.filterIndexed { idx, _ ->
                idx != index
            }
            it.copy(actions = updatedActionsList)
        }
    }

    fun onAddAction() {
        _uiState.update {
            val updatedActionsList = it.actions + TagExifAction("")
            it.copy(actions = updatedActionsList)
        }
    }

    fun onSelectFolder(index: Int, uri: Uri) {
        _uiState.update {
            val updatedActionsList = it.actions.mapIndexed { idx, action ->
                if (idx == index) {
                    when (action) {
                        is MoveAction -> {
                            action.copy(targetFolder = uri.toString())
                        }
                        is CopyAction -> {
                            action.copy(targetFolder = uri.toString())
                        }
                        else -> action
                    }
                } else action
            }
            it.copy(actions = updatedActionsList)
        }
    }

    fun onToggleEnable() {
        _uiState.update {
            it.copy(enableImmediately = !it.enableImmediately)
        }
    }

    fun addRule() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val newRule = Rule(
                    id = 0,
                    name = _uiState.value.name,
                    conditions = _uiState.value.conditions,
                    actions = _uiState.value.actions,
                    isEnabled = _uiState.value.enableImmediately
                )
                ruleRepo.addRule(newRule)
            }
        }
    }
}
