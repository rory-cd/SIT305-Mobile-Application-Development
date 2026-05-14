package com.rorycd.lostandfound.ui.create

import android.Manifest
import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import com.rorycd.lostandfound.R
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.rorycd.lostandfound.data.PostType
import com.rorycd.lostandfound.navigation.NavigationDestination
import com.rorycd.lostandfound.ui.AppViewModelProvider
import com.rorycd.lostandfound.ui.components.DatePickerText
import com.rorycd.lostandfound.ui.components.RadioGroupHorizontal
import com.rorycd.lostandfound.utils.formatDateAsString
import coil.compose.AsyncImage
import com.rorycd.lostandfound.ui.components.AutoCompleteLocation

/**
 * Destination class for NavGraph route to [CreateAdvertScreen].
 */
object CreateDestination : NavigationDestination {
    override val route = "create_advert"
    override val titleRes = R.string.create_destination_title
}

/**
 * Composable for create advert screen
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateAdvertScreen(
    onAdvertCreated: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: CreateAdvertViewModel = viewModel(factory = AppViewModelProvider.factory)
) {
    // Collect state flow
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    val context = LocalContext.current

    val locationPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val granted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
                permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true
        if (granted) {
            viewModel.onUseCurrentLocation()
        }
    }

    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.Start,
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp)
            .padding(top = 16.dp)
    ) {
        RadioGroupHorizontal(
            PostType.entries,
            state.postType,
            { postType -> viewModel.onPostTypeChanged(postType) }
        )

        // Name
        OutlinedTextField(
            value = state.name,
            onValueChange = { viewModel.onNameChanged(it) },
            label = { Text(stringResource(R.string.name_input_label)) },
            singleLine = true,
            maxLines = 1,
            modifier = Modifier.fillMaxWidth()
        )

        // Phone
        OutlinedTextField(
            value = state.phone,
            onValueChange = { input -> viewModel.onPhoneChanged(input) },
            label = { Text(stringResource(R.string.phone_input_label)) },
            singleLine = true,
            maxLines = 1,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Phone,
                imeAction = ImeAction.Next
            ),
            modifier = Modifier.fillMaxWidth()
        )

        // Description
        OutlinedTextField(
            value = state.description,
            onValueChange = { viewModel.onDescriptionChanged(it) },
            label = { Text(stringResource(R.string.description_input_label)) },
            singleLine = false,
            maxLines = 4,
            minLines = 4,
            modifier = Modifier.fillMaxWidth()
        )

        // Date
        DatePickerText(
            value = formatDateAsString(state.date),
            onDateSelected = { millis -> viewModel.onDateChanged(millis) },
            modifier = Modifier.padding(top = 4.dp)
        )

        // Location
        AutoCompleteLocation(
            value = state.locationInput,
            onValueChange = { viewModel.onLocationChanged(it) },
            onSelectLocation = { viewModel.onSelectLocation(it) },
            predictions = state.locationPredictions,
            label = stringResource(R.string.location_input_label),
            isLoading = state.isLoadingPredictions,
            locationSelected = state.selectedLocation != null,
            modifier = Modifier.fillMaxWidth()
        )

        // Current location button
        Button(
            onClick = {
                locationPermissionLauncher.launch(
                    arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    )
                )

                viewModel.onUseCurrentLocation()
            },
            modifier = Modifier.fillMaxWidth()
                .padding(top = 8.dp)
        ) {
            Text(stringResource(R.string.use_current_location))
        }

        Text(
            text = stringResource(R.string.item_image_label),
            modifier = Modifier.padding(top = 8.dp)
        )
        // Image
        Box(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = 16.dp)
        ) {
            val imageSelectLauncher =
                rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
                    if (uri != null) {
                        context.contentResolver.takePersistableUriPermission(
                            uri,
                            Intent.FLAG_GRANT_READ_URI_PERMISSION
                        )
                        viewModel.onImageSelected(uri.toString())
                    }
                }
            AsyncImage(
                model = state.imgUri,
                contentDescription = stringResource(id = R.string.item_image_description),
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(200.dp)
                    .background(MaterialTheme.colorScheme.tertiaryContainer)
                    .border(
                        width = 4.dp,
                        color = MaterialTheme.colorScheme.primary
                    )
            )
            IconButton(
                onClick = {
                    imageSelectLauncher.launch(
                        input = PickVisualMediaRequest(
                            mediaType = ActivityResultContracts.PickVisualMedia.ImageOnly
                        )
                    )
                },
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.primary)
                    .align(Alignment.BottomEnd)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.add_24px),
                    contentDescription = stringResource(R.string.checkmark_desc),
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
        }

        // Create button
        Button(
            onClick = {
                viewModel.createAdvert()
                onAdvertCreated()
            },
            enabled = state.isValid,
            modifier = Modifier.fillMaxWidth()
                .padding(top = 8.dp)
        ) {
            Text(stringResource(R.string.create_advert_confirm_button))
        }
    }
}
