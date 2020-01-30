package com.pechuro.bsuirschedule.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Transaction
import com.pechuro.bsuirschedule.local.entity.schedule.*
import com.pechuro.bsuirschedule.local.entity.schedule.complex.EmployeeExamComplex
import com.pechuro.bsuirschedule.local.entity.schedule.complex.EmployeeLessonComplex
import com.pechuro.bsuirschedule.local.entity.schedule.complex.GroupExamComplex
import com.pechuro.bsuirschedule.local.entity.schedule.complex.GroupLessonComplex
import com.pechuro.bsuirschedule.local.entity.schedule.crossref.*

@Dao
interface ScheduleDao {

    @Transaction
    suspend fun insertGroupClassesSchedule(schedule: GroupScheduleClassesCached, items: List<GroupLessonComplex>) {
        insert(schedule)
        items.forEach {
            val scheduleItemId = insert(it.scheduleItem)
            it.employees?.forEach { employee ->
                val joinEntity = GroupLessonEmployeeCrossRef(scheduleItemId, employee.id)
                insert(joinEntity)
            }
            it.auditories?.forEach { auditory ->
                val joinEntity = GroupLessonAuditoryCrossRef(scheduleItemId, auditory.id)
                insert(joinEntity)
            }
        }
    }

    @Transaction
    suspend fun insertGroupExamsSchedule(schedule: GroupScheduleExamsCached, items: List<GroupExamComplex>) {
        insert(schedule)
        items.forEach {
            val scheduleItemId = insert(it.scheduleItem)
            it.employees?.forEach { employee ->
                val joinEntity = GroupExamEmployeeCrossRef(scheduleItemId, employee.id)
                insert(joinEntity)
            }
            it.auditories?.forEach { auditory ->
                val joinEntity = GroupExamAuditoryCrossRef(scheduleItemId, auditory.id)
                insert(joinEntity)
            }
        }
    }

    @Transaction
    suspend fun insertEmployeeClassesSchedule(schedule: EmployeeScheduleClassesCached, items: List<EmployeeLessonComplex>) {
        insert(schedule)
        items.forEach {
            val scheduleItemId = insert(it.scheduleItem)
            it.groups?.forEach { group ->
                val joinEntity = EmployeeLessonGroupCrossRef(scheduleItemId, group.id)
                insert(joinEntity)
            }
            it.auditories?.forEach { auditory ->
                val joinEntity = EmployeeLessonAuditoryCrossRef(scheduleItemId, auditory.id)
                insert(joinEntity)
            }
        }
    }

    @Transaction
    suspend fun insertEmployeeExamsSchedule(schedule: EmployeeScheduleExamsCached, items: List<EmployeeExamComplex>) {
        insert(schedule)
        items.forEach {
            val scheduleItemId = insert(it.scheduleItem)
            it.groups?.forEach { group ->
                val joinEntity = EmployeeExamGroupCrossRef(scheduleItemId, group.id)
                insert(joinEntity)
            }
            it.auditories?.forEach { auditory ->
                val joinEntity = EmployeeExamAuditoryCrossRef(scheduleItemId, auditory.id)
                insert(joinEntity)
            }
        }
    }


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(value: GroupScheduleClassesCached)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(value: GroupLessonCached): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(value: GroupLessonEmployeeCrossRef)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(value: GroupLessonAuditoryCrossRef)


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(value: GroupScheduleExamsCached)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(value: GroupExamCached): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(value: GroupExamEmployeeCrossRef)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(value: GroupExamAuditoryCrossRef)


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(value: EmployeeScheduleClassesCached)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(value: EmployeeLessonCached): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(value: EmployeeLessonAuditoryCrossRef)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(value: EmployeeLessonGroupCrossRef)


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(value: EmployeeScheduleExamsCached)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(value: EmployeeExamCached): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(value: EmployeeExamAuditoryCrossRef)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(value: EmployeeExamGroupCrossRef)
}