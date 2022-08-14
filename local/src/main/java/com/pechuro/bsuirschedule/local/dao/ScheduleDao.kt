package com.pechuro.bsuirschedule.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.pechuro.bsuirschedule.local.entity.schedule.EmployeeClassesScheduleCached
import com.pechuro.bsuirschedule.local.entity.schedule.EmployeeExamScheduleCached
import com.pechuro.bsuirschedule.local.entity.schedule.EmployeeItemClassesCached
import com.pechuro.bsuirschedule.local.entity.schedule.EmployeeItemExamCached
import com.pechuro.bsuirschedule.local.entity.schedule.GroupClassesScheduleCached
import com.pechuro.bsuirschedule.local.entity.schedule.GroupExamScheduleCached
import com.pechuro.bsuirschedule.local.entity.schedule.GroupItemClassesCached
import com.pechuro.bsuirschedule.local.entity.schedule.GroupItemExamCached
import com.pechuro.bsuirschedule.local.entity.schedule.complex.EmployeeClassesItemComplex
import com.pechuro.bsuirschedule.local.entity.schedule.complex.EmployeeExamItemComplex
import com.pechuro.bsuirschedule.local.entity.schedule.complex.GroupClassesItemComplex
import com.pechuro.bsuirschedule.local.entity.schedule.complex.GroupExamItemComplex
import com.pechuro.bsuirschedule.local.entity.schedule.crossref.EmployeeExamAuditoryCrossRef
import com.pechuro.bsuirschedule.local.entity.schedule.crossref.EmployeeExamGroupCrossRef
import com.pechuro.bsuirschedule.local.entity.schedule.crossref.EmployeeLessonAuditoryCrossRef
import com.pechuro.bsuirschedule.local.entity.schedule.crossref.EmployeeLessonGroupCrossRef
import com.pechuro.bsuirschedule.local.entity.schedule.crossref.GroupExamAuditoryCrossRef
import com.pechuro.bsuirschedule.local.entity.schedule.crossref.GroupExamEmployeeCrossRef
import com.pechuro.bsuirschedule.local.entity.schedule.crossref.GroupLessonAuditoryCrossRef
import com.pechuro.bsuirschedule.local.entity.schedule.crossref.GroupLessonEmployeeCrossRef
import kotlinx.coroutines.flow.Flow
import java.util.Date

@Dao
interface ScheduleDao {

    @Transaction
    suspend fun insertGroupClassesSchedule(
        schedule: GroupClassesScheduleCached,
        items: List<GroupClassesItemComplex>
    ) {
        insert(schedule)
        insertGroupClassesItems(*items.toTypedArray())
    }

