package com.rorycd.lostandfound.ui.map

import android.Manifest
import android.app.Application
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.location.CurrentLocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.model.LatLng
import com.rorycd.lostandfound.data.Advert
import com.rorycd.lostandfound.data.AdvertRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn

/**
 * View model for [MapScreen]
 */
class MapViewModel(
    application: Application,
    advertRepository: AdvertRepository
) : AndroidViewModel(application) {
    val items: StateFlow<List<Advert>> = advertRepository.getAllAdvertStream()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    private val _currentLocation = MutableStateFlow<LatLng?>(null)
    val currentLocation = _currentLocation.asStateFlow()

    init {
        setCurrentLocation()
    }

    fun setCurrentLocation() {
        val context = getApplication<Application>().applicationContext
        val fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)

        // Ensure permissions are granted
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED) return

        val locationRequest = CurrentLocationRequest.Builder()
            .setPriority(Priority.PRIORITY_HIGH_ACCURACY)
            .build()

        fusedLocationProviderClient.getCurrentLocation(locationRequest, null).addOnSuccessListener {location ->
            location?.let {
                _currentLocation.value = LatLng(it.latitude, it.longitude)
            }
        }
    }
}
