package com.rorycd.bowerbird.prompt

import android.net.Uri

interface PromptRepository {
    suspend fun loadModel()
    suspend fun getResponse(prompt: String, uri: Uri): String
}
