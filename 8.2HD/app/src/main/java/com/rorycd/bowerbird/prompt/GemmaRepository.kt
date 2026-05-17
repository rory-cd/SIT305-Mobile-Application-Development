package com.rorycd.bowerbird.prompt

import android.content.Context
import android.net.Uri
import com.google.ai.edge.litertlm.Backend
import com.google.ai.edge.litertlm.Content
import com.google.ai.edge.litertlm.Conversation
import com.google.ai.edge.litertlm.Engine
import com.google.ai.edge.litertlm.EngineConfig
import com.google.ai.edge.litertlm.Message
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GemmaRepository @Inject constructor(
    @ApplicationContext private val context: Context
) : PromptRepository {

    private var engine: Engine? = null
    private var conversation: Conversation? = null

    override suspend fun loadModel() {

        if (conversation != null) return

        if (engine == null) {
            val config = EngineConfig(
                modelPath = "/data/local/tmp/llm/gemma-4-E2B-it.litertlm",
                backend = Backend.GPU(),
                visionBackend = Backend.GPU(),
                cacheDir = context.cacheDir.absolutePath
            )
            val newEngine = Engine(config)
            newEngine.initialize()
            engine = newEngine
        }

        if (conversation == null) {
            conversation = engine!!.createConversation()
        }
    }

    override suspend fun getResponse(prompt: String, uri: Uri): String {
        // Get the real file path from the URI
        val inputStream = context.contentResolver.openInputStream(uri)
        val tempFile = File(context.cacheDir, "temp_image.jpg")
        tempFile.outputStream().use { inputStream?.copyTo(it) }

        val message = Message.of(
            Content.ImageFile(tempFile.absolutePath),
            Content.Text(prompt)
        )

        return withContext(Dispatchers.IO) {
            conversation?.sendMessage(message).toString()
        }
    }

    fun close() {
        conversation?.close()
        conversation = null
        engine?.close()
        engine = null
    }
}