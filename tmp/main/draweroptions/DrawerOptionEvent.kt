package com.pechuro.bsuirschedule.feature.main.draweroptions

import com.pechuro.bsuirschedule.common.BaseEvent
import com.pechuro.bsuirschedule.feature.main.ScheduleInformation

sealed class DrawerOptionEvent : BaseEvent() {
    class OnScheduleUpdated(val info: ScheduleInformation) : DrawerOptionEvent()
    class OnScheduleDeleted(val info: ScheduleInformation) : DrawerOptionEvent()
}
