package com.rorycd.chatbot.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Coronavirus
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults.cardColors
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.rorycd.chatbot.R
import com.rorycd.chatbot.utils.formatTimestampRelative

@Composable
fun ChatBubble(
    text: String,
    color: Color,
    showIcon: Boolean,
    modifier: Modifier? = Modifier,
    timeStamp: Long? = null
) {
    Card(
        colors = cardColors(color),
        modifier = Modifier.fillMaxWidth(0.85f)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
                .padding(16.dp)
        ) {
            if (showIcon) Icon(
                imageVector = Icons.Outlined.Coronavirus,
                contentDescription = stringResource(R.string.ai_icon),
                tint = colorResource(R.color.teal_200),
                modifier = Modifier.size(24.dp)
                    .padding(bottom = 4.dp)
            )
            Text(
                text = text
            )
            if (timeStamp != null)
            Text(
                text = formatTimestampRelative(timeStamp ?: 0),
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(top = 4.dp)
                    .align(Alignment.End)
            )
        }
    }
}
