package com.rorycd.bowerbird.rules

data class FolderWithRulesDomain(
    val folderUri: String,
    val rules: List<Rule>
)
