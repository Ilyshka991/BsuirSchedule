package com.pechuro.bsuirschedule.feature.displayschedule

import com.pechuro.bsuirschedule.common.BaseEvent
import com.pechuro.bsuirschedule.domain.entity.ScheduleItem

sealed class DisplayScheduleEvent : BaseEvent() {
    data class OpenScheduleItem(val scheduleItem: ScheduleItem) : DisplayScheduleEvent()
}
