package com.rorycd.chatbot.navigation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rorycd.chatbot.data.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * View model for Navigation
 */
@HiltViewModel
class NavViewModel @Inject constructor (
    private val userRepo: UserRepository
) : ViewModel() {

    val isLoggedIn: StateFlow<Boolean?> = userRepo.isLoggedIn()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )

    fun logOut(){
        viewModelScope.launch {
            userRepo.logOut()
        }
    }
}
