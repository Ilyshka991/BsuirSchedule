package com.pechuro.bsuirschedule.feature.appwidget

import com.pechuro.bsuirschedule.domain.common.getOrDefault
import com.pechuro.bsuirschedule.domain.entity.*
import com.pechuro.bsuirschedule.domain.interactor.GetScheduleItems
import com.pechuro.bsuirschedule.domain.repository.IWidgetRepository
import com.pechuro.bsuirschedule.domain.ext.addDays
import com.pechuro.bsuirschedule.domain.ext.getWeekDay
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import java.util.*
import javax.inject.Inject

data class ScheduleWidgetData(
        val isForTomorrow: Boolean,
        val scheduleItems: List<ScheduleItem>
)

class ScheduleWidgetDataProvider @Inject constructor(
        private val getScheduleItems: GetScheduleItems,
        private val widgetRepository: IWidgetRepository
) {

    fun getWidgetInfo(widgetId: Int) = widgetRepository.getScheduleWidget(widgetId)

    fun getScheduleItemsList(widgetId: Int): ScheduleWidgetData {
        val widgetInfo = widgetRepository.getScheduleWidget(widgetId) ?: return ScheduleWidgetData(
                isForTomorrow = false,
                scheduleItems = emptyList()
        )
        val allScheduleItems = runBlocking {
            getScheduleItems.execute(GetScheduleItems.Params(widgetInfo.schedule)).getOrDefault(emptyFlow()).first()
        }
        val (scheduleItems, isForTomorrow) = when (widgetInfo.schedule) {
            is Schedule.GroupExams, is Schedule.EmployeeExams -> {
                allScheduleItems.filterExams(
                        subgroupNumber = widgetInfo.subgroupNumber,
                        currentDate = LocalDate.current()
                ) to false
            }
            is Schedule.GroupClasses, is Schedule.EmployeeClasses -> {
                var isForTomorrow = false
                val currentDayCalendar = Calendar.getInstance()
                val currentWeekDay = currentDayCalendar.getWeekDay()
                val currentWeekNumber = WeekNumber.calculateCurrentWeekNumber()
                val currentDayItems = allScheduleItems.filterLessons(
                        subgroupNumber = widgetInfo.subgroupNumber,
                        weekNumber = currentWeekNumber,
                        weekDay = currentWeekDay
                )
                val resultList: List<ScheduleItem>
                if (currentDayItems.isEmpty() || currentDayItems.last().endTime < LocalTime.current()) {
                    isForTomorrow = true
                    val nextWeekDay = currentDayCalendar.addDays(1).getWeekDay()
                    val nextWeekNumber = if (nextWeekDay == WeekDay.MONDAY) {
                        currentWeekNumber.getNext()
                    } else {
                        currentWeekNumber
                    }
                    resultList = allScheduleItems.filterLessons(
                            subgroupNumber = widgetInfo.subgroupNumber,
                            weekNumber = nextWeekNumber,
                            weekDay = nextWeekDay
                    )
                } else {
                    resultList = currentDayItems
                }
                resultList to isForTomorrow
            }
        }
        return ScheduleWidgetData(
                isForTomorrow = isForTomorrow,
                scheduleItems = scheduleItems
        )
    }

    private fun List<ScheduleItem>.filterExams(
            subgroupNumber: SubgroupNumber,
            currentDate: LocalDate
    ) = this
            .asSequence()
            .filterIsInstance<Exam>()
            .filter {
                it.date >= currentDate
            }
            .filter {
                when {
                    it is Exam.EmployeeExam -> true
                    subgroupNumber == SubgroupNumber.ALL -> true
                    it.subgroupNumber == SubgroupNumber.ALL -> true
                    else -> it.subgroupNumber == subgroupNumber
                }
            }
            .sortedWith(compareBy<Exam> { it.date }.thenBy { it.startTime })
            .toList()

    private fun List<ScheduleItem>.filterLessons(
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
