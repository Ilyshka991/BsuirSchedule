package com.pechuro.bsuirschedule.feature.addschedule

import com.pechuro.bsuirschedule.common.BaseEvent

sealed class AddScheduleEvent : BaseEvent() {

    object Complete : AddScheduleEvent()
}