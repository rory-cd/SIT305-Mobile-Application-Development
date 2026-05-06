package com.rorycd.chatbot

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rorycd.chatbot.prompt.PromptRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Viewmodel for main activity, used to load LLM
 */
@HiltViewModel
class MainViewModel @Inject constructor(
    private val promptRepository: PromptRepository
) : ViewModel() {

    init {
        viewModelScope.launch {
            promptRepository.loadModel()
        }
    }
}
