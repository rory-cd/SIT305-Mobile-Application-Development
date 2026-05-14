package com.rorycd.lostandfound.ui.map

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.rorycd.lostandfound.R
import com.rorycd.lostandfound.navigation.NavigationDestination
import com.rorycd.lostandfound.ui.AppViewModelProvider
import com.rorycd.lostandfound.ui.components.LoadingSpinner

/**
 * Destination class for NavGraph route to [MapScreen]
 */
object MapDestination : NavigationDestination {
    override val route = "map"
    override val titleRes = R.string.map_destination_title
}

/**
 * Screen where all adverts are shown on a map
 */
@Composable
fun MapScreen(
    modifier: Modifier = Modifier,
    viewModel: MapViewModel = viewModel(factory = AppViewModelProvider.factory)
) {
    val location by viewModel.currentLocation.collectAsStateWithLifecycle()
    val items by viewModel.items.collectAsStateWithLifecycle()

    // Initialise camera position at Melbourne
    val cameraPosition = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(LatLng(-37.813679, 144.962979), 12f)
    }

    // Update camera when location is retrieved
    LaunchedEffect(location) {
        location?.let {
            cameraPosition.position = CameraPosition.fromLatLngZoom(it, 12f)
        }
    }

    val locationPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val granted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
                permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true
        if (granted) {
            viewModel.setCurrentLocation()
        }
    }

    // Get current location
    LaunchedEffect(Unit) {
        locationPermissionLauncher.launch(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        )
    }

    Box(modifier = Modifier.fillMaxSize()) {
        GoogleMap(
            cameraPositionState = cameraPosition,
            modifier = Modifier.fillMaxSize()
        ) {
            for (item in items) {
                Marker(
                    state = MarkerState(position = LatLng(item.latitude, item.longitude)),
                    title = "${item.postType}: ${item.name}",
                    snippet = item.locationName
                )
            }
        }
        if (location == null) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.4f))
            ) {
                LoadingSpinner(stringResource(R.string.loading_map_message))
            }
        }
    }
}
