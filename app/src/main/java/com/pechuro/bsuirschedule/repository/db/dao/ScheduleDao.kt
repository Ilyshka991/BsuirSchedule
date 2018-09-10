package com.pechuro.bsuirschedule.repository.db.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Transaction
import com.pechuro.bsuirschedule.repository.entity.Schedule
import com.pechuro.bsuirschedule.repository.entity.ScheduleItem
import com.pechuro.bsuirschedule.repository.entity.complex.Classes

@Dao
interface ScheduleDao {

    @Transaction
    fun insertSchedule(schedules: Classes) {
        val id: Int = insert(schedules).toInt()
        schedules.classes.forEach { it.scheduleId = id }
        insert(schedules.classes)
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(values: Schedule): Long


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(values: List<ScheduleItem>)
}