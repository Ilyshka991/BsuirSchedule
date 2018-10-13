package com.pechuro.bsuirschedule.data.database.dao

import androidx.room.*
import com.pechuro.bsuirschedule.data.entity.Schedule
import com.pechuro.bsuirschedule.data.entity.ScheduleItem
import com.pechuro.bsuirschedule.data.entity.complex.Classes
import io.reactivex.Observable
import io.reactivex.Single

@Dao
interface ScheduleDao {

    @Transaction
    fun insertSchedule(schedule: Classes) {
        val id: Int = insert(schedule).toInt()
        schedule.schedule.forEach { it.scheduleId = id }
        insert(schedule.schedule)
    }

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(values: Schedule): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(values: List<ScheduleItem>)

    @Query("DELETE FROM all_schedules WHERE name = :name AND type = :type")
    fun delete(name: String, type: Int)

    @Query("DELETE FROM all_schedules WHERE type = :type")
    fun delete(type: Int)

    @Query("SELECT * FROM schedule_item " +
            "JOIN all_schedules " +
            "ON schedule_item.schedule_id = all_schedules._id " +
            "WHERE all_schedules.name = :name " +
            "AND all_schedules.type = :type")
    fun get(name: String, type: Int): Single<List<ScheduleItem>>

    @Query("SELECT * FROM schedule_item " +
            "JOIN all_schedules " +
            "ON schedule_item.schedule_id = all_schedules._id " +
            "WHERE all_schedules.name = :name " +
            "AND all_schedules.type = :type " +
            "AND schedule_item.week_day = :day " +
            "AND weekNumber LIKE '%' || :week || '%'")
    fun get(name: String, type: Int, day: String, week: String): Single<List<ScheduleItem>>

    @Query("SELECT * FROM all_schedules")
    fun getSchedules(): Observable<List<Schedule>>
}