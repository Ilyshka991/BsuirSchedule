package com.pechuro.bsuirschedule.ui.fragment.draweroptions

import com.pechuro.bsuirschedule.ui.data.ScheduleInformation
import com.pechuro.bsuirschedule.ui.utils.BaseEvent

sealed class DrawerOptionEvent : BaseEvent() {
    class OnScheduleUpdated(val info: ScheduleInformation) : DrawerOptionEvent()
    class OnScheduleDeleted(val info: ScheduleInformation) : DrawerOptionEvent()
}
