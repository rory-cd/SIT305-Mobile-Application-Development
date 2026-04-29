package com.rorycd.bowerbird.prompt

enum class PromptOutputs {
    TAG, FILENAME
}

data class PromptOptions(
    val conditions: List<String>? = null,
    val subjectDescriptions: List<String>? = null,
    val promptOutputs: List<PromptOutputs>
)
