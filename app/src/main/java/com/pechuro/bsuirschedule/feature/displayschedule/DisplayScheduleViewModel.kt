package com.pechuro.bsuirschedule.feature.displayschedule

import com.pechuro.bsuirschedule.common.base.BaseViewModel
import com.pechuro.bsuirschedule.domain.entity.Schedule
import com.pechuro.bsuirschedule.domain.entity.WeekNumber
import javax.inject.Inject

class DisplayScheduleViewModel @Inject constructor() : BaseViewModel() {

    lateinit var schedule: Schedule

    fun getCurrentWeekNumber(): WeekNumber {
        return WeekNumber.FIRST
    }
}