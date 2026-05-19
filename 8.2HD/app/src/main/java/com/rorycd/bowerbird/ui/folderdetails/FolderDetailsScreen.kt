package com.rorycd.bowerbird.ui.folderdetails

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.rorycd.bowerbird.R
import com.rorycd.bowerbird.ui.components.FolderRuleCard
import com.rorycd.bowerbird.utils.getFolderNameFromUri

/**
 * Screen showing the list of rules applied to the folder
 */
@Composable
fun FolderDetailsScreen(
    onSelectRule: (Int) -> Unit,
    viewModel: FolderDetailsViewModel = hiltViewModel()
) {
    val folderDetails by viewModel.folderDetails.collectAsStateWithLifecycle()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        item {
            // Folder name
            Text(
                text = getFolderNameFromUri(folderDetails?.folderUri?.toUri()),
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                ),
                modifier = Modifier
                    .padding(bottom = 10.dp, start = 4.dp, top = 8.dp)
                    .fillMaxWidth()
            )
        }

        if (folderDetails == null) item {
            CircularProgressIndicator()
        } else {
            val (active, inactive) = folderDetails!!.rules.partition { it.isEnabled }

            if (active.isEmpty() && inactive.isEmpty()) item {
                Text(stringResource(R.string.no_rules_for_folder))
            }

            // Active rules
            item {
                if (active.isNotEmpty()) Text(
                    text = stringResource(R.string.active_rules),
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(top = 14.dp)
                )
            }

            items(active) { rule ->
                FolderRuleCard(
                    rule = rule,
                    onSelect = { onSelectRule(rule.id) },
                    onRemove = {
                        viewModel.removeRuleFromFolder(folderDetails!!.folderUri, rule.id)
                    }
                )
            }

            // Inactive rules
            item {
                if (inactive.isNotEmpty()) Text(
                    text = stringResource(R.string.inactive_rules),
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(top = if (active.isNotEmpty()) 26.dp else 14.dp)
                )
            }

            items(inactive) { rule ->
                FolderRuleCard(
                    rule = rule,
                    onSelect = { onSelectRule(rule.id) },
                    onRemove = {
                        viewModel.removeRuleFromFolder(folderDetails!!.folderUri, rule.id)
                    }
                )
            }
        }
    }
}
