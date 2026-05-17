package com.rorycd.bowerbird.rules

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class Operator {
    CONTAINS,
    ENDS_WITH,
    STARTS_WITH,
    GREATER_THAN,
    EQUALS,
    LESS_THAN
}

@Serializable
sealed interface RuleCondition {
    interface BasicCondition : RuleCondition {
        val operator: Operator
        val operand: String
        val validOperators: List<Operator>
        fun check(context: Context, fileUri: Uri): Boolean
    }

    interface SmartCondition : RuleCondition {
        val condition: String
    }
}

@Serializable
@SerialName("filename")
data class FilenameCondition(
    override val operator: Operator,
    override val operand: String,
    override val validOperators: List<Operator> = listOf(
        Operator.STARTS_WITH,
        Operator.CONTAINS,
        Operator.ENDS_WITH
    )
) : RuleCondition.BasicCondition {

    override fun check(context: Context, fileUri: Uri): Boolean {

        val filename = getFileName(context, fileUri) ?: return false

        return when (operator) {
            Operator.STARTS_WITH -> filename.startsWith(operand)
            Operator.ENDS_WITH -> filename.endsWith(operand)
            Operator.CONTAINS -> filename.contains(operand)
            else -> false
        }
    }

    private fun getFileName(context: Context, uri: Uri): String? {
        return context.contentResolver.query(uri, null, null, null, null)?.use { cursor ->
            val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            cursor.moveToFirst()
            cursor.getString(nameIndex)
        }
    }
}

@Serializable
enum class FileSizeUnit {
    KILOBYTES, MEGABYTES
}

@Serializable
@SerialName("file_size")
data class FileSizeCondition(
    override val operator: Operator,
    override val operand: String,
    val unit: FileSizeUnit,
    override val validOperators: List<Operator> = listOf(
        Operator.LESS_THAN,
        Operator.EQUALS,
        Operator.GREATER_THAN
    )
) : RuleCondition.BasicCondition {

    override fun check(context: Context, fileUri: Uri): Boolean {

        val fileSizeBytes = getFileSize(context, fileUri) ?: return false

        val operandValue = operand.toLongOrNull() ?: 0L
        val multiplier = when (unit) {
            FileSizeUnit.KILOBYTES -> 1000L
            FileSizeUnit.MEGABYTES -> 1000000L
        }
        val checkSizeBytes = operandValue * multiplier

        return when (operator) {
            Operator.LESS_THAN -> fileSizeBytes < checkSizeBytes
            Operator.EQUALS -> (fileSizeBytes / multiplier) == (checkSizeBytes / multiplier)
            Operator.GREATER_THAN -> fileSizeBytes > checkSizeBytes
            else -> false
        }
    }

    private fun getFileSize(context: Context, uri: Uri): Long? {
        return context.contentResolver.query(uri, null, null, null, null)?.use { cursor ->
            val sizeIndex = cursor.getColumnIndex(OpenableColumns.SIZE)
            cursor.moveToFirst()
            cursor.getLong(sizeIndex)
        }
    }
}

@Serializable
@SerialName("image_check")
data class ImageCheckCondition(
    override val condition: String
) : RuleCondition.SmartCondition {

}
