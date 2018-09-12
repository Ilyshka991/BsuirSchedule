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
        schedule.schedule.forEach { it.scheduleId = id }
        insert(schedule.schedule)
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(values: Schedule): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(values: List<ScheduleItem>)

    @Query("DELETE FROM all_schedules WHERE name = :name AND type = :type")
    fun delete(name: String, type: Int)

    @Query("DELETE FROM all_schedules WHERE type = :type")
    fun delete(type: Int)

    @Query("SELECT * FROM all_schedules WHERE name = :name AND type = :type")
    fun get(name: String, type: Int): Single<Classes>

    @Query("SELECT all_employees.fio FROM all_employees" +
            " LEFT JOIN all_schedules ON all_schedules.name = all_employees.employee_id" +
            " AND all_schedules.type = :type WHERE all_schedules.name IS NULL")
    fun getNotAddedEmployees(type: Int): Single<List<String>>

    @Query("SELECT all_groups.number FROM all_groups" +
            " LEFT JOIN all_schedules ON all_schedules.name = all_groups.number" +
            " AND all_schedules.type = :type WHERE all_schedules.name IS NULL")
    fun getNotAddedGroups(type: Int): Single<List<String>>
}