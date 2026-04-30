package com.rorycd.lostandfound.ui

import android.app.Application
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.rorycd.lostandfound.LostAndFoundApplication
import com.rorycd.lostandfound.ui.create.CreateAdvertViewModel
import com.rorycd.lostandfound.ui.details.DetailsViewModel
import com.rorycd.lostandfound.ui.itemlist.ItemListViewModel

/**
 * Provides Factory to create instance of ViewModel for the entire app
 */
object AppViewModelProvider {
    val factory = viewModelFactory {
        // Initializer for CreateAdvertViewModel
        initializer {
            CreateAdvertViewModel(
                lostAndFoundApplication().container.advertRepository
            )
        }
        // Initializer for ItemListViewModel
        initializer {
            ItemListViewModel(
                lostAndFoundApplication().container.advertRepository
            )
        }
        // Initializer for DetailsViewModel
        initializer {
            DetailsViewModel(
                createSavedStateHandle(),
                lostAndFoundApplication().container.advertRepository
            )
        }
    }
}

/**
 * Extension function to queries for [Application] object and returns an instance of
 * [LostAndFoundApplication].
 */
fun CreationExtras.lostAndFoundApplication(): LostAndFoundApplication =
    (this[AndroidViewModelFactory.APPLICATION_KEY] as LostAndFoundApplication)
