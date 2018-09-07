package com.pechuro.bsuirschedule.repository

import com.pechuro.bsuirschedule.repository.api.ScheduleApi
import com.pechuro.bsuirschedule.repository.db.dao.ScheduleDao

class ScheduleRepository(private val scheduleApi: ScheduleApi, private val scheduleDao: ScheduleDao)
