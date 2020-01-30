package com.pechuro.bsuirschedule.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Transaction
import com.pechuro.bsuirschedule.local.entity.*

@Dao
interface ScheduleDao {

    @Transaction
    suspend fun insertGroupSchedule(schedule: GroupScheduleCached, items: List<GroupScheduleItemComplex>) {
        insert(schedule)
        items.forEach {
            val scheduleItemId = insert(it.scheduleItem)
            it.employees?.forEach { employee ->
                val joinEntity = GroupScheduleItemEmployeeCrossRef(scheduleItemId, employee.id)
                insert(joinEntity)
            }
            it.auditories?.forEach { auditory ->
                val joinEntity = GroupScheduleItemAuditoryCrossRef(scheduleItemId, auditory.id)
                insert(joinEntity)
            }
        }
    }

    @Transaction
    suspend fun insertEmployeeSchedule(schedule: EmployeeScheduleCached, items: List<EmployeeScheduleItemComplex>) {
        insert(schedule)
        items.forEach {
            val scheduleItemId = insert(it.scheduleItem)
            it.groups?.forEach { group ->
                val joinEntity = EmployeeScheduleItemGroupCrossRef(scheduleItemId, group.id)
                insert(joinEntity)
            }
            it.auditories?.forEach { auditory ->
                val joinEntity = EmployeeScheduleItemAuditoryCrossRef(scheduleItemId, auditory.id)
                insert(joinEntity)
            }
        }
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(value: GroupScheduleCached)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(value: EmployeeScheduleCached)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(value: GroupScheduleItemCached): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(value: EmployeeScheduleItemCached): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(value: GroupScheduleItemEmployeeCrossRef)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(value: EmployeeScheduleItemGroupCrossRef)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(value: GroupScheduleItemAuditoryCrossRef)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(value: EmployeeScheduleItemAuditoryCrossRef)
}