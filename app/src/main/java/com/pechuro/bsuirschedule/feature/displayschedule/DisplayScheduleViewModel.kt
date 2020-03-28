package com.pechuro.bsuirschedule.feature.displayschedule

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import com.pechuro.bsuirschedule.common.LiveEvent
import com.pechuro.bsuirschedule.common.base.BaseViewModel
import com.pechuro.bsuirschedule.domain.common.Logger
import com.pechuro.bsuirschedule.domain.common.getOrDefault
import com.pechuro.bsuirschedule.domain.entity.Schedule
import com.pechuro.bsuirschedule.domain.entity.Schedule.*
import com.pechuro.bsuirschedule.domain.entity.ScheduleItem
import com.pechuro.bsuirschedule.domain.entity.WeekDay
import com.pechuro.bsuirschedule.domain.entity.WeekNumber
import com.pechuro.bsuirschedule.domain.interactor.GetScheduleItems
import com.pechuro.bsuirschedule.ext.flowLiveData
import com.pechuro.bsuirschedule.feature.displayschedule.data.DisplayScheduleItem
import com.pechuro.bsuirschedule.feature.displayschedule.data.DisplayScheduleItemInfo
import com.pechuro.bsuirschedule.feature.displayschedule.data.DisplayScheduleItemInfo.DayClasses
import com.pechuro.bsuirschedule.feature.displayschedule.data.DisplayScheduleItemInfo.WeekClasses
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.emptyFlow
import java.util.*
import javax.inject.Inject

class DisplayScheduleViewModel @Inject constructor(
        private val getScheduleItems: GetScheduleItems
) : BaseViewModel() {

    val openScheduleItemDetailsEvent = LiveEvent<ScheduleItem>()

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

    fun getItems(info: DisplayScheduleItemInfo) = scheduleItemList.switchMap {
        liveData(context = viewModelScope.coroutineContext + Dispatchers.IO) {
            emit(it.mapToDisplayScheduleItems(info))
        }
    }

    fun onScheduleItemClicked(scheduleItem: ScheduleItem) {
        openScheduleItemDetailsEvent.value = scheduleItem
    }

    private fun List<ScheduleItem>.mapToDisplayScheduleItems(
            info: DisplayScheduleItemInfo
    ): List<DisplayScheduleItem> = when {
        info is DayClasses && schedule is GroupClasses -> {
            mapToGroupDayClasses(info.weekDay, info.weekNumber)
        }
        info is DayClasses && schedule is EmployeeClasses -> {
            mapToEmployeeDayClasses(info.weekDay, info.weekNumber)
        }
        info is WeekClasses && schedule is GroupClasses -> {
            mapToGroupWeekClasses(info.weekDay)
        }
        info is WeekClasses && schedule is EmployeeClasses -> {
            mapToEmployeeWeekClasses(info.weekDay)
        }
        info is DisplayScheduleItemInfo.Exams && schedule is GroupExams -> {
            mapToGroupExams()
        }
        info is DisplayScheduleItemInfo.Exams && schedule is EmployeeExams -> {
            mapToEmployeeExams()
        }
        else -> {
            Logger.e("DisplayScheduleItemInfo $info is incompatible with ${schedule::class.java.simpleName}")
            emptyList()
        }
    }.run {
        if (isEmpty()) listOf(DisplayScheduleItem.Empty) else this
    }

    private fun List<ScheduleItem>.mapToGroupDayClasses(
            weekDay: WeekDay,
            weekNumber: WeekNumber
    ): List<DisplayScheduleItem.GroupDayClasses> = this
            .asSequence()
            .filterIsInstance<ScheduleItem.GroupLesson>()
            .filter { it.weekDay == weekDay && it.weekNumber == weekNumber }
            .map(DisplayScheduleItem::GroupDayClasses)
            .toList()

    private fun List<ScheduleItem>.mapToEmployeeDayClasses(
            weekDay: WeekDay,
            weekNumber: WeekNumber
    ): List<DisplayScheduleItem.EmployeeDayClasses> = this
            .asSequence()
            .filterIsInstance<ScheduleItem.EmployeeLesson>()
            .filter { it.weekDay == weekDay && it.weekNumber == weekNumber }
            .map(DisplayScheduleItem::EmployeeDayClasses)
            .toList()

    private fun List<ScheduleItem>.mapToGroupWeekClasses(
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
                DisplayScheduleItem.GroupWeekClasses(
                        scheduleItem = groupLesson,
                        itemsIdList = groupLessons.map { it.id },
                        weekNumbers = weekNumbers
                )
            }
            .toList()

    private fun List<ScheduleItem>.mapToEmployeeWeekClasses(
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
                DisplayScheduleItem.EmployeeWeekClasses(
                        scheduleItem = employeeLesson,
                        itemsIdList = employeeLessons.map { it.id },
                        weekNumbers = weekNumbers
                )
            }.toList()

    private fun List<ScheduleItem>.mapToGroupExams(): List<DisplayScheduleItem.GroupExams> = this
            .filterIsInstance<ScheduleItem.GroupExam>()
            .map(DisplayScheduleItem::GroupExams)

    private fun List<ScheduleItem>.mapToEmployeeExams(): List<DisplayScheduleItem.EmployeeExams> = this
            .filterIsInstance<ScheduleItem.EmployeeExam>()
            .map(DisplayScheduleItem::EmployeeExams)
}