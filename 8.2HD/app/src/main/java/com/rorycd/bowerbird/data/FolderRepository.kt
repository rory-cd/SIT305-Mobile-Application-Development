package com.rorycd.bowerbird.data

import android.net.Uri
import androidx.core.net.toUri
import com.rorycd.bowerbird.rules.FolderWithRulesDomain
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FolderRepository @Inject constructor(
    private val folderDao: FolderDao,
    private val processedFileDao: ScannedFileDao
) {
    suspend fun addFolder(folder: Folder) {
        folderDao.insert(folder)
    }

    suspend fun getFolder(uri: String): Folder? {
        return folderDao.getFolder(uri)
    }

    fun getFolderWithRules(uri: String): Flow<FolderWithRulesDomain?> {
        return folderDao.getFolderWithRulesFlow(uri).map { folder ->
            if (folder == null) return@map null

            val domainRules = folder.rules.map { it.toDomain() }
            FolderWithRulesDomain(folder.folder.uri, domainRules)
        }
    }

    suspend fun addFolderRuleJoin(folderUri: String, ruleId: Int) {
        val join = FolderRuleJoin(folderUri, ruleId)
        folderDao.insertFolderRuleJoin(join)
    }

    suspend fun deleteFolderRuleJoin(folderUri: String, ruleId: Int) {
        val join = FolderRuleJoin(folderUri, ruleId)
        folderDao.deleteFolderRuleJoin(join)
    }

    suspend fun deleteFolder(folder: Folder) {
        folderDao.delete(folder)
    }

    fun getAllWatchedFoldersFlow(): Flow<List<Folder>> {
        return folderDao.getAllFoldersFlow()
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
