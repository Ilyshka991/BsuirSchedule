package com.pechuro.bsuirschedule.domain.repository

import com.pechuro.bsuirschedule.domain.entity.Schedule

interface ISessionRepository {

    suspend fun getLastOpenedSchedule(): Schedule?

    suspend fun setLastOpenedSchedule(schedule: Schedule?)
}