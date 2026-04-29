package com.rorycd.lostandfound.ui.itemlist

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.isTraversalGroup
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.traversalIndex
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.rorycd.lostandfound.R
import com.rorycd.lostandfound.navigation.NavigationDestination
import com.rorycd.lostandfound.ui.AppViewModelProvider
import com.rorycd.lostandfound.ui.components.ItemSearchBar
import com.rorycd.lostandfound.ui.components.ItemSearchBar
import com.rorycd.lostandfound.ui.create.CreateAdvertScreen
import com.rorycd.lostandfound.ui.create.CreateAdvertViewModel

/**
 * Destination class for NavGraph route to [CreateAdvertScreen].
 */
object ItemListDestination : NavigationDestination {
    override val route = "item_list"
    override val titleRes = R.string.item_list_destination_title
}

/**
 * Composable for create advert screen
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItemListScreen(
    onSelectItem: (Int) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ItemListViewModel = viewModel(factory = AppViewModelProvider.factory)
) {
    // Collect state
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    // Retrieve all events sorted by date
    val items by viewModel.items.collectAsStateWithLifecycle()
    val filteredItems = items.filter { it.name.contains(state.query, ignoreCase = true) }

    LazyColumn(
        Modifier.padding(24.dp)
    ) {
        // Header elements
        item {
            Text(
                text = stringResource(R.string.item_list_title),
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(bottom = 24.dp)
            )
            TextField(
                value = state.query,
                onValueChange = { input -> viewModel.onQueryChanged(input) },
                label = { Text(stringResource(R.string.filter_input_label)) },
                singleLine = true,
                maxLines = 1,
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Search
                ),
                modifier = Modifier.fillMaxWidth()
            )
        }

        // Item list
        items(filteredItems) {
            ListItem(
                headlineContent = { Text(it.name) },
                supportingContent = { Text(it.postType) },
                colors = ListItemDefaults.colors(containerColor = Color.Transparent),
                modifier = Modifier
                    .clickable {
                        onSelectItem(it.id)
                    }
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp)
            )
        }
    }
}
