package com.rorycd.lostandfound.ui.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rorycd.lostandfound.data.Advert
import com.rorycd.lostandfound.data.AdvertRepository
import com.rorycd.lostandfound.utils.formatDateAsString
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DetailsViewModel(
    savedStateHandle: SavedStateHandle,
    private val advertRepository: AdvertRepository
)  : ViewModel() {

    // UI state
    private val _uiState = MutableStateFlow(DetailsUiState())
    val uiState: StateFlow<DetailsUiState> = _uiState.asStateFlow()

    var advert: Advert? = null

    init {
        viewModelScope.launch {
            // Get item id from route args
            val itemId: Int = checkNotNull(savedStateHandle["itemId"])

            advert = advertRepository.getAdvert(itemId)

            if (advert == null) {
                _uiState.update { it.copy(toastMessage = "Something went wrong - results could not be fetched.") }
                return@launch
            }

            _uiState.update {
                it.copy(
                    id = advert!!.id,
                    postType = advert!!.postType.lowercase().replaceFirstChar { it.uppercase() },
                    name = advert!!.name,
                    phone = advert!!.phone,
                    description = advert!!.description,
                    imgUri = advert!!.imgUri,
                    dateFormatted = formatDateAsString(advert!!.date)
                )
            }
        }
    }

    fun deleteItem() {
        viewModelScope.launch {
            advert?.let { advertRepository.deleteAdvert(it) }
        }
    }

    fun clearToast() {
        _uiState.update { it.copy(toastMessage = null) }
    }
}
