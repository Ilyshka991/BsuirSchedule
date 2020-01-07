package com.pechuro.bsuirschedule.local.dao

import androidx.room.Dao

@Dao
interface ScheduleDao {

    /*@Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(values: ScheduleCached): Long

    @Update
    fun update(schedule: ScheduleCached)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(values: List<ScheduleItemCached>)

    @Query("DELETE FROM all_schedules WHERE name = :name AND type = :type")
    fun delete(name: String, type: Int)

    @Query("DELETE FROM schedule_item WHERE schedule_id = :id")
    fun deleteItems(id: Long)

    @Query("DELETE FROM schedule_item WHERE item_id = :id")
    fun deleteItem(id: Int)

    @Query("SELECT * FROM schedule_item JOIN all_schedules ON schedule_item.schedule_id = all_schedules._id WHERE all_schedules.name = :name AND all_schedules.type = :type")
    fun get(name: String, type: Int): Observable<List<ScheduleItemCached>>

    @Query("SELECT * FROM schedule_item JOIN all_schedules ON schedule_item.schedule_id = all_schedules._id WHERE all_schedules.name = :name AND all_schedules.type = :type AND schedule_item.week_day = :day")
    fun get(name: String, type: Int, day: String): Observable<List<ScheduleItemCached>>

    @Query("SELECT * FROM schedule_item JOIN all_schedules ON schedule_item.schedule_id = all_schedules._id WHERE all_schedules.name = :name AND all_schedules.type = :type AND schedule_item.week_day = :day AND week_number LIKE '%' || :week || '%'")
    fun get(name: String, type: Int, day: String, week: String): Observable<List<ScheduleItemCached>>

    @Query("SELECT * FROM schedule_item JOIN all_schedules ON schedule_item.schedule_id = all_schedules._id WHERE all_schedules.name = :name AND all_schedules.type = :type AND schedule_item.week_day = :day AND subgroup_number IN (:subgroups)")
    fun get(name: String, type: Int, day: String, subgroups: Array<Int>): Observable<List<ScheduleItemCached>>

    @Query("SELECT * FROM schedule_item JOIN all_schedules ON schedule_item.schedule_id = all_schedules._id WHERE all_schedules.name = :name AND all_schedules.type = :type AND schedule_item.week_day = :day AND week_number LIKE '%' || :week || '%' AND subgroup_number IN (:subgroups)")
    fun get(name: String, type: Int, day: String, week: String, subgroups: Array<Int>): Observable<List<ScheduleItemCached>>

    @Query("SELECT EXISTS(SELECT 1 FROM all_schedules WHERE name = :name AND type = :type AND last_update = :lastUpdate)")
    fun isUpToDate(name: String, type: Int, lastUpdate: String): Boolean

    @Query("SELECT * FROM all_schedules")
    fun getSchedules(): Observable<List<ScheduleCached>>

    @Query("SELECT * FROM all_schedules WHERE type = 1 OR type = 2")
    fun getStudentSchedules(): Single<List<ScheduleCached>>*/
}