package com.rorycd.eventplanner.ui

import android.app.Application
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.rorycd.eventplanner.EventApplication

/**
 * Provides Factory to create instance of ViewModel for the entire Planner app
 */
object AppViewModelProvider {
    val factory = viewModelFactory {
        // Initializer for NewEventViewModel
        initializer {
            NewEventViewModel(
                eventApplication().container.eventsRepository
            )
        }
    }
}

/**
 * Extension function to queries for [Application] object and returns an instance of
 * [EventApplication].
 */
fun CreationExtras.eventApplication(): EventApplication =
    (this[AndroidViewModelFactory.APPLICATION_KEY] as EventApplication)
