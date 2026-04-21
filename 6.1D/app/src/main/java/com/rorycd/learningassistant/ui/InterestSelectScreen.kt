package com.rorycd.learningassistant.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.rorycd.learningassistant.R
import com.rorycd.learningassistant.data.UserRepository
import kotlinx.coroutines.launch

@Composable
fun InterestSelectScreen(
    repo: UserRepository,
    onFinishSelection: () -> Unit
) {
    val interests = listOf(
        "Algorithms",
        "Data Structures",
        "Web Development",
        "Testing",
        "Front End",
        "Back End",
        "Data Science",
        "AI",
        "Quantum Computing",
        "JavaScript",
        "C",
        "Kotlin"
    )

    val selected = remember { mutableStateListOf<String>() }
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            interests.forEach { label ->
                FilterChip(
                    onClick = {
                        if (selected.contains(label)) selected.remove(label)
                        else selected.add(label)
                    },
                    label = { Text(label) },
                    selected = selected.contains(label)
                )
            }
        }
        Button(
            onClick = {
                scope.launch { repo.addInterests(selected) }
                onFinishSelection()
            }
        )
        { Text(stringResource(R.string.next)) }
    }
}
