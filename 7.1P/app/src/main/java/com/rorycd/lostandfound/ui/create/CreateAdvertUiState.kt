package com.rorycd.lostandfound.ui.create

import com.rorycd.lostandfound.data.PostType
import java.time.Instant

/**
 * Represents the current state of the [CreateAdvertScreen] UI
 */
data class CreateAdvertUiState(
    val postType: PostType = PostType.LOST,
    val name: String = "",
    val phone: String = "",
    val description: String = "",
    val date: Long = Instant.now().toEpochMilli(),
    val location: String = "",
    val imgUri: String = "",
    val isValid: Boolean = false
)
