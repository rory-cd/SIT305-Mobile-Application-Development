package com.rorycd.bowerbird.rules

import android.content.Context
import androidx.exifinterface.media.ExifInterface
import android.net.Uri
import android.provider.DocumentsContract
import android.webkit.MimeTypeMap
import androidx.core.net.toUri
import androidx.documentfile.provider.DocumentFile
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed interface RuleAction {
    fun executeAction(context: Context, fileUri: Uri?)

    interface BasicAction : RuleAction {}

    interface SmartAction : RuleAction {}
}

@Serializable
@SerialName("tag_exif")
data class TagExif(
    val prompt: String
) : RuleAction.SmartAction {
    var tagList: List<String>? = null

    override fun executeAction(context: Context, fileUri: Uri?) {
        val tags = tagList
        if (fileUri == null || tags.isNullOrEmpty()) return
        context.contentResolver.openFileDescriptor(fileUri, "rw")?.use { pfd ->
            val exif = ExifInterface(pfd.fileDescriptor)
            val tagsString = tags.joinToString(", ")
            exif.setAttribute(ExifInterface.TAG_USER_COMMENT, "Bowerbird Tags: $tagsString")
            exif.setAttribute(ExifInterface.TAG_IMAGE_DESCRIPTION, "Bowerbird Tags: $tagsString")
            exif.saveAttributes()
        }
    }

    fun setTags(newTagList: List<String>) {
        tagList = newTagList
    }
}

@Serializable
@SerialName("rename")
data class Rename(
    val newName: String
) : RuleAction.BasicAction {
    override fun executeAction(context: Context, fileUri: Uri?) {
        if (fileUri == null) return
        val contentResolver = context.contentResolver

        val type = contentResolver.getType(fileUri)
        val ext = MimeTypeMap.getSingleton().getExtensionFromMimeType(type)

        val newUri = DocumentsContract.renameDocument(
            contentResolver,
            fileUri,
            "$newName.$ext"
        )
    }
}

@Serializable
@SerialName("move")
data class Move(
    val targetFolder: String?
) : RuleAction.BasicAction {
    override fun executeAction(context: Context, fileUri: Uri?) {
        if (fileUri == null || targetFolder == null) return

        val sourceFile = DocumentFile.fromSingleUri(context, fileUri) ?: return
        val destDir = DocumentFile.fromTreeUri(context, targetFolder.toUri()) ?: return

        val destFile = destDir.createFile(
            sourceFile.type ?: "application/octet-stream",
            sourceFile.name ?: "copy"
        ) ?: return

        context.contentResolver.openInputStream(fileUri)?.use { input ->
            context.contentResolver.openOutputStream(destFile.uri)?.use { output ->
                input.copyTo(output)
            }
        }
        sourceFile.delete()
    }
}

@Serializable
@SerialName("copy")
data class Copy(
    val targetFolder: String?
) : RuleAction.BasicAction {
    override fun executeAction(context: Context, fileUri: Uri?) {
        if (fileUri == null || targetFolder == null) return

        val sourceFile = DocumentFile.fromSingleUri(context, fileUri) ?: return
        val destDir = DocumentFile.fromTreeUri(context, targetFolder.toUri()) ?: return

        val destFile = destDir.createFile(
            sourceFile.type ?: "application/octet-stream",
            sourceFile.name ?: "copy"
        ) ?: return

        context.contentResolver.openInputStream(fileUri)?.use { input ->
            context.contentResolver.openOutputStream(destFile.uri)?.use { output ->
                input.copyTo(output)
            }
        }
    }
}
