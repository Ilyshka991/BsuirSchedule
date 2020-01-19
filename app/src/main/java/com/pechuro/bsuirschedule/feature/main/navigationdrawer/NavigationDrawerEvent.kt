package com.pechuro.bsuirschedule.feature.main.navigationdrawer

import com.pechuro.bsuirschedule.common.BaseEvent
import com.pechuro.bsuirschedule.domain.entity.Schedule

sealed class NavigationDrawerEvent : BaseEvent() {

    object OpenSettings : NavigationDrawerEvent()

    object AddSchedule : NavigationDrawerEvent()

    data class OpenSchedule(val schedule: Schedule) : NavigationDrawerEvent()
}