    @Transaction
    suspend fun insertGroupClassesItems(vararg items: GroupClassesItemComplex) {
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
    suspend fun insertGroupExamsSchedule(
        schedule: GroupExamScheduleCached,
        items: List<GroupExamItemComplex>
    ) {
        insert(schedule)
        insertGroupExamItems(*items.toTypedArray())
    }

    @Transaction
    suspend fun insertGroupExamItems(vararg items: GroupExamItemComplex) {
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
    suspend fun insertEmployeeClassesSchedule(
        schedule: EmployeeClassesScheduleCached,
        items: List<EmployeeClassesItemComplex>
    ) {
        insert(schedule)
        insertEmployeeClassesItems(*items.toTypedArray())
    }

    @Transaction
    suspend fun insertEmployeeClassesItems(vararg items: EmployeeClassesItemComplex) {
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
    suspend fun insertEmployeeExamsSchedule(
        schedule: EmployeeExamScheduleCached,
        items: List<EmployeeExamItemComplex>
    ) {
        insert(schedule)
        insertEmployeeExamItems(*items.toTypedArray())
    }

    @Transaction
    suspend fun insertEmployeeExamItems(vararg items: EmployeeExamItemComplex) {
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

    @Transaction
    suspend fun updateGroupClassesSchedule(
        schedule: GroupClassesScheduleCached,
        items: List<GroupClassesItemComplex>
    ) {
        update(schedule)
        deleteNotUserGroupClassesItems(schedule.name)
        insertGroupClassesItems(*items.toTypedArray())
    }

    @Transaction
    suspend fun updateGroupExamsSchedule(
        schedule: GroupExamScheduleCached,
        items: List<GroupExamItemComplex>
    ) {
        update(schedule)
        deleteNotUserGroupExamItems(schedule.name)
        insertGroupExamItems(*items.toTypedArray())
    }

    @Transaction
    suspend fun updateEmployeeClassesSchedule(
        schedule: EmployeeClassesScheduleCached,
        items: List<EmployeeClassesItemComplex>
    ) {
        update(schedule)
        deleteNotUserEmployeeClassesItems(schedule.name)
        insertEmployeeClassesItems(*items.toTypedArray())
    }

    @Transaction
    suspend fun updateEmployeeExamsSchedule(
        schedule: EmployeeExamScheduleCached,
        items: List<EmployeeExamItemComplex>
    ) {
        update(schedule)
        deleteNotUserEmployeeExamItems(schedule.name)
        insertEmployeeExamItems(*items.toTypedArray())
    }

    @Transaction
    suspend fun addGroupLesson(item: GroupClassesItemComplex) {
        val scheduleItemId = insert(item.scheduleItem)
        item.employees.forEach { employee ->
            val joinEntity = GroupLessonEmployeeCrossRef(scheduleItemId, employee.id)
            insert(joinEntity)
        }
        item.auditories.forEach { auditory ->
            val joinEntity = GroupLessonAuditoryCrossRef(scheduleItemId, auditory.id)
            insert(joinEntity)
        }
    }

    @Transaction
    suspend fun addGroupExam(item: GroupExamItemComplex) {
        val scheduleItemId = insert(item.scheduleItem)
        item.employees.forEach { employee ->
            val joinEntity = GroupExamEmployeeCrossRef(scheduleItemId, employee.id)
            insert(joinEntity)
        }
        item.auditories.forEach { auditory ->
            val joinEntity = GroupExamAuditoryCrossRef(scheduleItemId, auditory.id)
            insert(joinEntity)
        }
    }

    @Transaction
    suspend fun addEmployeeLesson(item: EmployeeClassesItemComplex) {
        val scheduleItemId = insert(item.scheduleItem)
        item.groups.forEach { group ->
            val joinEntity = EmployeeLessonGroupCrossRef(scheduleItemId, group.id)
            insert(joinEntity)
        }
        item.auditories.forEach { auditory ->
            val joinEntity = EmployeeLessonAuditoryCrossRef(scheduleItemId, auditory.id)
            insert(joinEntity)
        }
    }

    @Transaction
    suspend fun addEmployeeExam(item: EmployeeExamItemComplex) {
        val scheduleItemId = insert(item.scheduleItem)
        item.groups.forEach { group ->
            val joinEntity = EmployeeExamGroupCrossRef(scheduleItemId, group.id)
            insert(joinEntity)
        }
        item.auditories.forEach { auditory ->
            val joinEntity = EmployeeExamAuditoryCrossRef(scheduleItemId, auditory.id)
            insert(joinEntity)
        }
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(value: GroupClassesScheduleCached)

    @Update
    suspend fun update(value: GroupClassesScheduleCached)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(value: GroupItemClassesCached): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(value: GroupLessonEmployeeCrossRef)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(value: GroupLessonAuditoryCrossRef)


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(value: GroupExamScheduleCached)

    @Update
    suspend fun update(value: GroupExamScheduleCached)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(value: GroupItemExamCached): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(value: GroupExamEmployeeCrossRef)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(value: GroupExamAuditoryCrossRef)


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(value: EmployeeClassesScheduleCached)

    @Update
    suspend fun update(value: EmployeeClassesScheduleCached)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(value: EmployeeItemClassesCached): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(value: EmployeeLessonAuditoryCrossRef)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(value: EmployeeLessonGroupCrossRef)


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(value: EmployeeExamScheduleCached)

    @Update
    suspend fun update(value: EmployeeExamScheduleCached)

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


    @Query("SELECT * FROM group_schedule_classes WHERE name = :name")
    fun getGroupClassesByName(name: String): GroupClassesScheduleCached

    @Query("SELECT * FROM group_schedule_exam WHERE name = :name")
    fun getGroupExamsByName(name: String): GroupExamScheduleCached

    @Query("SELECT * FROM employee_schedule_classes WHERE name = :name")
    fun getEmployeeClassesByName(name: String): EmployeeClassesScheduleCached

    @Query("SELECT * FROM employee_schedule_exam WHERE name = :name")
    fun getEmployeeExamsByName(name: String): EmployeeExamScheduleCached


    @Query("DELETE FROM group_schedule_classes WHERE name = :name")
    suspend fun deleteGroupClassesSchedule(name: String)

    @Query("DELETE FROM group_schedule_exam WHERE name = :name")
    suspend fun deleteGroupExamSchedule(name: String)

    @Query("DELETE FROM employee_schedule_classes WHERE name = :name")
    suspend fun deleteEmployeeClassesSchedule(name: String)

    @Query("DELETE FROM employee_schedule_exam WHERE name = :name")
    suspend fun deleteEmployeeExamSchedule(name: String)


    @Transaction
    @Query("SELECT * FROM group_item_classes WHERE schedule_name = :scheduleName")
    fun getGroupClassesItems(scheduleName: String): Flow<List<GroupClassesItemComplex>>

    @Transaction
    @Query("SELECT * FROM group_item_exam WHERE schedule_name = :scheduleName")
    fun getGroupExamItems(scheduleName: String): Flow<List<GroupExamItemComplex>>

    @Transaction
    @Query("SELECT * FROM employee_item_classes WHERE schedule_name = :scheduleName")
    fun getEmployeeClassesItems(scheduleName: String): Flow<List<EmployeeClassesItemComplex>>

    @Transaction
    @Query("SELECT * FROM employee_item_exam WHERE schedule_name = :scheduleName")
    fun getEmployeeExamItems(scheduleName: String): Flow<List<EmployeeExamItemComplex>>


    @Transaction
    @Query("SELECT * FROM group_item_classes WHERE id = :id")
    fun getGroupClassesItemByIdFlow(id: Long): Flow<GroupClassesItemComplex>

    @Transaction
    @Query("SELECT * FROM group_item_classes WHERE id = :id")
    fun getGroupClassesItemById(id: Long): GroupClassesItemComplex

    @Transaction
    @Query("SELECT * FROM group_item_exam WHERE id = :id")
    fun getGroupExamItemById(id: Long): Flow<GroupExamItemComplex>

    @Transaction
    @Query("SELECT * FROM employee_item_classes WHERE id = :id")
    fun getEmployeeClassesItemByIdFlow(id: Long): Flow<EmployeeClassesItemComplex>

    @Transaction
    @Query("SELECT * FROM employee_item_classes WHERE id = :id")
    fun getEmployeeClassesItemById(id: Long): EmployeeClassesItemComplex

    @Transaction
    @Query("SELECT * FROM employee_item_exam WHERE id = :id")
    fun getEmployeeExamItemById(id: Long): Flow<EmployeeExamItemComplex>


    @Query("DELETE FROM group_item_classes WHERE id = :id")
    suspend fun deleteGroupClassesItem(id: Long)

    @Query("DELETE FROM group_item_exam WHERE id = :id")
    suspend fun deleteGroupExamItem(id: Long)

    @Query("DELETE FROM employee_item_classes WHERE id = :id")
    suspend fun deleteEmployeeClassesItem(id: Long)

    @Query("DELETE FROM employee_item_exam WHERE id = :id")
    suspend fun deleteEmployeeExamItem(id: Long)

    @Query("DELETE FROM group_item_classes WHERE schedule_name = :scheduleName AND is_added_by_user = 0")
    suspend fun deleteNotUserGroupClassesItems(scheduleName: String)

    @Query("DELETE FROM group_item_exam WHERE schedule_name = :scheduleName AND is_added_by_user = 0")
    suspend fun deleteNotUserGroupExamItems(scheduleName: String)

    @Query("DELETE FROM employee_item_classes WHERE schedule_name = :scheduleName AND is_added_by_user = 0")
    suspend fun deleteNotUserEmployeeClassesItems(scheduleName: String)

    @Query("DELETE FROM employee_item_exam WHERE schedule_name = :scheduleName AND is_added_by_user = 0")
    suspend fun deleteNotUserEmployeeExamItems(scheduleName: String)


    @Query("SELECT week_number FROM group_item_classes INNER JOIN join_group_lesson_employee ON group_item_classes.id = join_group_lesson_employee.schedule_item_id WHERE join_group_lesson_employee.employee_id IN (:employeeIds) AND schedule_name = :scheduleName AND subject = :subject AND subgroup_number = :subgroupNumber AND lesson_type = :lessonType AND start_time = :startTime AND end_time = :endTime AND week_day = :weekDay")
    suspend fun getGroupClassesWeeks(
        scheduleName: String,
        subject: String,
        subgroupNumber: Int,
        lessonType: String,
        startTime: Date,
        endTime: Date,
        weekDay: Int,
        employeeIds: List<Long>
    ): List<Int>

    @Query("SELECT week_number FROM group_item_classes WHERE schedule_name = :scheduleName AND subject = :subject AND subgroup_number = :subgroupNumber AND lesson_type = :lessonType AND start_time = :startTime AND end_time = :endTime AND week_day = :weekDay")
    suspend fun getGroupClassesWeeks(
        scheduleName: String,
        subject: String,
        subgroupNumber: Int,
        lessonType: String,
        startTime: Date,
        endTime: Date,
        weekDay: Int,
    ): List<Int>

    @Query("SELECT week_number FROM employee_item_classes INNER JOIN join_employee_lesson_group ON employee_item_classes.id = join_employee_lesson_group.schedule_item_id WHERE join_employee_lesson_group.group_id IN (:groupIds) AND schedule_name = :scheduleName AND subject = :subject AND subgroup_number = :subgroupNumber AND lesson_type = :lessonType AND start_time = :startTime AND end_time = :endTime AND week_day = :weekDay")
    suspend fun getEmployeeClassesWeeks(
        scheduleName: String,
        subject: String,
        subgroupNumber: Int,
        lessonType: String,
        startTime: Date,
        endTime: Date,
        weekDay: Int,
        groupIds: List<Long>
    ): List<Int>

    @Query("SELECT week_number FROM employee_item_classes WHERE schedule_name = :scheduleName AND subject = :subject AND subgroup_number = :subgroupNumber AND lesson_type = :lessonType AND start_time = :startTime AND end_time = :endTime AND week_day = :weekDay")
    suspend fun getEmployeeClassesWeeks(
        scheduleName: String,
        subject: String,
        subgroupNumber: Int,
        lessonType: String,
        startTime: Date,
        endTime: Date,
        weekDay: Int,
    ): List<Int>

    @Query("SELECT * FROM group_item_classes WHERE id = :id")
    suspend fun getGroupClassesById(id: Long): GroupItemClassesCached

    @Query("SELECT * FROM group_item_exam WHERE id = :id")
    suspend fun getGroupExamById(id: Long): GroupItemExamCached

    @Query("SELECT * FROM employee_item_classes WHERE id = :id")
    suspend fun getEmployeeClassesById(id: Long): EmployeeItemClassesCached

    @Query("SELECT * FROM employee_item_exam WHERE id = :id")
    suspend fun getEmployeeExamById(id: Long): EmployeeItemExamCached


    @Transaction
    suspend fun getGroupClassesWeeks(
        id: Long,
        subject: String,
        subgroupNumber: Int,
        lessonType: String,
        startTime: Date,
        endTime: Date,
        weekDay: Int
    ): List<Int> {
        val scheduleItem = getGroupClassesItemById(id)
        val employeeIds = scheduleItem.employees.map { it.id }
        val scheduleName = scheduleItem.scheduleItem.scheduleName
        return if (employeeIds.isNotEmpty()) {
            getGroupClassesWeeks(
                scheduleName = scheduleName,
                subject = subject,
                subgroupNumber = subgroupNumber,
                lessonType = lessonType,
                startTime = startTime,
                endTime = endTime,
                weekDay = weekDay,
                employeeIds = employeeIds
            )
        } else {
            getGroupClassesWeeks(
                scheduleName = scheduleName,
                subject = subject,
                subgroupNumber = subgroupNumber,
                lessonType = lessonType,
                startTime = startTime,
                endTime = endTime,
                weekDay = weekDay
            )
        }
    }

    @Transaction
    suspend fun getEmployeeClassesWeeks(
        id: Long,
        subject: String,
        subgroupNumber: Int,
        lessonType: String,
        startTime: Date,
        endTime: Date,
        weekDay: Int
    ): List<Int> {
        val scheduleItem = getEmployeeClassesItemById(id)
        val groupIds = scheduleItem.groups.map { it.id }
        val scheduleName = scheduleItem.scheduleItem.scheduleName
        return if (groupIds.isNotEmpty()) {
            getEmployeeClassesWeeks(
                scheduleName = scheduleName,
                subject = subject,
                subgroupNumber = subgroupNumber,
                lessonType = lessonType,
                startTime = startTime,
                endTime = endTime,
                weekDay = weekDay,
                groupIds = groupIds
            )
        } else {
            getEmployeeClassesWeeks(
                scheduleName = scheduleName,
                subject = subject,
                subgroupNumber = subgroupNumber,
                lessonType = lessonType,
                startTime = startTime,
                endTime = endTime,
                weekDay = weekDay
            )
        }

    }
}