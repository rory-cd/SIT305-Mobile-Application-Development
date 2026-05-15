package com.rorycd.learningassistant.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.VectorProperty
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.rorycd.learningassistant.R
import com.rorycd.learningassistant.data.QuizResultWithAnswers
import java.text.DateFormat

@Composable
fun ResultCard(
    resultWithAnswers: QuizResultWithAnswers
) {
    var isExpanded by rememberSaveable { mutableStateOf(false) }
    val score = resultWithAnswers.result.score.toDouble() / resultWithAnswers.result.maxScore
    val percentageScore = (score * 100).toInt()
    val date = DateFormat.getDateInstance().format(resultWithAnswers.result.completeDate)

    Card(
        modifier = Modifier.fillMaxWidth()
            .padding(bottom = 16.dp),
        onClick = { isExpanded = !isExpanded }
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Main card
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(start = 16.dp, top = 16.dp, end = 16.dp)
                    .fillMaxWidth()
            ) {
                Column {
                    Text(
                        text = resultWithAnswers.quiz.topic,
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = date,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.tertiary
                    )
                }
                Text(
                    text = "$percentageScore%",
                    fontWeight = FontWeight.Black
                )
            }
            // Expand icon
            val rotationAngle by animateFloatAsState(
                targetValue = if (isExpanded) 180f else 0f,
                label = "rotation"
            )
            Icon(
                painter = painterResource(id = R.drawable.keyboard_arrow_down_24px),
                contentDescription = stringResource(R.string.arrow_down_description),
                modifier = Modifier.rotate(rotationAngle)
            )
            // Result details
            if (isExpanded) {
                HorizontalDivider(Modifier.padding(top = 4.dp))

                resultWithAnswers.answers.forEach {
                    val answer = it.answer
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        val answerAsChar = answer.selectedAnswer.toCharArray().first()
                        val answerIndex = answerAsChar.code - 65

                        // Question
                        Text(
                            text = it.question.title,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(bottom = 4.dp)
                        )
                        // Options
                        it.question.options.forEachIndexed { idx, option ->
                            val textColour =
                                when (idx) {
                                    answerIndex if answer.isCorrect -> Color.Green
                                    answerIndex -> Color.Red
                                    else -> MaterialTheme.colorScheme.onSurfaceVariant
                                }
                            BulletPointItem((65 + idx).toChar(), option, textColour)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun BulletPointItem(prefix: Char, text: String, colour: Color) {
    Row(modifier = Modifier.padding(vertical = 2.dp)) {
        Text(
            text = "$prefix:",
            color = colour,
            modifier = Modifier.width(24.dp)
        )
        Text(
            text = text,
            color = colour,
            modifier = Modifier.weight(1f)
        )
    }
}
