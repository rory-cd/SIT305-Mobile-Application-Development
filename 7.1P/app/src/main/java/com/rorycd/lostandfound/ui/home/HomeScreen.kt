package com.rorycd.lostandfound.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.rorycd.lostandfound.R
import com.rorycd.lostandfound.navigation.NavigationDestination

/**
 * Destination class for NavGraph route to [HomeScreen]
 */
object HomeDestination : NavigationDestination {
    override val route = "home"
    override val titleRes = R.string.home_destination_title
}

/**
 * Screen where users can choose to create a new advert or show all
 */
@Composable
fun HomeScreen(
    onCreateAdvert: () -> Unit,
    onShowAllItems: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize()
            .padding(16.dp)
    ) {
        // New advert
        Button(
            onClick = onCreateAdvert,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                stringResource(R.string.new_advert_button),
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(8.dp)
            )
        }
        // Show item list
        Button(
            onClick = onShowAllItems,
            modifier = Modifier.fillMaxWidth()
                .padding(top = 24.dp)
        ) {
            Text(
                stringResource(R.string.show_items_button),
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}

@Preview
@Composable
fun HomeScreenPreview() {
    HomeScreen({}, {})
}
