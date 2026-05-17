package com.rorycd.bowerbird.data

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.Junction
import androidx.room.Relation

/**
 * Joining entity, connecting rules to assigned folders
 */
@Entity(
    tableName = "folder_rule_join",
    primaryKeys = ["folderUri", "ruleId"],
    foreignKeys = [
        ForeignKey(
            entity = Folder::class,
            parentColumns = ["uri"],
            childColumns = ["folderUri"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = RuleEntity::class,
            parentColumns = ["id"],
            childColumns = ["ruleId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("ruleId"), Index("folderUri")]
)
data class FolderRuleJoin(
    val folderUri: String,
    val ruleId: Int
)

data class FolderWithRules(
    @Embedded val folder: Folder,
    @Relation(
        parentColumn = "uri",
        entityColumn = "id",
        associateBy = Junction(
            value = FolderRuleJoin::class,
            parentColumn = "folderUri",
            entityColumn = "ruleId"
        )
    )
    val rules: List<RuleEntity>
)
