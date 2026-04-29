package com.rorycd.lostandfound.ui.itemlist

/**
 * Model for representing an Advert in UI
 */
data class ItemUiModel (
    val id: Int,
    val postType: String,
    val name: String,
    val phone: String,
    val description: String,
    val dateFormatted: String,
    val location: String
)
