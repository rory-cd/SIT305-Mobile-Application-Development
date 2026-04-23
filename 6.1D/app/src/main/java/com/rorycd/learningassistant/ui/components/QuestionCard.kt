package com.rorycd.learningassistant.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun QuestionCard(
    idx: Int,
    question: String,
    answers: List<String>,
    onAnswerSelected: (String) -> Unit,
    modifier: Modifier = Modifier,
    selectedAnswer: String? = null
) {
    Card(
        modifier = modifier
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "$idx. $question",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Column(
                Modifier.selectableGroup(),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                answers.forEach { text ->
                    Row(
                        Modifier.fillMaxWidth()
                            .selectable(
                                selected = (text == selectedAnswer),
                                onClick = { onAnswerSelected(text) },
                                role = Role.RadioButton
                            )
                    ) {
                        RadioButton(
                            selected = (text == selectedAnswer),
                            onClick = null
                        )
                        Text(
                            text = text,
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.padding(start = 16.dp)
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun PreviewQuestionCard() {
    QuestionCard(
        1,
        "What's a battle?",
        listOf("Did that boy just say what's a battle?", "No he said what's that rattle.", "A sponge."),
        onAnswerSelected = {})
}
