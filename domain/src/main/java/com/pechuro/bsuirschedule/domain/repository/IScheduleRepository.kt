package com.pechuro.bsuirschedule.domain.repository

import com.pechuro.bsuirschedule.domain.entity.Classes
import com.pechuro.bsuirschedule.domain.entity.Schedule
import com.pechuro.bsuirschedule.domain.entity.ScheduleType
import kotlinx.coroutines.flow.Flow

interface IScheduleRepository {

    suspend fun getCurrentWeek(): Int

    suspend fun getAllStoredClasses(): Flow<List<Classes>>

    suspend fun getClasses(name: String, vararg type: ScheduleType): List<Classes>

    suspend fun update(schedule: Schedule)

    suspend fun updateAll()

    suspend fun isUpdateAvailable(schedule: Schedule): Flow<Boolean>

    suspend fun delete(name: String, type: ScheduleType)

    suspend fun deleteByType(vararg type: ScheduleType)

    suspend fun deleteAll()
}
