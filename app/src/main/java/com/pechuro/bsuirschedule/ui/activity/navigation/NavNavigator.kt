package com.pechuro.bsuirschedule.ui.activity.navigation

import com.pechuro.bsuirschedule.data.entity.Schedule
import com.pechuro.bsuirschedule.ui.base.BaseNavigator

interface NavNavigator : BaseNavigator {
    fun onRequestUpdate(schedule: Schedule)

    fun onScheduleUpdated(name: String, type: Int)

    fun onScheduleUpdateFail(name: String, type: Int)
}