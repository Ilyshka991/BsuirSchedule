package com.pechuro.bsuirschedule.ui.fragment.drawer

import com.pechuro.bsuirschedule.ui.data.ScheduleInformation
import com.pechuro.bsuirschedule.ui.utils.BaseEvent

sealed class DrawerEvent : BaseEvent() {
    class OnItemLongClick(val info: ScheduleInformation) : DrawerEvent()
    class OnNavigate(val info: ScheduleInformation) : DrawerEvent()
    object OnOpenAddDialog : DrawerEvent()
}
