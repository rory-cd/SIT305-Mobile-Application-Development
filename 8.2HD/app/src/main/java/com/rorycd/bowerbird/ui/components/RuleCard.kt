package com.rorycd.bowerbird.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.rorycd.bowerbird.rules.FileSizeCondition
import com.rorycd.bowerbird.rules.FileSizeUnit
import com.rorycd.bowerbird.rules.FilenameCondition
import com.rorycd.bowerbird.rules.Operator
import com.rorycd.bowerbird.rules.Rule
import com.rorycd.bowerbird.rules.TagExifAction

@Composable
fun RuleCard(
    rule: Rule,
    onToggle: (Int) -> Unit
) {
    Column {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            Text(
                text = rule.name,

            )
            Switch(
                checked = rule.isEnabled,
                onCheckedChange = { onToggle(rule.id) },
                colors = SwitchDefaults.colors(
                    checkedTrackColor = MaterialTheme.colorScheme.secondary
                )
            )
        }
        HorizontalDivider()
    }
}

@Preview
@Composable
fun RuleCardPreview() {
    RuleCard(
        Rule(
            name = "Jeremy sorting mechanism",
            conditions = listOf(
                FilenameCondition(Operator.STARTS_WITH, "Jeremy"),
                FileSizeCondition(Operator.GREATER_THAN, "100", FileSizeUnit.KILOBYTES)
            ),
            actions = listOf(
                TagExifAction("detailed attributes of the person")
            )
        ),
        {}
    )
}
