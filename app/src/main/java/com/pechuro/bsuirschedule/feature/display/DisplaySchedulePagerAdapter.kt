package com.pechuro.bsuirschedule.feature.display

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.pechuro.bsuirschedule.domain.entity.WeekDay
import com.pechuro.bsuirschedule.domain.entity.WeekNumber
import com.pechuro.bsuirschedule.ext.addDays
import com.pechuro.bsuirschedule.ext.getWeekDay
import com.pechuro.bsuirschedule.feature.display.data.DisplayScheduleItemInfo
import com.pechuro.bsuirschedule.feature.display.data.DisplayScheduleViewType
import com.pechuro.bsuirschedule.feature.display.fragment.DisplayScheduleFragment
import java.util.*
import kotlin.math.floor

class DisplaySchedulePagerAdapter(
        hostFragment: Fragment,
        val viewType: DisplayScheduleViewType,
        private val startWeekNumber: WeekNumber
) : FragmentStateAdapter(hostFragment) {

    companion object {
        private const val DAY_CLASSES_TOTAL_COUNT = 180
        private const val DAY_CLASSES_BACK_COUNT = 30
    }

    override fun getItemCount() = when (viewType) {
        DisplayScheduleViewType.EXAMS -> 1
        DisplayScheduleViewType.WEEK_CLASSES -> WeekDay.TOTAL_COUNT
        DisplayScheduleViewType.DAY_CLASSES -> DAY_CLASSES_TOTAL_COUNT
    }

    override fun createFragment(position: Int): Fragment {
        val itemInfo = when (viewType) {
            DisplayScheduleViewType.EXAMS -> DisplayScheduleItemInfo.Exams
            DisplayScheduleViewType.WEEK_CLASSES -> DisplayScheduleItemInfo.WeekClasses(
                    weekDay = getWeekdayAt(position)
            )
            DisplayScheduleViewType.DAY_CLASSES -> DisplayScheduleItemInfo.DayClasses(
                    weekDay = getWeekdayAt(position),
                    weekNumber = getWeekNumberAt(position)
            )
        }
        return DisplayScheduleFragment.newInstance(itemInfo)
    }

    fun getStartPosition(): Int = when (viewType) {
        DisplayScheduleViewType.DAY_CLASSES -> DAY_CLASSES_BACK_COUNT
        DisplayScheduleViewType.WEEK_CLASSES -> getCurrentCalendar().getWeekDay().index
        DisplayScheduleViewType.EXAMS -> 0
    }

    fun getPositionForDate(date: Date): Int? {
        val firstPositionDate = getCalendarAt(0).time
        val diff = date.time - firstPositionDate.time
        val position = diff / 1000 / 60 / 60 / 24
        if (position < 0 || position > itemCount - 1) return null
        return position.toInt()
    }

    fun getCalendarAt(position: Int) = getCurrentCalendar().addDays(position - getStartPosition())

    fun getWeekNumberAt(position: Int): WeekNumber {
        val startWeekPosition = getStartPosition() - getWeekdayAt(getStartPosition()).index
        val weeksCount = floor((position.toFloat() - startWeekPosition) / WeekDay.TOTAL_COUNT).toInt()
        val startWeekNumber = when (viewType) {
            DisplayScheduleViewType.DAY_CLASSES -> startWeekNumber
            else -> throw UnsupportedOperationException()
        }
        val weekNumberIndex = (startWeekNumber.index + weeksCount) % WeekNumber.TOTAL_COUNT
        val normalizedIndex = (WeekNumber.TOTAL_COUNT + weekNumberIndex) % WeekNumber.TOTAL_COUNT
        return WeekNumber.getForIndex(normalizedIndex)
    }

    private fun getCurrentCalendar() = Calendar.getInstance().apply {
        set(Calendar.HOUR_OF_DAY, 0)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
    }

    private fun getWeekdayAt(position: Int) = getCalendarAt(position).getWeekDay()
}