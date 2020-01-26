package com.pechuro.bsuirschedule.local.dao

import androidx.room.*
import com.pechuro.bsuirschedule.local.entity.*

@Dao
interface ScheduleDao {

    @Transaction
    suspend fun insert(value: ClassesCached) {
        insert(value.schedule)
        value.items.forEach {
            val scheduleItemId = insert(it.scheduleItem)
            it.employees?.forEach { employee ->
                val joinEntity = ScheduleItemEmployeeCrossRef(scheduleItemId, employee.id)
                insert(joinEntity)
            }
            it.auditories?.forEach { auditory ->
                val joinEntity = ScheduleItemAuditoryCrossRef(scheduleItemId, auditory.id)
                insert(joinEntity)
            }
            it.groups?.forEach { group ->
                val joinEntity = ScheduleItemGroupCrossRef(scheduleItemId, group.id)
                insert(joinEntity)
            }
        }
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(value: ScheduleCached)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(value: ScheduleItemCached): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(values: List<ScheduleItemCached>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(value: ScheduleItemEmployeeCrossRef)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(value: ScheduleItemAuditoryCrossRef)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(value: ScheduleItemGroupCrossRef)

    @Update
    suspend fun update(schedule: ScheduleCached)

    /*
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