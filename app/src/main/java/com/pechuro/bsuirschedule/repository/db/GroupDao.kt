package com.pechuro.bsuirschedule.repository.db

import android.arch.persistence.room.*
import io.reactivex.Single
import com.pechuro.bsuirschedule.repository.entity.Group

@Dao
interface GroupDao {

    @Query("SELECT * FROM groups")
    fun getGroups(): Single<List<Group>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(group: Group)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(groups: List<Group>)
}