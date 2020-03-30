package com.pechuro.bsuirschedule.feature.displayschedule

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.pechuro.bsuirschedule.domain.entity.WeekDay
import com.pechuro.bsuirschedule.domain.entity.WeekNumber
import com.pechuro.bsuirschedule.ext.addDays
import com.pechuro.bsuirschedule.ext.getWeekDay
import com.pechuro.bsuirschedule.feature.displayschedule.data.DisplayScheduleItemInfo
import com.pechuro.bsuirschedule.feature.displayschedule.data.DisplayScheduleViewType
import com.pechuro.bsuirschedule.feature.displayschedule.fragment.DisplayScheduleFragment
import java.util.*
import kotlin.math.floor

class DisplaySchedulePagerAdapter(
        hostFragment: Fragment,
        val viewType: DisplayScheduleViewType
) : FragmentStateAdapter(hostFragment) {

    companion object {
        private const val DAY_CLASSES_TOTAL_COUNT = 180
        private const val DAY_CLASSES_BACK_COUNT = 30
    }

    override fun getItemCount() = when (viewType) {
        is DisplayScheduleViewType.Exams -> 1
        is DisplayScheduleViewType.WeekClasses -> WeekDay.TOTAL_COUNT
        is DisplayScheduleViewType.DayClasses -> DAY_CLASSES_TOTAL_COUNT
    }

    override fun createFragment(position: Int): Fragment {
        val itemInfo = when (viewType) {
            is DisplayScheduleViewType.Exams -> DisplayScheduleItemInfo.Exams
            is DisplayScheduleViewType.WeekClasses -> DisplayScheduleItemInfo.WeekClasses(
                    weekDay = getWeekdayAt(position)
            )
            is DisplayScheduleViewType.DayClasses -> DisplayScheduleItemInfo.DayClasses(
                    weekDay = getWeekdayAt(position),
                    weekNumber = getWeekNumberAt(position)
            )
        }
        return DisplayScheduleFragment.newInstance(itemInfo)
    }

    fun getStartPosition(): Int = when (viewType) {
        is DisplayScheduleViewType.DayClasses -> DAY_CLASSES_BACK_COUNT
        is DisplayScheduleViewType.WeekClasses -> getCurrentCalendar().getWeekDay().index
        is DisplayScheduleViewType.Exams -> 0
    }

    fun getCalendarAt(position: Int) = getCurrentCalendar().addDays(position - getStartPosition())

    fun getWeekNumberAt(position: Int): WeekNumber {
        val startWeekPosition = getStartPosition() - getWeekdayAt(getStartPosition()).index
        val weeksCount = floor((position.toFloat() - startWeekPosition) / WeekDay.TOTAL_COUNT).toInt()
        val startWeekNumber = when (viewType) {
            is DisplayScheduleViewType.DayClasses -> viewType.startWeekNumber
            else -> throw UnsupportedOperationException()
        }
        val weekNumberIndex = (startWeekNumber.index + weeksCount) % WeekNumber.TOTAL_COUNT
        val normalizedIndex = (WeekNumber.TOTAL_COUNT + weekNumberIndex) % WeekNumber.TOTAL_COUNT
        return WeekNumber.getForIndex(normalizedIndex)
    }

    private fun getCurrentCalendar() = Calendar.getInstance()

    private fun getWeekdayAt(position: Int) = getCalendarAt(position).getWeekDay()
}