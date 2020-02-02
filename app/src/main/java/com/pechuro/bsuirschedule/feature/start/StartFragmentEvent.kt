package com.pechuro.bsuirschedule.feature.start

import com.pechuro.bsuirschedule.common.BaseEvent

sealed class StartFragmentEvent : BaseEvent() {

    object AddSchedule : StartFragmentEvent()
}