package com.pechuro.bsuirschedule.repository.db.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import com.pechuro.bsuirschedule.repository.entity.Group
import io.reactivex.Single

@Dao
interface GroupDao {

    @Query("SELECT * FROM all_groups")
    fun getGroups(): Single<List<Group>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg group: Group)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(groups: List<Group>)
}