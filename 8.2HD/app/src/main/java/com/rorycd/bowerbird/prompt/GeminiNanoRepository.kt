package com.rorycd.bowerbird.prompt

import android.content.ContentValues.TAG
import android.util.Log
import com.google.mlkit.genai.common.DownloadStatus
import com.google.mlkit.genai.common.FeatureStatus
import com.google.mlkit.genai.prompt.GenerateContentResponse
import com.google.mlkit.genai.prompt.Generation
import com.google.mlkit.genai.prompt.GenerativeModel

class GeminiNanoRepository(
    val generativeModel: GenerativeModel = Generation.getClient(),
    ) : PromptRepository {

    override suspend fun loadModel() {
        // Check if Gemini Nano is AVAILABLE, DOWNLOADABLE, or UNAVAILABLE. Then, download the feature if it is downloadable:
        val status = generativeModel.checkStatus()

        when (status) {
            FeatureStatus.UNAVAILABLE -> {
                // Gemini Nano not supported on this device or device hasn't fetched the latest configuration to support it
            }

            FeatureStatus.DOWNLOADABLE -> {
                // Gemini Nano can be downloaded on this device, but is not currently downloaded
//                var modelDownloaded = false
                generativeModel.download().collect { status ->
                    when (status) {
                        is DownloadStatus.DownloadStarted ->
                            Log.d(TAG, "starting download for Gemini Nano")

                        is DownloadStatus.DownloadProgress ->
                            Log.d(TAG, "Nano ${status.totalBytesDownloaded} bytes downloaded")

                        is DownloadStatus.DownloadCompleted -> {
                            Log.d(TAG, "Gemini Nano download complete")
//                            modelDownloaded = true
                        }

                        is DownloadStatus.DownloadFailed -> {
                            Log.e(TAG, "Nano download failed ${status.e.message}")
                        }
                    }
                }
            }

            FeatureStatus.DOWNLOADING -> {
                // Gemini Nano currently being downloaded
            }

            FeatureStatus.AVAILABLE -> {
                // Gemini Nano currently downloaded and available to use on this device
            }
        }
    }

    override suspend fun getResponse(prompt: String): String {
        val response = generativeModel.generateContent(prompt)
        return response.toString()
    }

}
