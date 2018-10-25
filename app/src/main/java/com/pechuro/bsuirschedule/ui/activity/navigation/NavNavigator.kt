package com.pechuro.bsuirschedule.ui.activity.navigation

import com.pechuro.bsuirschedule.data.entity.Schedule

interface NavNavigator {
    fun onRequestUpdate(schedule: Schedule)

    fun onScheduleUpdated(name: String, type: Int)

    fun onScheduleUpdateFail(name: String, type: Int)
}