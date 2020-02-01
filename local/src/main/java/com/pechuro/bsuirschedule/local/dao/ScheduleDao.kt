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
}