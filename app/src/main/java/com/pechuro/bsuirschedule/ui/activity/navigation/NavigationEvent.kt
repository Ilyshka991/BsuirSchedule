package com.pechuro.bsuirschedule.ui.activity.navigation

import com.pechuro.bsuirschedule.data.entity.Schedule

sealed class NavigationEvent

class OnScheduleUpdated(val name: String, val type: Int) : NavigationEvent()
class OnScheduleUpdateFail(val name: String, val type: Int) : NavigationEvent()
class OnRequestUpdate(val schedule: Schedule) : NavigationEvent()
