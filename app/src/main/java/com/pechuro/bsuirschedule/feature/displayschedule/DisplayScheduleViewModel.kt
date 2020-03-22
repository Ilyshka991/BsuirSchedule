package com.pechuro.bsuirschedule.feature.displayschedule

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.pechuro.bsuirschedule.common.base.BaseViewModel
import com.pechuro.bsuirschedule.domain.common.getOrDefault
import com.pechuro.bsuirschedule.domain.entity.Schedule
import com.pechuro.bsuirschedule.domain.entity.ScheduleItem
import com.pechuro.bsuirschedule.domain.entity.WeekDay
import com.pechuro.bsuirschedule.domain.entity.WeekNumber
import com.pechuro.bsuirschedule.domain.interactor.GetScheduleItems
import com.pechuro.bsuirschedule.ext.flowLiveData
import com.pechuro.bsuirschedule.feature.displayschedule.data.DisplayScheduleItem
import com.pechuro.bsuirschedule.feature.displayschedule.data.DisplayScheduleItemInfo
import kotlinx.coroutines.flow.emptyFlow
import java.util.*
import javax.inject.Inject

class DisplayScheduleViewModel @Inject constructor(
        private val getScheduleItems: GetScheduleItems
) : BaseViewModel() {

    lateinit var schedule: Schedule

    private val scheduleItemList: LiveData<List<ScheduleItem>> = flowLiveData {
        getScheduleItems.execute(GetScheduleItems.Params(schedule)).getOrDefault(emptyFlow())
    }

    fun getCurrentWeekNumber(): WeekNumber {
        val currentCalendar = Calendar.getInstance()
        var year = currentCalendar.get(Calendar.YEAR)
        if (currentCalendar.get(Calendar.MONTH) < 8) year--
        val firstDayCalendar = Calendar.getInstance()
        firstDayCalendar.set(year, Calendar.SEPTEMBER, 1, 0, 0, 0)
        val difference = (currentCalendar.timeInMillis - firstDayCalendar.timeInMillis) / 1000 / 60 / 60 / 24
        var day = firstDayCalendar.get(GregorianCalendar.DAY_OF_WEEK)
        day -= 2
        if (day == -1) {
            day = 6
        }
        val weekNumberIndex = ((difference + day).toInt() / 7) % 4
        return WeekNumber.getForIndex(weekNumberIndex)
    }

    fun getItems(info: DisplayScheduleItemInfo) = scheduleItemList.map { it.filterToDisplayScheduleItems(info) }

    private fun List<ScheduleItem>.filterToDisplayScheduleItems(
            info: DisplayScheduleItemInfo
    ): List<DisplayScheduleItem> = when {
        info is DisplayScheduleItemInfo.DayClasses && schedule is Schedule.GroupClasses ->
            filterToGroupDayClasses(info.weekDay, info.weekNumber)
        info is DisplayScheduleItemInfo.DayClasses && schedule is Schedule.EmployeeClasses ->
            filterToEmployeeDayClasses(info.weekDay, info.weekNumber)
        info is DisplayScheduleItemInfo.WeekClasses && schedule is Schedule.GroupClasses ->
            filterToGroupWeekClasses(info.weekDay)
        info is DisplayScheduleItemInfo.WeekClasses && schedule is Schedule.EmployeeClasses ->
            filterToEmployeeWeekClasses(info.weekDay)
        info is DisplayScheduleItemInfo.Exams && schedule is Schedule.GroupExams ->
            filterToGroupExams()
        info is DisplayScheduleItemInfo.Exams && schedule is Schedule.EmployeeExams ->
            filterToEmployeeExams()
        else -> throw IllegalStateException("DisplayScheduleItemInfo $info is incompatible with ${schedule::class.qualifiedName}")
    }

    private fun List<ScheduleItem>.filterToGroupDayClasses(
            weekDay: WeekDay,
            weekNumber: WeekNumber
    ): List<DisplayScheduleItem.GroupDayClasses> = this
            .asSequence()
            .filterIsInstance<ScheduleItem.GroupLesson>()
            .filter { it.weekDay == weekDay && it.weekNumber == weekNumber }
            .map(DisplayScheduleItem::GroupDayClasses)
            .toList()

    private fun List<ScheduleItem>.filterToEmployeeDayClasses(
            weekDay: WeekDay,
            weekNumber: WeekNumber
    ): List<DisplayScheduleItem.EmployeeDayClasses> = this
            .asSequence()
            .filterIsInstance<ScheduleItem.EmployeeLesson>()
            .filter { it.weekDay == weekDay && it.weekNumber == weekNumber }
            .map(DisplayScheduleItem::EmployeeDayClasses)
            .toList()

    private fun List<ScheduleItem>.filterToGroupWeekClasses(
            weekDay: WeekDay
    ): List<DisplayScheduleItem.GroupWeekClasses> = this
            .asSequence()
            .filterIsInstance<ScheduleItem.GroupLesson>()
            .filter { it.weekDay == weekDay }
            .groupBy { it.subject }
            .values
            .map { groupLessons ->
                val groupLesson = groupLessons.first()
                val weekNumbers = groupLessons.map { it.weekNumber }.sorted()
                DisplayScheduleItem.GroupWeekClasses(groupLesson, weekNumbers)
            }
            .toList()

    private fun List<ScheduleItem>.filterToEmployeeWeekClasses(
            weekDay: WeekDay
    ): List<DisplayScheduleItem.EmployeeWeekClasses> = this
            .asSequence()
            .filterIsInstance<ScheduleItem.EmployeeLesson>()
            .filter { it.weekDay == weekDay }
            .groupBy { it.subject }
            .values
            .map { employeeLessons ->
                val employeeLesson = employeeLessons.first()
                val weekNumbers = employeeLessons.map { it.weekNumber }.sorted()
                DisplayScheduleItem.EmployeeWeekClasses(employeeLesson, weekNumbers)
            }
            .toList()

    private fun List<ScheduleItem>.filterToGroupExams(): List<DisplayScheduleItem.GroupExams> = this
            .filterIsInstance<ScheduleItem.GroupExam>()
            .map(DisplayScheduleItem::GroupExams)

    private fun List<ScheduleItem>.filterToEmployeeExams(): List<DisplayScheduleItem.EmployeeExams> = this
            .filterIsInstance<ScheduleItem.EmployeeExam>()
            .map(DisplayScheduleItem::EmployeeExams)
}