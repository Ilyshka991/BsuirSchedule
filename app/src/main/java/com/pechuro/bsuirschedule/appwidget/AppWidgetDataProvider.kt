package com.pechuro.bsuirschedule.appwidget

import com.pechuro.bsuirschedule.domain.common.getOrDefault
import com.pechuro.bsuirschedule.domain.entity.*
import com.pechuro.bsuirschedule.domain.interactor.GetScheduleItems
import com.pechuro.bsuirschedule.ext.addDays
import com.pechuro.bsuirschedule.ext.getWeekDay
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import java.util.*
import javax.inject.Inject

data class ScheduleWidgetData(
        val isForTomorrow: Boolean,
        val scheduleItems: List<ScheduleItem>
)

class AppWidgetDataProvider @Inject constructor(
        private val getScheduleItems: GetScheduleItems
) {

    fun getScheduleItemsList(info: ScheduleWidgetInfo): ScheduleWidgetData {
        val allScheduleItems = runBlocking {
            getScheduleItems.execute(GetScheduleItems.Params(info.schedule)).getOrDefault(emptyFlow()).first()
        }
        if (info.schedule is Schedule.GroupExams || info.schedule is Schedule.EmployeeExams) {
            return ScheduleWidgetData(
                    isForTomorrow = false,
                    scheduleItems = allScheduleItems
            )
        }
        var isForTomorrow = false
        val currentDayCalendar = Calendar.getInstance()
        val currentWeekDay = currentDayCalendar.getWeekDay()
        val currentWeekNumber = WeekNumber.calculateCurrentWeekNumber()
        val currentDayItems = allScheduleItems.getScheduleItemsFor(
                subgroupNumber = info.subgroupNumber,
                weekNumber = currentWeekNumber,
                weekDay = currentWeekDay
        )

        val resultList = if (currentDayItems.isEmpty() || currentDayItems.last().endTime < LocalTime.current()) {
            isForTomorrow = true
            val nextWeekDay = currentDayCalendar.addDays(1).getWeekDay()
            val nextWeekNumber = if (nextWeekDay == WeekDay.MONDAY) {
                currentWeekNumber.getNext()
            } else {
                currentWeekNumber
            }
            allScheduleItems.getScheduleItemsFor(
                    subgroupNumber = info.subgroupNumber,
                    weekNumber = nextWeekNumber,
                    weekDay = nextWeekDay
            )
        } else {
            currentDayItems
        }
        return ScheduleWidgetData(
                isForTomorrow = isForTomorrow,
                scheduleItems = resultList
        )
    }

    private fun List<ScheduleItem>.getScheduleItemsFor(
            subgroupNumber: SubgroupNumber,
            weekDay: WeekDay,
            weekNumber: WeekNumber
    ) = this
            .asSequence()
            .filterIsInstance<Lesson>()
            .filter {
                it.weekDay == weekDay && it.weekNumber == weekNumber
            }
            .filter {
                when {
                    it is Lesson.EmployeeLesson -> true
                    subgroupNumber == SubgroupNumber.ALL -> true
                    it.subgroupNumber == SubgroupNumber.ALL -> true
                    else -> it.subgroupNumber == subgroupNumber
                }
            }
            .sortedBy { it.startTime }
            .toList()
}
