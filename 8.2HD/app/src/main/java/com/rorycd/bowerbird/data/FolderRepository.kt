package com.rorycd.bowerbird.data

import android.net.Uri
import androidx.core.net.toUri

class FolderRepository(
    private val folderDao: FolderDao,
    private val processedFileDao: ScannedFileDao
) {
    suspend fun addFolder(folder: Uri) {
        folderDao.insert(Folder(folder.toString()))
    }

    suspend fun getAllWatchedFolders(): List<Uri> {
        return folderDao.getAllFolders().map { it.uri.toUri() }
    }

    suspend fun addToScanned(file: Uri, originalFolder: Uri) {
        processedFileDao.insert(
            ScannedFile(
                fileUri = file.toString(),
                folderUri = originalFolder.toString()
            )
        )
    }

    suspend fun fileHasBeenScanned(file: Uri, folder: Uri): Boolean {
        return processedFileDao.existsIn(file.toString(), folder.toString())
    }
}
