package com.pechuro.bsuirschedule.feature.main

import com.pechuro.bsuirschedule.common.BaseEvent

sealed class FabEvent : BaseEvent() {
    object OnFabClick : FabEvent()
    object OnFabShow : FabEvent()
    object OnFabHide : FabEvent()
    object OnFabShowPos : FabEvent()
}

sealed class ScheduleUpdateEvent : BaseEvent() {
    class OnRequestUpdate(val info: ScheduleInformation) : ScheduleUpdateEvent()
}