package com.rorycd.chatbot.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults.cardColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.rorycd.chatbot.utils.formatTimeAsString

@Composable
fun ChatBubble(
    text: String,
    color: Color,
    modifier: Modifier? = Modifier,
    timeStamp: Long? = null
) {
    Card(
        colors = cardColors(color),
        modifier = Modifier.fillMaxWidth(0.85f)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
                .padding(8.dp)
        ) {
            Text(
                text = text
            )
            Text(
                text = formatTimeAsString(timeStamp ?: 0),
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(top = 2.dp)
                    .align(Alignment.End)
            )
        }
    }
}
