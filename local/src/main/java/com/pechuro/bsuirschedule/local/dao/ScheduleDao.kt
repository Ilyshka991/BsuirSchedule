package com.pechuro.bsuirschedule.local.dao

import androidx.room.*
import com.pechuro.bsuirschedule.local.entity.schedule.*
import com.pechuro.bsuirschedule.local.entity.schedule.complex.EmployeeClassesItemComplex
import com.pechuro.bsuirschedule.local.entity.schedule.complex.EmployeeExamItemComplex
import com.pechuro.bsuirschedule.local.entity.schedule.complex.GroupClassesItemComplex
import com.pechuro.bsuirschedule.local.entity.schedule.complex.GroupExamItemComplex
import com.pechuro.bsuirschedule.local.entity.schedule.crossref.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ScheduleDao {

    @Transaction
    suspend fun insertGroupClassesSchedule(schedule: GroupClassesScheduleCached, items: List<GroupClassesItemComplex>) {
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
    suspend fun insertGroupExamsSchedule(schedule: GroupExamScheduleCached, items: List<GroupExamItemComplex>) {
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
    suspend fun insertEmployeeClassesSchedule(schedule: EmployeeClassesScheduleCached, items: List<EmployeeClassesItemComplex>) {
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
    suspend fun insertEmployeeExamsSchedule(schedule: EmployeeExamScheduleCached, items: List<EmployeeExamItemComplex>) {
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
    suspend fun insert(value: GroupClassesScheduleCached)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(value: GroupItemClassesCached): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(value: GroupLessonEmployeeCrossRef)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(value: GroupLessonAuditoryCrossRef)


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(value: GroupExamScheduleCached)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(value: GroupItemExamCached): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(value: GroupExamEmployeeCrossRef)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(value: GroupExamAuditoryCrossRef)


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(value: EmployeeClassesScheduleCached)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(value: EmployeeItemClassesCached): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(value: EmployeeLessonAuditoryCrossRef)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(value: EmployeeLessonGroupCrossRef)


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(value: EmployeeExamScheduleCached)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(value: EmployeeItemExamCached): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(value: EmployeeExamAuditoryCrossRef)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(value: EmployeeExamGroupCrossRef)


    @Query("SELECT * FROM group_schedule_classes")
    fun getAllGroupClassesSchedules(): Flow<List<GroupClassesScheduleCached>>

    @Query("SELECT * FROM group_schedule_exam")
    fun getAllGroupExamSchedules(): Flow<List<GroupExamScheduleCached>>

    @Query("SELECT * FROM employee_schedule_classes")
    fun getAllEmployeeClassesSchedules(): Flow<List<EmployeeClassesScheduleCached>>

    @Query("SELECT * FROM employee_schedule_exam")
    fun getAllEmployeeExamSchedules(): Flow<List<EmployeeExamScheduleCached>>


    @Query("DELETE FROM group_schedule_classes WHERE name = :name")
    suspend fun deleteGroupClassesSchedule(name: String)

    @Query("DELETE FROM group_schedule_exam WHERE name = :name")
    suspend fun deleteGroupExamSchedule(name: String)

    @Query("DELETE FROM employee_schedule_classes WHERE name = :name")
    suspend fun deleteEmployeeClassesSchedule(name: String)

    @Query("DELETE FROM employee_schedule_exam WHERE name = :name")
    suspend fun deleteEmployeeExamSchedule(name: String)


    @Query("DELETE FROM group_schedule_classes")
    suspend fun deleteAllGroupClassesSchedules()

    @Query("DELETE FROM group_schedule_exam")
    suspend fun deleteAllGroupExamSchedules()

    @Query("DELETE FROM employee_schedule_classes")
    suspend fun deleteAllEmployeeClassesSchedules()

    @Query("DELETE FROM employee_schedule_exam")
    suspend fun deleteAllEmployeeExamSchedules()
}