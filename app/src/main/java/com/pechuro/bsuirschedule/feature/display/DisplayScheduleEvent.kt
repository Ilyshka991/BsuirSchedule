package com.pechuro.bsuirschedule.feature.display

import com.pechuro.bsuirschedule.common.BaseEvent
import com.pechuro.bsuirschedule.feature.display.data.DisplayScheduleItem
import java.util.*

sealed class DisplayScheduleEvent : BaseEvent() {

    data class OpenScheduleItemDetails(val data: DisplayScheduleItem) : DisplayScheduleEvent()

    data class OpenScheduleItemOptions(val data: DisplayScheduleItem) : DisplayScheduleEvent()

    data class OnPositionChanged(val position: Int) : DisplayScheduleEvent()

    data class OpenDatePicker(
            val startDate: Date,
            val endDate: Date,
            val currentDate: Date
    ) : DisplayScheduleEvent()
}
