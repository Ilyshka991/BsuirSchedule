package com.pechuro.bsuirschedule.feature.navigation

import com.pechuro.bsuirschedule.common.BaseEvent
import com.pechuro.bsuirschedule.domain.entity.Schedule
import com.pechuro.bsuirschedule.domain.entity.ScheduleType

sealed class NavigationSheetEvent : BaseEvent() {

    object OnOpenSettings : NavigationSheetEvent()

    object OnAddSchedule : NavigationSheetEvent()

    data class OnScheduleClick(val schedule: Schedule) : NavigationSheetEvent()

    data class OnScheduleLongClick(val schedule: Schedule) : NavigationSheetEvent()

    data class OnTitleClick(val scheduleType: ScheduleType) : NavigationSheetEvent()

    data class OnTitleLongClick(val scheduleType: ScheduleType) : NavigationSheetEvent()
}