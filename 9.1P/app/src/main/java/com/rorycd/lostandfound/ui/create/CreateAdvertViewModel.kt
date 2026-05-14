package com.rorycd.lostandfound.ui.create

import android.Manifest
import android.app.Application
import android.content.pm.PackageManager
import android.location.Geocoder
import android.os.Build
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.location.CurrentLocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.model.LatLng
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
import com.google.android.libraries.places.api.model.AutocompletePrediction
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.api.net.PlacesClient
import kotlinx.coroutines.Dispatchers
import java.time.ZoneOffset
import java.util.Locale

/**
 * View model for [CreateAdvertScreen]. Manages UI and adds new advert to database
 */
class CreateAdvertViewModel(
    application: Application,
    private val advertRepository: AdvertRepository,
    private val placesClient: PlacesClient
) : AndroidViewModel(application) {
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

    fun onLocationChanged(input: String) {
        // Update input state and wipe predictions
        _uiState.update { it.copy(
            locationInput = input,
            selectedLocation = null,
            selectedLocationName = "",
            locationPredictions = emptyList()
        )}

        if (input.length > 3) {
            // Set loading to true
            _uiState.update { it.copy(isLoadingPredictions = true) }

            // Build request
            val request = FindAutocompletePredictionsRequest.builder()
                .setQuery(input)
                .setRegionCode("AU")
                .build()

            // Get predictions
            placesClient.findAutocompletePredictions(request)
                .addOnSuccessListener { response ->
                    _uiState.update {
                        val updated = it.copy(
                            locationPredictions = response.autocompletePredictions,
                            isLoadingPredictions = false
                        )
                        updated.copy(isValid = isValid(updated))
                    }
                }
                .addOnFailureListener { exception ->
                    Log.e("Places", "Error: ${exception.message}")
                    _uiState.update { it.copy(isLoadingPredictions = false) }
                }
        } else {
            _uiState.update {
                val updated = it.copy(locationPredictions = emptyList())
                updated.copy(isValid = isValid(updated))
            }
        }
    }

    fun onSelectLocation(location: AutocompletePrediction) {
        val placeRequest = FetchPlaceRequest.newInstance(
            location.placeId,
            listOf(Place.Field.LOCATION, Place.Field.DISPLAY_NAME)
        )
        placesClient.fetchPlace(placeRequest)
            .addOnSuccessListener { response ->
                val place = response.place
                val location = place.location

                if (location != null) {
                    _uiState.update {
                        val updated = it.copy(
                            selectedLocation = LatLng(location.latitude, location.longitude),
                            selectedLocationName = place.displayName ?: "",
                            locationInput = place.displayName ?: "",
                            locationPredictions = emptyList()
                        )
                        updated.copy(isValid = isValid(updated))
                    }
                }
            }
    }

    fun onUseCurrentLocation() {
        val context = getApplication<Application>().applicationContext
        val fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)

        // Ensure permissions are granted
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED) return

        val locationRequest = CurrentLocationRequest.Builder()
            .setPriority(Priority.PRIORITY_HIGH_ACCURACY)
            .build()

        _uiState.update { state -> state.copy(isLoadingCurrentLocation = true) }

        fusedLocationProviderClient.getCurrentLocation(locationRequest, null).addOnSuccessListener {location ->
            location?.let {
                val latLng = LatLng(it.latitude, it.longitude)
                val geocoder = Geocoder(context, Locale.getDefault())

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    // Asynchronous version
                    geocoder.getFromLocation(it.latitude, it.longitude, 1) { addresses ->
                        val name = addresses.firstOrNull()?.getAddressLine(0) ?: ""
                        _uiState.update { state -> state.copy(
                            selectedLocation = latLng,
                            selectedLocationName = name,
                            locationInput = name,
                            isLoadingCurrentLocation = false
                        ) }
                    }
                } else {
                    // Synchronous version
                    viewModelScope.launch(Dispatchers.IO) {
                        @Suppress("DEPRECATION")
                        val addresses = geocoder.getFromLocation(it.latitude, it.longitude, 1)
                        val name = addresses?.firstOrNull()?.getAddressLine(0) ?: ""
                        _uiState.update { state ->
                            state.copy(
                                selectedLocation = latLng,
                                selectedLocationName = name,
                                locationInput = name,
                                isLoadingCurrentLocation = false
                            )
                        }
                    }
                }
            }
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

            if (state.selectedLocation == null) return@launch

            val newAdvert = Advert(
                postType = state.postType.toString(),
                name = state.name,
                phone = state.phone,
                description = state.description,
                date = state.date,
                latitude = state.selectedLocation.latitude,    // Default location: Melbourne
                longitude = state.selectedLocation.longitude,
                locationName = state.selectedLocationName,
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
            selectedLocation != null &&
            imgUri.isNotBlank()
        }
    }
}
