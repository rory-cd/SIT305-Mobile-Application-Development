package com.rorycd.bowerbird.ui.editrule

import com.rorycd.bowerbird.ui.newrule.NewRuleScreen
import com.rorycd.bowerbird.ui.newrule.NewRuleUiState
import kotlin.collections.plus
import android.net.Uri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.rorycd.bowerbird.data.RuleRepository
import com.rorycd.bowerbird.navigation.EditRuleRoute
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
 * View model for [EditRuleScreen]
 */
@HiltViewModel
class EditRuleViewModel @Inject constructor (
    private val ruleRepo: RuleRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    // Get rule id from route args
    private val ruleId: Int = savedStateHandle.toRoute<EditRuleRoute>().ruleId

    // UI state
    private val _uiState = MutableStateFlow(EditRuleUiState())
    val uiState: StateFlow<EditRuleUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {

            val rule = ruleRepo.getRuleById(ruleId)

            _uiState.update { currentState ->
                currentState.copy(
                    name = rule.name,
                    applyConditions = rule.conditions?.isNotEmpty() ?: false,
                    conditions = rule.conditions ?: listOf(ImageCheckCondition("")),
                    actions = rule.actions,
                    isEnabled = rule.isEnabled
                )
            }
        }
    }

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
            it.copy(isEnabled = !it.isEnabled)
        }
    }

    fun updateRule() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val newRule = Rule(
                    id = ruleId,
                    name = _uiState.value.name,
                    conditions = _uiState.value.conditions,
                    actions = _uiState.value.actions,
                    isEnabled = _uiState.value.isEnabled
                )
                ruleRepo.updateRule(newRule)
            }
        }
    }

    fun deleteRule() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                ruleRepo.deleteRule(ruleId)
            }
        }
    }
}
