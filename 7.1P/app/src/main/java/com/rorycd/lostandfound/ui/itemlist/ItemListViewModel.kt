package com.rorycd.lostandfound.ui.itemlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rorycd.lostandfound.data.Advert
import com.rorycd.lostandfound.data.AdvertRepository
import com.rorycd.lostandfound.ui.create.CreateAdvertUiState
import com.rorycd.lostandfound.utils.formatDateAsString
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

/**
 * View model for [ItemListScreen]
 */
class ItemListViewModel(advertRepository: AdvertRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(ItemListUiState())
    val uiState: StateFlow<ItemListUiState> = _uiState.asStateFlow()

    val items: StateFlow<List<ItemUiModel>> = advertRepository.getAllAdvertStream()
        // For each advert, map it to a ui model
        .map { adverts -> adverts.map { it.toUiModel() } }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun onQueryChanged(newQuery: String) {
        _uiState.update { it.copy(query = newQuery) }
    }
}

/**
 * Extension function for Advert, used to create an [ItemUiModel] from an [Advert]
 */
private fun Advert.toUiModel() = ItemUiModel(
    id = id,
    postType = postType.lowercase().replaceFirstChar { it.uppercase() },
    name = name,
    phone = phone,
    description = description,
    location = location,
    dateFormatted = formatDateAsString(date)
)
