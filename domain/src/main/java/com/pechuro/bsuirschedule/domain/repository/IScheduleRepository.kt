package com.pechuro.bsuirschedule.domain.repository

import com.pechuro.bsuirschedule.domain.entity.*
import kotlinx.coroutines.flow.Flow

interface IScheduleRepository {

    suspend fun getAllSchedules(): Flow<List<Schedule>>

    suspend fun getGroupClassesByName(name: String): Schedule.GroupClasses

    suspend fun getGroupExamsByName(name: String): Schedule.GroupExams

    suspend fun getEmployeeClassesByName(name: String): Schedule.EmployeeClasses

    suspend fun getEmployeeExamsByName(name: String): Schedule.EmployeeExams

    suspend fun <T : Schedule> getScheduleItems(schedule: T): Flow<ScheduleItem<T>>

    suspend fun loadGroupSchedule(group: Group, types: List<ScheduleType>): List<Schedule>

    suspend fun loadEmployeeSchedule(employee: Employee, types: List<ScheduleType>): List<Schedule>

    suspend fun updateAll()

    suspend fun update(schedule: Schedule)

    suspend fun setNotRemindForUpdates(schedule: Schedule, notRemind: Boolean)

    suspend fun isUpdateAvailable(schedule: Schedule): Boolean

    suspend fun delete(schedule: Schedule)

    suspend fun deleteAll()

    suspend fun getCurrentWeek(): Int
}
