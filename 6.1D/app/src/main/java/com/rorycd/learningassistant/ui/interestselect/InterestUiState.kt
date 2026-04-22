package com.rorycd.learningassistant.ui.interestselect

/**
 * Represents the current state of the [InterestSelectScreen] UI
 */
data class InterestUiState (
    val selected: Set<String> = emptySet()
)
