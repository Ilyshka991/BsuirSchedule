package com.pechuro.bsuirschedule.feature.displayschedule

import com.pechuro.bsuirschedule.common.BaseEvent
import com.pechuro.bsuirschedule.domain.entity.ScheduleItem
import java.util.*

sealed class DisplayScheduleEvent : BaseEvent() {

    data class OpenScheduleItemDetails(val scheduleItem: ScheduleItem) : DisplayScheduleEvent()

    data class OpenScheduleItemOptions(val scheduleItem: ScheduleItem) : DisplayScheduleEvent()

    data class OnPositionChanged(val position: Int) : DisplayScheduleEvent()

    data class OpenDatePicker(
            val startDate: Date,
            val endDate: Date,
            val currentDate: Date
    ) : DisplayScheduleEvent()
}
