package com.pechuro.bsuirschedule.local.dao

import androidx.room.*
import com.pechuro.bsuirschedule.local.entity.staff.GroupCached
import kotlinx.coroutines.flow.Flow

@Dao
interface GroupDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(groups: List<GroupCached>): List<Long>


    @Update
    suspend fun update(group: GroupCached)

    @Update
    suspend fun update(groups: List<GroupCached>)


    @Transaction
    suspend fun insertOrUpdate(groups: List<GroupCached>) {
        val insertResult = insert(groups)
        val updateList = mutableListOf<GroupCached>()
        for (i in insertResult.indices) {
            if (insertResult[i] == -1L) updateList.add(groups[i])
        }
        if (updateList.isNotEmpty()) update(updateList)
    }


    @Query("SELECT * FROM `group`")
    fun getAll(): Flow<List<GroupCached>>

    @Query("SELECT * FROM `group` WHERE id = :id")
    suspend fun getById(id: Long): GroupCached

    @Query("SELECT EXISTS(SELECT 1 FROM `group`)")
    suspend fun isNotEmpty(): Boolean
}