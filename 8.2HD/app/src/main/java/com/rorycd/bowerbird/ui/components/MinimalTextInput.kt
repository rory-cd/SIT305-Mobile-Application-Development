package com.rorycd.bowerbird.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.sp

@Composable
fun MinimalTextInput(
    value: String,
    onValueChange: (String) -> Unit,
    hint: String,
    modifier: Modifier = Modifier,
    largeText: Boolean = false,
    numbersOnly: Boolean = false,
    maxLines: Int = 1,
    charLimit: Int = 100
) {
    Box(modifier = modifier) {
        if (value.isEmpty()) {
            // Hint
            Text(
                text = hint,
                style =
                    if (largeText) MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp)
                    else MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
            )
        }

        BasicTextField(
            value = value,
            onValueChange = { newValue ->
                val isValidInputType = !numbersOnly || (newValue.all { it.isDigit() })
                if (isValidInputType && newValue.length < charLimit) {
                    onValueChange(newValue)
                }
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = if (numbersOnly) KeyboardType.Number else KeyboardType.Text
            ),
            textStyle =
                if (largeText) MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp)
                else MaterialTheme.typography.bodyLarge,
            maxLines = maxLines,
            cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
            modifier = Modifier.fillMaxWidth()
        )
    }
}
