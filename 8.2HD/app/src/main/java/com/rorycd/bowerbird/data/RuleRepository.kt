package com.rorycd.bowerbird.data

import android.net.Uri
import com.rorycd.bowerbird.rules.Rule
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RuleRepository @Inject constructor(
    private val folderDao: FolderDao,
    private val ruleDao: RuleDao
) {
    suspend fun addRule(rule: Rule) =
        ruleDao.insert(rule.toEntity())

    suspend fun updateRule(rule: Rule) =
        ruleDao.update(rule.toEntity())

    suspend fun deleteRule(id: Int) =
        ruleDao.deleteRuleById(id)

    suspend fun getRuleById(id: Int) =
        ruleDao.getRuleById(id)

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
