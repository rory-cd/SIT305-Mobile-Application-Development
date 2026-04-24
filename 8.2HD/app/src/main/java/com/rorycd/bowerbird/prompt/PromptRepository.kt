package com.rorycd.bowerbird.prompt

interface PromptRepository {
    suspend fun loadModel()
    suspend fun getResponse(prompt: String): String
}
