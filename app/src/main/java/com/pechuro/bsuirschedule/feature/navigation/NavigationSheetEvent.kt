package com.pechuro.bsuirschedule.feature.navigation

import com.pechuro.bsuirschedule.common.BaseEvent
import com.pechuro.bsuirschedule.domain.entity.Schedule

sealed class NavigationSheetEvent : BaseEvent() {

    object OnOpenSettings : NavigationSheetEvent()

    object OnAddSchedule : NavigationSheetEvent()

    data class OnScheduleClick(val schedule: Schedule) : NavigationSheetEvent()

    data class OnScheduleDeleted(val schedule: Schedule) : NavigationSheetEvent()
}