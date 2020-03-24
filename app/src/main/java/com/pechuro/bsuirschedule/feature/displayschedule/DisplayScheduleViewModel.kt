package com.pechuro.bsuirschedule.feature.displayschedule

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.pechuro.bsuirschedule.common.SingleLiveEvent
import com.pechuro.bsuirschedule.common.base.BaseViewModel
import com.pechuro.bsuirschedule.domain.common.getOrDefault
import com.pechuro.bsuirschedule.domain.entity.Schedule
import com.pechuro.bsuirschedule.domain.entity.ScheduleItem
import com.pechuro.bsuirschedule.domain.entity.WeekNumber
import com.pechuro.bsuirschedule.domain.interactor.GetScheduleItems
import com.pechuro.bsuirschedule.ext.flowLiveData
import com.pechuro.bsuirschedule.feature.displayschedule.data.DisplayScheduleItemInfo
import kotlinx.coroutines.flow.emptyFlow
import java.util.*
import javax.inject.Inject

class DisplayScheduleViewModel @Inject constructor(
        private val getScheduleItems: GetScheduleItems
) : BaseViewModel() {

    val openScheduleItemDetailsEvent = SingleLiveEvent<ScheduleItem>()

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

    fun getItems(info: DisplayScheduleItemInfo) = scheduleItemList.map {
        it.filter { scheduleItem ->
            when (info) {
                is DisplayScheduleItemInfo.DayClasses -> when (scheduleItem) {
                    is ScheduleItem.GroupLesson -> scheduleItem.weekDay == info.weekDay && scheduleItem.weekNumber == info.weekNumber
                    is ScheduleItem.EmployeeLesson -> scheduleItem.weekDay == info.weekDay && scheduleItem.weekNumber == info.weekNumber
                    else -> false
                }
                is DisplayScheduleItemInfo.WeekClasses -> when (scheduleItem) {
                    is ScheduleItem.GroupLesson -> scheduleItem.weekDay == info.weekDay
                    is ScheduleItem.EmployeeLesson -> scheduleItem.weekDay == info.weekDay
                    else -> false
                }
                is DisplayScheduleItemInfo.Exams -> true
            }
        }
    }

    fun onScheduleItemClicked(scheduleItem: ScheduleItem) {
        openScheduleItemDetailsEvent.value = scheduleItem
    }
}