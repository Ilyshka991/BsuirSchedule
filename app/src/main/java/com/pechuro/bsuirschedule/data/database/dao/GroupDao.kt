package com.pechuro.bsuirschedule.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.pechuro.bsuirschedule.data.entity.Group
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
    fun get(): Single<List<Group>>

    @Query("SELECT number FROM all_groups")
    fun getNumbers(): Single<List<String>>

    @Query("SELECT EXISTS(SELECT 1 FROM all_groups)")
    fun isNotEmpty(): Boolean
}