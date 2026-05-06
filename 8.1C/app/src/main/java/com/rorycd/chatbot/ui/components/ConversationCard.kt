package com.rorycd.chatbot.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.rorycd.chatbot.utils.formatDateAsString
import com.rorycd.chatbot.utils.formatTimeAsString

@Composable
fun ConversationCard(
    title: String,
    onSelect: () -> Unit,
    summary: String?,
    timeStamp: Long
) {
    Card(
        onClick = onSelect
    ) {
        Row(
            modifier = Modifier.fillMaxWidth()
                .padding(16.dp)
        ) {
            Column() {
                Text(
                    style = MaterialTheme.typography.headlineSmall,
                    text = title
                )
                Text(
                    text = formatDateAsString(timeStamp),
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(top = 2.dp)
                )
                Text(
                    text = summary ?: "",
                    maxLines = 3,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }
    }
}

@Preview
@Composable
fun PreviewConversationCard() {
    ConversationCard(
        "A great conversation",
        {},
        "Some sort of summary which is really nice and great and I love it because it should truncate but I bet it doesn't because soemtimes it works but now I need to check another folder and what a pain I meant project ah well whatever",
        1778071516
    )
}
