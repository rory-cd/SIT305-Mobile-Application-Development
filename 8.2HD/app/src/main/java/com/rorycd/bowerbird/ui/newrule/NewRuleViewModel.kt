package com.rorycd.bowerbird.ui.newrule

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rorycd.bowerbird.data.RuleRepository
import com.rorycd.bowerbird.rules.FileSizeCondition
import com.rorycd.bowerbird.rules.FileSizeUnit
import com.rorycd.bowerbird.rules.FilenameCondition
import com.rorycd.bowerbird.rules.ImageCheckCondition
import com.rorycd.bowerbird.rules.Operator
import com.rorycd.bowerbird.rules.RuleCondition
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

    fun onToggleConditions(value: Boolean) {
        _uiState.update { it.copy(
            applyConditions = value
        ) }
    }

    fun onSetConditionType(index: Int, updatedCondition: RuleCondition) {
        _uiState.update {
            // Create new list with replaced item
            val updatedConditionsList = it.conditions.mapIndexed { idx, condition ->
                if (idx == index) updatedCondition else condition
            }
            // Set list in state
            it.copy(conditions = updatedConditionsList)
        }
    }

    fun onConditionPromptChange(index: Int, prompt: String) {
        _uiState.update {
            // Create new list with replaced item
            val updatedConditionsList = it.conditions.mapIndexed { idx, condition ->
                if (idx == index && condition is ImageCheckCondition)
                    condition.copy(condition = prompt)
                else condition
            }
            // Set list in state
            it.copy(conditions = updatedConditionsList)
        }
    }

    fun onSetConditionOperator(index: Int, operator: Operator) {
        _uiState.update {
            // Create new list with replaced item
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
            // Set list in state
            it.copy(conditions = updatedConditionsList)
        }
    }

    fun onConditionOperandChange(index: Int, operand: String) {
        _uiState.update {
            // Create new list with replaced item
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
            // Set list in state
            it.copy(conditions = updatedConditionsList)
        }
    }

    fun onSetConditionUnit(index: Int, unit: FileSizeUnit) {
        _uiState.update {
            // Create new list with replaced item
            val updatedConditionsList = it.conditions.mapIndexed { idx, condition ->
                if (idx == index && condition is FileSizeCondition) {
                    condition.copy(unit = unit)
                } else condition
            }
            // Set list in state
            it.copy(conditions = updatedConditionsList)
        }
    }

    fun onDeleteCondition(index: Int) {
        _uiState.update {
            // Create new list with condition filtered out
            val updatedConditionsList = it.conditions.filterIndexed { idx, _ ->
                idx != index
            }
            // Set list in state
            it.copy(conditions = updatedConditionsList)
        }
    }

    fun onAddCondition() {
        _uiState.update {
            // Create new list with the new condition
            val updatedConditionsList = it.conditions + ImageCheckCondition("")
            // Set list in state
            it.copy(conditions = updatedConditionsList)
        }
    }

    fun addRule() {

        viewModelScope.launch {
            withContext(Dispatchers.IO) {

            }
        }
    }
}
