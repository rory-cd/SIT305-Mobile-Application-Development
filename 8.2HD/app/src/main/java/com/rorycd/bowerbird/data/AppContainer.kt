package com.rorycd.bowerbird.data

import android.content.Context
import com.rorycd.bowerbird.prompt.PromptRepository
import com.rorycd.bowerbird.prompt.GemmaRepository

/**
 * App container for dependency injection
 */
interface AppContainer {
    val promptRepository: PromptRepository
}

/**
 * AppContainer implementation
 */
class AppDataContainer(private val context: Context) : AppContainer {
    override val promptRepository: PromptRepository by lazy {
        GemmaRepository(context)
    }
}
