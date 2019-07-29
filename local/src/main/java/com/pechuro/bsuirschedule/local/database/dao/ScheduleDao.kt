package com.pechuro.bsuirschedule.local.database.dao

import androidx.room.*
import com.pechuro.bsuirschedule.local.database.constants.ScheduleTypes
import com.pechuro.bsuirschedule.local.database.entity.Schedule
import com.pechuro.bsuirschedule.local.database.entity.ScheduleItem
import com.pechuro.bsuirschedule.local.database.entity.complex.Classes
import io.reactivex.Observable
import io.reactivex.Single

@Dao
interface ScheduleDao {

    @Transaction
    fun insert(classes: Classes) {
        val id = insert(classes.schedule)
        classes.items.forEach { it.scheduleId = id }
        insert(classes.items)
    }

    @Transaction
    fun update(classes: Classes) {
        update(classes.schedule)
        deleteItems(classes.schedule.id)
        classes.items.forEach { it.scheduleId = classes.schedule.id }
        insert(classes.items)
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(values: Schedule): Long

    @Update
    fun update(schedule: Schedule)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(values: List<ScheduleItem>)

    @Query("DELETE FROM all_schedules WHERE name = :name AND type = :type")
    fun delete(name: String, type: Int)

    @Query("DELETE FROM schedule_item WHERE schedule_id = :id")
    fun deleteItems(id: Long)

    @Query("DELETE FROM schedule_item WHERE item_id = :id")
    fun deleteItem(id: Int)

    @Query("SELECT * FROM schedule_item JOIN all_schedules ON schedule_item.schedule_id = all_schedules._id WHERE all_schedules.name = :name AND all_schedules.type = :type")
    fun get(name: String, type: Int): Observable<List<ScheduleItem>>

    @Query("SELECT * FROM schedule_item JOIN all_schedules ON schedule_item.schedule_id = all_schedules._id WHERE all_schedules.name = :name AND all_schedules.type = :type AND schedule_item.week_day = :day")
    fun get(name: String, type: Int, day: String): Observable<List<ScheduleItem>>

    @Query("SELECT * FROM schedule_item JOIN all_schedules ON schedule_item.schedule_id = all_schedules._id WHERE all_schedules.name = :name AND all_schedules.type = :type AND schedule_item.week_day = :day AND week_number LIKE '%' || :week || '%'")
    fun get(name: String, type: Int, day: String, week: String): Observable<List<ScheduleItem>>

    @Query("SELECT * FROM schedule_item JOIN all_schedules ON schedule_item.schedule_id = all_schedules._id WHERE all_schedules.name = :name AND all_schedules.type = :type AND schedule_item.week_day = :day AND subgroup_number IN (:subgroups)")
    fun get(name: String, type: Int, day: String, subgroups: Array<Int>): Observable<List<ScheduleItem>>

    @Query("SELECT * FROM schedule_item JOIN all_schedules ON schedule_item.schedule_id = all_schedules._id WHERE all_schedules.name = :name AND all_schedules.type = :type AND schedule_item.week_day = :day AND week_number LIKE '%' || :week || '%' AND subgroup_number IN (:subgroups)")
    fun get(name: String, type: Int, day: String, week: String, subgroups: Array<Int>): Observable<List<ScheduleItem>>

    @Query("SELECT EXISTS(SELECT 1 FROM all_schedules WHERE name = :name AND type = :type AND last_update = :lastUpdate)")
    fun isUpToDate(name: String, type: Int, lastUpdate: String): Boolean

    @Query("SELECT * FROM all_schedules")
    fun getSchedules(): Observable<List<Schedule>>

    @Query("SELECT * FROM all_schedules WHERE type = ${ScheduleTypes.STUDENT_CLASSES} OR type = ${ScheduleTypes.STUDENT_EXAMS}")
    fun getStudentSchedules(): Single<List<Schedule>>
}