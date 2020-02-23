package com.pechuro.bsuirschedule.data.repository

import com.pechuro.bsuirschedule.data.common.BaseRepository
import com.pechuro.bsuirschedule.domain.entity.Schedule
import com.pechuro.bsuirschedule.domain.repository.IScheduleRepository
import com.pechuro.bsuirschedule.domain.repository.ISessionRepository
import com.pechuro.bsuirschedule.local.sharedprefs.LastOpenedSchedule
import com.pechuro.bsuirschedule.local.sharedprefs.LastOpenedSchedule.ScheduleType.*
import com.pechuro.bsuirschedule.local.sharedprefs.SharedPreferencesManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SessionRepositoryImpl(
        private val sharedPreferencesManager: SharedPreferencesManager,
        private val scheduleRepository: IScheduleRepository
) : BaseRepository(), ISessionRepository {

    override suspend fun getLastOpenedSchedule(): Flow<Schedule?> =
            sharedPreferencesManager.getLastOpenedSchedule().map {
                when (it?.type) {
                    GROUP_CLASSES -> scheduleRepository.getGroupClassesByName(it.name)
                    GROUP_EXAMS -> scheduleRepository.getGroupExamsByName(it.name)
                    EMPLOYEE_CLASSES -> scheduleRepository.getEmployeeClassesByName(it.name)
                    EMPLOYEE_EXAMS -> scheduleRepository.getEmployeeExamsByName(it.name)
                    else -> null
                }
            }

    override suspend fun setLastOpenedSchedule(schedule: Schedule?) {
        val prefsValue = schedule?.run {
            val scheduleType = when (this) {
                is Schedule.GroupClasses -> GROUP_CLASSES
                is Schedule.GroupExams -> GROUP_EXAMS
                is Schedule.EmployeeClasses -> EMPLOYEE_CLASSES
                is Schedule.EmployeeExams -> EMPLOYEE_EXAMS
            }
            LastOpenedSchedule(name, scheduleType)
        }
        sharedPreferencesManager.setLastOpenedSchedule(prefsValue)
    }
}