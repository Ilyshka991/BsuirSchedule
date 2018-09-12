package com.pechuro.bsuirschedule.repository.db.dao

import android.arch.persistence.room.*
import com.pechuro.bsuirschedule.repository.entity.Schedule
import com.pechuro.bsuirschedule.repository.entity.ScheduleItem
import com.pechuro.bsuirschedule.repository.entity.complex.Classes
import io.reactivex.Single

@Dao
interface ScheduleDao {

    @Transaction
    fun insertSchedule(schedule: Classes) {
        val id: Int = insert(schedule).toInt()
        schedule.classes.forEach { it.scheduleId = id }
        insert(schedule.classes)
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(values: Schedule): Long


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(values: List<ScheduleItem>)

    @Query("SELECT * FROM all_schedules WHERE name =:group")
    fun get(group: String): Single<Classes>
}