package com.pechuro.bsuirschedule.domain.repository

import com.pechuro.bsuirschedule.domain.entity.Classes
import com.pechuro.bsuirschedule.domain.entity.Schedule
import com.pechuro.bsuirschedule.domain.entity.ScheduleType
import kotlinx.coroutines.flow.Flow

interface IScheduleRepository {

    suspend fun getAllSchedules(): Flow<List<Schedule>>

    suspend fun getAllClasses(): Flow<List<Classes>>

    suspend fun getClasses(name: String, type: ScheduleType): Classes

    suspend fun getClassesList(name: String, types: List<ScheduleType>): List<Classes>

    suspend fun updateCache(schedule: Schedule)

    suspend fun updateCached()

    suspend fun isUpdateAvailable(schedule: Schedule): Flow<Boolean>

    suspend fun delete(name: String, type: ScheduleType)

    suspend fun deleteByType(vararg type: ScheduleType)

    suspend fun deleteAll()

    suspend fun getCurrentWeek(): Int
}
