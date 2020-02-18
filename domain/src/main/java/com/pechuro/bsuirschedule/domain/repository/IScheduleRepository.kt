package com.pechuro.bsuirschedule.domain.repository

import com.pechuro.bsuirschedule.domain.entity.*
import kotlinx.coroutines.flow.Flow

interface IScheduleRepository {

    suspend fun getAllSchedules(): Flow<List<Schedule>>

    suspend fun <T : Schedule> getScheduleItems(schedule: T): Flow<ScheduleItem<T>>

    suspend fun loadGroupSchedule(group: Group, types: List<ScheduleType>)

    suspend fun loadEmployeeSchedule(employee: Employee, types: List<ScheduleType>)

    suspend fun updateAll()

    suspend fun update(schedule: Schedule)

    suspend fun setNotRemindForUpdates(schedule: Schedule, notRemind: Boolean)

    suspend fun isUpdateAvailable(schedule: Schedule): Boolean

    suspend fun delete(schedule: Schedule)

    suspend fun deleteAll()

    suspend fun getCurrentWeek(): Int
}
