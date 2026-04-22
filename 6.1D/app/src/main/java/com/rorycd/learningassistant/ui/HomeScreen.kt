package com.rorycd.learningassistant.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.rorycd.learningassistant.R
import com.rorycd.learningassistant.data.QuizRepository
import com.rorycd.learningassistant.network.QuizResponse
import com.rorycd.learningassistant.data.UserRepository

@Composable
fun HomeScreen(
    onLogOut: () -> Unit,
    userRepo: UserRepository,
    quizRepo: QuizRepository
) {
    val collectedState by userRepo.getCurrentUserFlow().collectAsStateWithLifecycle(null)
    var quizResponse by remember { mutableStateOf<QuizResponse?>(null) }
    val state = collectedState
    val scope = rememberCoroutineScope()


    LaunchedEffect(Unit) {
        quizResponse = quizRepo.fetchQuiz("Android")
    }

    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .padding(horizontal = dimensionResource(R.dimen.padding_medium))
    ) {
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                // Welcome message
                Text(
                    text = stringResource(R.string.hello_message),
                    style = MaterialTheme.typography.displaySmall
                )
                Text(
                    text = collectedState?.username ?: "",
                    style = MaterialTheme.typography.displayLarge
                )
            }
            Column(
                horizontalAlignment = Alignment.End,
                modifier = Modifier
                    .padding(start = 16.dp)
                    .width(75.dp)
            ) {
                AsyncImage(
                    model = state?.imgUri,
                    contentDescription = stringResource(id = R.string.user_image_description),
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(75.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.tertiaryContainer)
                        .border(
                            width = 2.dp,
                            color = MaterialTheme.colorScheme.primary,
                            shape = CircleShape
                        )
                )
                TextButton(
                    onClick = {
                        userRepo.logOut()
                        onLogOut()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Text(stringResource(R.string.log_out_button))
                }
            }
        }
        quizResponse?.quiz?.forEach { quizItem ->
            Text(quizItem.question)
        }
    }
}
