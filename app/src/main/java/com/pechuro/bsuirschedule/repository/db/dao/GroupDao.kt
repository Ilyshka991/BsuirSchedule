package com.pechuro.bsuirschedule.repository.db.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import com.pechuro.bsuirschedule.repository.entity.Group
import io.reactivex.Single

@Dao
interface GroupDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg group: Group)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(groups: List<Group>)

    @Query("SELECT * FROM all_groups")
    fun getGroups(): Single<List<Group>>

    @Query("SELECT EXISTS(SELECT 1 FROM all_groups)")
    fun isNotEmpty(): Boolean
}