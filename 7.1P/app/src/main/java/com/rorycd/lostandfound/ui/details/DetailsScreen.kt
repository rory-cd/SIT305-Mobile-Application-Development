package com.rorycd.lostandfound.ui.details

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.rorycd.lostandfound.R
import com.rorycd.lostandfound.navigation.NavigationDestination
import com.rorycd.lostandfound.ui.AppViewModelProvider

/**
 * Destination class for NavGraph route to [DetailsScreen]
 */
object DetailsDestination : NavigationDestination {
    override val route = "details"
    override val titleRes = R.string.item_details_destination_title

    const val ITEM_ID_ARG = "itemId"
    val routeWithArgs = "${route}/{$ITEM_ID_ARG}"
}

/**
 * Screen where item details are displayed
 */
@Composable
fun DetailsScreen(
    onDeleteItem: () -> Unit,
    viewModel: DetailsViewModel = viewModel(factory = AppViewModelProvider.factory)
) {
    // Collect state flow
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    Column {
        // Image
        AsyncImage(
            model = state.imgUri,
            contentDescription = stringResource(id = R.string.item_image_description),
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .padding(bottom = 16.dp)
                .fillMaxWidth()
                .height(200.dp)
                .background(MaterialTheme.colorScheme.tertiaryContainer)
        )
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.padding(20.dp)
        ) {
            // Title
            Text(
                text = "${state.postType}: ${state.name}",
                style = MaterialTheme.typography.headlineSmall,
            )
            // Description
            Text(
                text = state.description,
            )
            // Location
            Text(
                text = "Location: ${state.location}",
            )
            // Date
            Text(
                text = state.dateFormatted,
            )
            // Phone
            Text(
                text = "Phone: ${state.phone}",
            )
        }
        // Delete
        Button(
            onClick = {
                viewModel.deleteItem()
                onDeleteItem()
            },
            modifier = Modifier.fillMaxWidth()
                .padding(top = 24.dp)
        ) {
            Text(
                stringResource(R.string.delete_item_button),
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(8.dp)
            )
        }
    }

    // Show toast if toast message is set
    LaunchedEffect(state.toastMessage) {
        state.toastMessage?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            viewModel.clearToast()
        }
    }
}
