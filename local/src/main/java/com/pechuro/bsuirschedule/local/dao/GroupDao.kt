package com.pechuro.bsuirschedule.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.pechuro.bsuirschedule.local.entity.GroupCached
import kotlinx.coroutines.flow.Flow

@Dao
interface GroupDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg group: GroupCached)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(groups: List<GroupCached>)

    @Query("DELETE FROM `group`")
    suspend fun deleteAll()

    @Query("SELECT * FROM `group`")
    fun getAll(): Flow<List<GroupCached>>

    @Query("SELECT number FROM `group`")
    suspend fun getAllNubmers(): List<String>

    @Query("SELECT * FROM `group` WHERE id = :id")
    suspend fun getById(id: Long): GroupCached

    @Query("SELECT EXISTS(SELECT 1 FROM `group`)")
    suspend fun isNotEmpty(): Boolean
}