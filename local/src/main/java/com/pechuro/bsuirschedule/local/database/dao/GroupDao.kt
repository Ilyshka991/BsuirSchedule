package com.pechuro.bsuirschedule.local.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.pechuro.bsuirschedule.local.database.entity.Group
import io.reactivex.Single

@Dao
interface GroupDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg group: Group)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(groups: List<Group>)

    @Query("DELETE FROM all_groups")
    fun delete()

    @Query("SELECT * FROM all_groups")
    fun getAll(): Single<List<Group>>

    @Query("SELECT number FROM all_groups")
    fun getAllNumbers(): Single<List<String>>

    @Query("SELECT EXISTS(SELECT 1 FROM all_groups)")
    fun isNotEmpty(): Single<Boolean>
}