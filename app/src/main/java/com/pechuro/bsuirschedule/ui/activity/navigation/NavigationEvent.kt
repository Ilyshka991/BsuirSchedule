package com.pechuro.bsuirschedule.ui.activity.navigation

import com.pechuro.bsuirschedule.ui.data.ScheduleInformation

sealed class NavigationEvent

class OnScheduleUpdated(val info: ScheduleInformation) : NavigationEvent()
class OnScheduleUpdateFail(val info: ScheduleInformation) : NavigationEvent()
class OnRequestUpdate(val info: ScheduleInformation) : NavigationEvent()
