package com.rorycd.lostandfound.ui.create

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rorycd.lostandfound.data.Advert
import com.rorycd.lostandfound.data.AdvertRepository
import com.rorycd.lostandfound.data.PostType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.ZoneId
import java.time.ZoneOffset

/**
 * View model for [CreateAdvertScreen]. Manages UI and adds new advert to database
 */
class CreateAdvertViewModel(private val advertRepository: AdvertRepository) : ViewModel() {
    private val _uiState = MutableStateFlow(CreateAdvertUiState())
    val uiState: StateFlow<CreateAdvertUiState> = _uiState.asStateFlow()

    // UI Logic
    fun onPostTypeChanged(newPostType: PostType) {
        _uiState.update { it.copy(postType = newPostType) }
    }

    fun onNameChanged(newName: String) {
        _uiState.update {
            val updated = it.copy(name = newName)
            updated.copy(isValid = isValid(updated))
        }
    }

    fun onPhoneChanged(newPhone: String) {
        _uiState.update {
            val updated = it.copy(phone = newPhone)
            updated.copy(isValid = isValid(updated))
        }
    }

    fun onDescriptionChanged(newDescription: String) {
        _uiState.update {
            val updated = it.copy(description = newDescription)
            updated.copy(isValid = isValid(updated))
        }
    }
    fun onDateChanged(millis: Long?) {
        if (millis == null) return
        _uiState.update {
            val localMillis = Instant.ofEpochMilli(millis)
                .atZone(ZoneOffset.UTC)
                .toLocalDate()
                .atStartOfDay(ZoneId.systemDefault())
                .toInstant()
                .toEpochMilli()
            val updated = it.copy(date = localMillis)
            updated.copy(isValid = isValid(updated))
        }
    }

    fun onLocationChanged(newLocation: String) {
        _uiState.update {
            val updated = it.copy(location = newLocation)
            updated.copy(isValid = isValid(updated))
        }
    }

    fun onImageSelected(newUri: String) {
        _uiState.update {
            val updated = it.copy(imgUri = newUri)
            updated.copy(isValid = isValid(updated))
        }
    }


    // Database interaction
    fun createAdvert() {
        viewModelScope.launch {
            val state = uiState.value

            val newAdvert = Advert(
                postType = state.postType.toString(),
                name = state.name,
                phone = state.phone,
                description = state.description,
                date = state.date,
                location = state.location,
                imgUri = state.imgUri
            )
            advertRepository.insertAdvert(newAdvert)
        }
    }

    // Helpers
    fun isValid(state: CreateAdvertUiState): Boolean {
        return with(state) {
            name.isNotBlank() &&
            phone.isNotBlank() &&
            description.isNotBlank() &&
            location.isNotBlank() &&
            imgUri.isNotBlank()
        }
    }
}
