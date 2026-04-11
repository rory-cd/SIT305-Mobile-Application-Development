package com.rorycd.eventplanner.navigation

import androidx.annotation.StringRes
import com.rorycd.eventplanner.R

enum class Screen(@StringRes val title: Int) {
    EventList(title = R.string.app_name),
    AddEvent(title = R.string.add_event),
    EditEvent(title = R.string.edit_event)
}
