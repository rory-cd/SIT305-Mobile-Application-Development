package com.rorycd.eventplanner.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import com.rorycd.eventplanner.R

val eventsTest = listOf("Brunch with Jimbo", "Drop the kids at Footy", "Enya concert")

@Composable
fun EventListScreen(
    events: List<String>,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_small)),
        modifier = Modifier.padding(all = dimensionResource(R.dimen.padding_small))
    ) {
        items(events) {
            EventCard(
                title = it,
                modifier = modifier
            )
        }
    }
}

@Composable
fun EventCard(
    title: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
    ) {
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = title,
                modifier = Modifier.padding(all = dimensionResource(R.dimen.padding_small))
            )
        }
    }
}

@Preview
@Composable
fun EventListScreenPreview() {
    EventListScreen(eventsTest)
}
