package com.pechuro.bsuirschedule.feature.main.drawer

import com.pechuro.bsuirschedule.feature.main.ScheduleInformation
import com.pechuro.bsuirschedule.common.BaseEvent

sealed class DrawerEvent : BaseEvent() {
    class OnItemLongClick(val info: ScheduleInformation) : DrawerEvent()
    class OnNavigate(val info: ScheduleInformation) : DrawerEvent()
    object OnOpenAddDialog : DrawerEvent()
}
