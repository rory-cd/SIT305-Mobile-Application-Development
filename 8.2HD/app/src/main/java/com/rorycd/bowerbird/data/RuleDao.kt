package com.rorycd.bowerbird.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object for Room [RuleEntity] table access
 */
@Dao
interface RuleDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(rule: RuleEntity)

    @Update
    suspend fun update(rule: RuleEntity)

    @Delete
    suspend fun delete(rule: RuleEntity)

    @Query("DELETE from rules WHERE id = :id")
    suspend fun deleteRuleById(id: Int) : Int

    @Query("SELECT * from rules")
    fun getAllRules(): Flow<List<RuleEntity>>

    @Query("SELECT * from rules WHERE id = :id")
    suspend fun getRuleById(id: Int): RuleEntity
}
