package com.pechuro.bsuirschedule.repository.db.dao

import android.arch.persistence.room.*
import com.pechuro.bsuirschedule.repository.entity.Group
import io.reactivex.Single

@Dao
interface GroupDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg group: Group)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(groups: List<Group>)

    @Query("SELECT * FROM all_groups")
    fun get(): Single<List<Group>>

    @Query("DELETE FROM all_groups")
    fun delete()

    @Query("SELECT EXISTS(SELECT 1 FROM all_groups)")
    fun isNotEmpty(): Boolean
}