package com.pechuro.bsuirschedule.feature.appwidgetconfiguration

import com.pechuro.bsuirschedule.domain.entity.Schedule
import com.pechuro.bsuirschedule.domain.entity.ScheduleType

sealed class AppWidgetConfigurationScheduleDisplayData {

    data class Title(val scheduleType: ScheduleType) : AppWidgetConfigurationScheduleDisplayData()

    data class Content(
            val schedule: Schedule,
            val checked: Boolean
    ) : AppWidgetConfigurationScheduleDisplayData()

}