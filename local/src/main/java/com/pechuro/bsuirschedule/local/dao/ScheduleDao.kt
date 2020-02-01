package com.pechuro.bsuirschedule.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Transaction
import com.pechuro.bsuirschedule.local.entity.schedule.*
import com.pechuro.bsuirschedule.local.entity.schedule.complex.EmployeeItemExamComplex
import com.pechuro.bsuirschedule.local.entity.schedule.complex.EmployeeItemClassesComplex
import com.pechuro.bsuirschedule.local.entity.schedule.complex.GroupItemExamComplex
import com.pechuro.bsuirschedule.local.entity.schedule.complex.GroupItemClassesComplex
import com.pechuro.bsuirschedule.local.entity.schedule.crossref.*

@Dao
interface ScheduleDao {

    @Transaction
    suspend fun insertGroupClassesSchedule(schedule: GroupScheduleClassesCached, items: List<GroupItemClassesComplex>) {
        insert(schedule)
        items.forEach {
            val scheduleItemId = insert(it.scheduleItem)
            it.employees.forEach { employee ->
                val joinEntity = GroupLessonEmployeeCrossRef(scheduleItemId, employee.id)
                insert(joinEntity)
            }
            it.auditories.forEach { auditory ->
                val joinEntity = GroupLessonAuditoryCrossRef(scheduleItemId, auditory.id)
                insert(joinEntity)
            }
        }
    }

    @Transaction
    suspend fun insertGroupExamsSchedule(schedule: GroupScheduleExamCached, items: List<GroupItemExamComplex>) {
        insert(schedule)
        items.forEach {
            val scheduleItemId = insert(it.scheduleItem)
            it.employees.forEach { employee ->
                val joinEntity = GroupExamEmployeeCrossRef(scheduleItemId, employee.id)
                insert(joinEntity)
            }
            it.auditories.forEach { auditory ->
                val joinEntity = GroupExamAuditoryCrossRef(scheduleItemId, auditory.id)
                insert(joinEntity)
            }
        }
    }

    @Transaction
    suspend fun insertEmployeeClassesSchedule(schedule: EmployeeScheduleClassesCached, items: List<EmployeeItemClassesComplex>) {
        insert(schedule)
        items.forEach {
            val scheduleItemId = insert(it.scheduleItem)
            it.groups.forEach { group ->
                val joinEntity = EmployeeLessonGroupCrossRef(scheduleItemId, group.id)
                insert(joinEntity)
            }
            it.auditories.forEach { auditory ->
                val joinEntity = EmployeeLessonAuditoryCrossRef(scheduleItemId, auditory.id)
                insert(joinEntity)
            }
        }
    }

    @Transaction
    suspend fun insertEmployeeExamsSchedule(schedule: EmployeeScheduleExamCached, items: List<EmployeeItemExamComplex>) {
        insert(schedule)
        items.forEach {
            val scheduleItemId = insert(it.scheduleItem)
            it.groups.forEach { group ->
                val joinEntity = EmployeeExamGroupCrossRef(scheduleItemId, group.id)
                insert(joinEntity)
            }
            it.auditories.forEach { auditory ->
                val joinEntity = EmployeeExamAuditoryCrossRef(scheduleItemId, auditory.id)
                insert(joinEntity)
            }
        }
    }


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(value: GroupScheduleClassesCached)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(value: GroupItemClassesCached): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(value: GroupLessonEmployeeCrossRef)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(value: GroupLessonAuditoryCrossRef)


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(value: GroupScheduleExamCached)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(value: GroupItemExamCached): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(value: GroupExamEmployeeCrossRef)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(value: GroupExamAuditoryCrossRef)


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(value: EmployeeScheduleClassesCached)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(value: EmployeeItemClassesCached): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(value: EmployeeLessonAuditoryCrossRef)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(value: EmployeeLessonGroupCrossRef)


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(value: EmployeeScheduleExamCached)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(value: EmployeeItemExamCached): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(value: EmployeeExamAuditoryCrossRef)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(value: EmployeeExamGroupCrossRef)
}