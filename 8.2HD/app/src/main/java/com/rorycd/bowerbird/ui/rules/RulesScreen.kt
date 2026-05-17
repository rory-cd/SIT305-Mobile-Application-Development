package com.rorycd.bowerbird.ui.rules

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.rorycd.bowerbird.R

/**
 * Screen showing the full list of rules
 */
@Composable
fun RulesScreen(
    viewModel: RulesViewModel = hiltViewModel()
) {
    val rules by viewModel.rules.collectAsStateWithLifecycle()

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier
        .fillMaxSize()
        .padding(horizontal = 16.dp)
    ) {
        if (rules.isNullOrEmpty()) item {
            Text(stringResource(R.string.no_rules_message))
        }

        items(rules ?: emptyList()) { rule ->
            Text(rule.toString())
        }
    }
}
