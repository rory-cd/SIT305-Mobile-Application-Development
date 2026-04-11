package com.rorycd.eventplanner.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Card
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.dp
import com.rorycd.eventplanner.R

@Composable
fun EventCard(
    title: String,
    location: String?,
    category: String?,
    date: String,
    time: String,
    onSelectCard: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = onSelectCard,
        modifier = modifier
    ) {
        Column (
            modifier = Modifier
                .padding(all = dimensionResource(R.dimen.padding_medium))
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(title)
                Text(date)
            }
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                // Location
                if (location != null) {
                    Row (verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Filled.LocationOn,
                            contentDescription = null,
                            modifier = Modifier
                                .padding(end = 4.dp)
                                .size(16.dp)
                        )
                        Text(location ?: "")
                    }
                }
                Spacer(modifier = Modifier.weight(1f))
                Text(time)
            }
            // Category
            if (category != null) {
                FilterChip(
                    selected = false,
                    onClick = { },
                    label = { Text(category) }
                )
            }
        }
    }
}
