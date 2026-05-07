package com.rorycd.chatbot.prompt

import android.content.Context
import android.net.Uri
import com.google.ai.edge.litertlm.Content
import com.google.ai.edge.litertlm.Contents
import com.google.ai.edge.litertlm.Conversation
import com.google.ai.edge.litertlm.ConversationConfig
import com.google.ai.edge.litertlm.Engine
import com.google.ai.edge.litertlm.EngineConfig
import com.google.ai.edge.litertlm.Message
import com.google.ai.edge.litertlm.SamplerConfig
import com.rorycd.chatbot.data.ChatMessage
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
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
        withContext(Dispatchers.IO) {
            if (engine == null) {
                val config = EngineConfig(
                    modelPath = "/data/local/tmp/llm/gemma-4-E2B-it.litertlm",
//                    backend = Backend.GPU(),
//                    visionBackend = Backend.GPU(),
                    cacheDir = context.cacheDir.absolutePath
                )
                val newEngine = Engine(config)
                newEngine.initialize()
                engine = newEngine
            }
        }
    }

    override suspend fun getResponseAsync(prompt: String, uri: Uri?): Flow<String>? {
        if (conversation == null) return null

        val contents = createContents(prompt, uri)

        return withContext(Dispatchers.IO) {
            conversation!!.sendMessageAsync(Message.user(contents))
                .catch {  }
                .map { it.toString() }
        }
    }

    override suspend fun getResponse(prompt: String, uri: Uri?): String? {
        if (conversation == null) return null

        val contents = createContents(prompt, uri)

        return withContext(Dispatchers.IO) {
            conversation!!.sendMessage(Message.user(contents)).toString()
        }
    }

    override fun startConversation(userName: String) {
        if (engine == null) return

        if (conversation != null) conversation!!.close()

        val conversationConfig = ConversationConfig(
            systemInstruction = Contents.of("You are a helpful assistant. You are talking to $userName."),
            samplerConfig = SamplerConfig(topK = 10, topP = 0.95, temperature = 0.8)
        )

        conversation = engine!!.createConversation(conversationConfig)
    }

    override fun resumeConversation(userName: String, initialMessages: List<ChatMessage>) {
        if (engine == null) return

        if (conversation != null) conversation!!.close()

        val conversationConfig = ConversationConfig(
            systemInstruction = Contents.of("You are a helpful assistant. You are talking to $userName."),
            initialMessages = initialMessages.map { chatMsg ->
                when (chatMsg.isFromUser) {
                    true -> Message.user(chatMsg.text)
                    false -> Message.model(chatMsg.text)
                }
            },
            samplerConfig = SamplerConfig(topK = 10, topP = 0.95, temperature = 0.8),
        )

        conversation = engine!!.createConversation(conversationConfig)
    }

    override fun endConversation() {
        conversation?.close()
        conversation = null
    }

    private fun createContents(prompt: String, uri: Uri?): Contents {
        var tempFile: File? = null

        if (uri != null) {
            // Get the real file path from the URI
            val inputStream = context.contentResolver.openInputStream(uri)
            tempFile = File(context.cacheDir, "temp_image.jpg")
            tempFile.outputStream().use { inputStream?.copyTo(it) }
        }

        // Change contents if image is included
        val contents = if (tempFile == null)
            Contents.of(Content.Text(prompt))
        else
            Contents.of(Content.ImageFile(tempFile.absolutePath), Content.Text(prompt))

        return contents
    }
}
