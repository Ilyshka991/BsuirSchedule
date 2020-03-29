package com.pechuro.bsuirschedule.feature.flow

import com.pechuro.bsuirschedule.common.BaseEvent

sealed class FlowFragmentEvent : BaseEvent() {

    object DisplayScheduleSetFirstDay : FlowFragmentEvent()

    object DisplayScheduleAddItem : FlowFragmentEvent()
}