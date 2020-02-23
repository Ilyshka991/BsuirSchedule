package com.pechuro.bsuirschedule.domain.repository

import com.pechuro.bsuirschedule.domain.entity.Schedule
import kotlinx.coroutines.flow.Flow

interface ISessionRepository {

    suspend fun getLastOpenedSchedule(): Flow<Schedule?>

    suspend fun setLastOpenedSchedule(schedule: Schedule?)
}