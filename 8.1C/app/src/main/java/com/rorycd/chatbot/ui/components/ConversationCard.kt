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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.rorycd.chatbot.utils.formatDateAsString
import com.rorycd.chatbot.utils.formatTimeAsString

@Composable
fun ConversationCard(
    title: String,
    onSelect: () -> Unit,
    timeStamp: Long
) {
    Card(
        onClick = onSelect
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Column() {
                Text(
                    style = MaterialTheme.typography.bodyLarge,
                    text = title,
                    fontWeight = FontWeight.Bold
                )
                Row(
                    modifier = Modifier.padding(top = 4.dp)
                ) {
                    Text(
                        text = formatDateAsString(timeStamp) + " - ",
                        style = MaterialTheme.typography.bodySmall,
                    )
                    Text(
                        text = formatTimeAsString(timeStamp),
                        style = MaterialTheme.typography.bodySmall,
                    )
                }
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
        1778071516
    )
}
