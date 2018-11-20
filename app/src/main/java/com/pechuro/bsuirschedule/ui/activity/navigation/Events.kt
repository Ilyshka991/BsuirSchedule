package com.pechuro.bsuirschedule.ui.activity.navigation

import com.pechuro.bsuirschedule.ui.data.ScheduleInformation
import com.pechuro.bsuirschedule.ui.utils.BaseEvent

sealed class FabEvent : BaseEvent() {
    object OnFabClick : FabEvent()
    object OnFabShow : FabEvent()
    object OnFabHide : FabEvent()
    object OnFabShowPos : FabEvent()
}

sealed class ScheduleUpdateEvent : BaseEvent() {
    class OnRequestUpdate(val info: ScheduleInformation) : ScheduleUpdateEvent()
}