package com.pechuro.bsuirschedule.feature.main.navigationdrawer

import com.pechuro.bsuirschedule.common.BaseEvent
import com.pechuro.bsuirschedule.domain.entity.Schedule
import com.pechuro.bsuirschedule.domain.entity.ScheduleType

sealed class NavigationDrawerEvent : BaseEvent() {

    object OnOpenSettings : NavigationDrawerEvent()

    object OnAddSchedule : NavigationDrawerEvent()

    data class OnScheduleClick(val schedule: Schedule) : NavigationDrawerEvent()

    data class OnScheduleLongClick(val schedule: Schedule) : NavigationDrawerEvent()

    data class OnTitleClick(val scheduleType: ScheduleType) : NavigationDrawerEvent()

    data class OnTitleLongClick(val scheduleType: ScheduleType) : NavigationDrawerEvent()
}