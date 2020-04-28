package com.pechuro.bsuirschedule.data.repository

import com.pechuro.bsuirschedule.data.common.BaseRepository
import com.pechuro.bsuirschedule.domain.entity.Schedule
import com.pechuro.bsuirschedule.domain.entity.ScheduleWidgetInfo
import com.pechuro.bsuirschedule.domain.entity.SubgroupNumber
import com.pechuro.bsuirschedule.domain.exception.DataSourceException
import com.pechuro.bsuirschedule.domain.repository.IScheduleRepository
import com.pechuro.bsuirschedule.domain.repository.IWidgetRepository
import com.pechuro.bsuirschedule.local.sharedprefs.LocalScheduleInfo
import com.pechuro.bsuirschedule.local.sharedprefs.LocalScheduleInfo.ScheduleType.*
import com.pechuro.bsuirschedule.local.sharedprefs.LocalScheduleWidgetInfo
import com.pechuro.bsuirschedule.local.sharedprefs.SharedPreferencesManager
import kotlinx.coroutines.runBlocking

class WidgetRepositoryImpl(
        private val sharedPreferencesManager: SharedPreferencesManager,
        private val scheduleRepository: IScheduleRepository
) : BaseRepository(), IWidgetRepository {

    override fun getScheduleWidget(widgetId: Int): ScheduleWidgetInfo? = sharedPreferencesManager
            .getScheduleWidgetInfo(widgetId)
            ?.run { mapToDomainInfo() }

    override fun updateScheduleWidget(info: ScheduleWidgetInfo) {
        removeScheduleWidget(info.widgetId)
        add(info)
    }

    override fun removeScheduleWidget(widgetId: Int) {
        sharedPreferencesManager.removeScheduleWidgetInfo(widgetId)
    }

    private fun add(info: ScheduleWidgetInfo) {
        val localInfo = info.mapToLocalInfo()
        sharedPreferencesManager.addScheduleWidgetInfo(localInfo)
    }

    private fun LocalScheduleWidgetInfo.mapToDomainInfo(): ScheduleWidgetInfo {
        val schedule = runBlocking {
            when (schedule.type) {
                GROUP_CLASSES -> scheduleRepository.getGroupClassesByName(schedule.name)
                GROUP_EXAMS -> scheduleRepository.getGroupExamsByName(schedule.name)
                EMPLOYEE_CLASSES -> scheduleRepository.getEmployeeClassesByName(schedule.name)
                EMPLOYEE_EXAMS -> scheduleRepository.getEmployeeExamsByName(schedule.name)
                else -> null
            }
        } ?: throw DataSourceException.InvalidData
        return ScheduleWidgetInfo(
                widgetId = widgetId,
                schedule = schedule,
                subgroupNumber = SubgroupNumber.getForValue(subgroupNumber)
        )
    }

    private fun ScheduleWidgetInfo.mapToLocalInfo() = LocalScheduleWidgetInfo(
            widgetId = widgetId,
            schedule = LocalScheduleInfo(
                    name = schedule.name,
                    type = when (schedule) {
                        is Schedule.GroupClasses -> GROUP_CLASSES
                        is Schedule.GroupExams -> GROUP_EXAMS
                        is Schedule.EmployeeClasses -> EMPLOYEE_CLASSES
                        is Schedule.EmployeeExams -> EMPLOYEE_EXAMS
                    }
            ),
            subgroupNumber = subgroupNumber.value
    )
}