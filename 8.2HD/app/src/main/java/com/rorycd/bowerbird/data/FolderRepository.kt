package com.rorycd.bowerbird.data

import android.net.Uri
import androidx.core.net.toUri

class FolderRepository(
    private val folderDao: FolderDao,
    private val processedFileDao: ProcessedFileDao
) {
    suspend fun addFolder(folder: Uri) {
        folderDao.insert(Folder(folder.toString()))
    }

    suspend fun getAllWatchedFolders() : List<Uri> {
        return folderDao.getAllFolders().map { it.uri.toUri() }
    }

    suspend fun fileHasBeenProcessed(file: Uri) : Boolean {
        return processedFileDao.exists(file.toString())
    }

    suspend fun fileHasBeenProcessedIn(file: Uri, folder: Uri) : Boolean {
        return processedFileDao.existsIn(file.toString(), folder.toString())
    }

    suspend fun getProcessedFilesIn(folder: Uri) : List<Uri> {
        return processedFileDao.getFilesInFolder(folder.toString()).map { it.folderUri.toUri() }
    }
}
