package com.pechuro.bsuirschedule.repository.db.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import com.pechuro.bsuirschedule.repository.entity.Schedule
import io.reactivex.Single

@Dao
interface ScheduleDao {
    @Query("SELECT * FROM all_schedules")
    fun getSchedules(): Single<List<Schedule>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg schedule: Schedule)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(schedules: List<Schedule>)
}