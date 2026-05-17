package com.rorycd.bowerbird.data

import android.net.Uri
import com.rorycd.bowerbird.rules.Rule
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class RuleRepository @Inject constructor(
    private val folderDao: FolderDao,
    private val ruleDao: RuleDao
) {
    suspend fun addRule(rule: Rule) =
        ruleDao.insert(rule.toEntity())

    suspend fun updateRule(id: Int, rule: Rule) =
        ruleDao.update(rule.toEntity(id))

    suspend fun deleteRule(id: Int) =
        ruleDao.deleteRuleById(id)

    fun getAllRules(): Flow<List<Rule>> =
        ruleDao.getAllRules().map { list -> list.map { it.toDomain() }}

    suspend fun getAllRulesForFolder(uri: Uri): List<Rule> =
        folderDao.getFolderWithRules(uri.toString())?.rules?.map {it.toDomain()} ?: emptyList()

    suspend fun assignRuleToFolder(ruleId: Int, folderUri: Uri) =
        folderDao.insertFolderRuleJoin(
            FolderRuleJoin(folderUri.toString(), ruleId)
        )

    suspend fun unassignRuleFromFolder(ruleId: Int, folderUri: Uri) =
        folderDao.deleteFolderRuleJoin(
            FolderRuleJoin(folderUri.toString(), ruleId)
        )
}
